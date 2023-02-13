package ru.kdev.sosilol.service

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.kdev.sosilol.configuration.GithubConfiguration
import ru.kdev.sosilol.entity.Profile
import ru.kdev.sosilol.repository.ProfileRepository

sealed interface GithubService {

    fun authorize(code: String, state: String): AccessTokenInfo?
    fun getProfile(token: String): GithubProfile
    fun getRawProfile(token: String): GithubProfile
    fun renderProfilePage(profile: GithubProfile): String
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GithubProfile(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    var pastes: List<ru.kdev.sosilol.entity.Paste> = listOf()
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AccessTokenInfo(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)

@Service
class GithubServiceImpl(val templateEngine: TemplateEngine, val configuration: GithubConfiguration, val profileRepository: ProfileRepository) : GithubService {

    private val restTemplate = RestTemplate()

    override fun authorize(code: String, state: String): AccessTokenInfo? {
        val request = HttpEntity(
            JSONObject()
                .put("client_id", configuration.clientId)
                .put("client_secret", configuration.clientSecret)
                .put("code", code)
                .toString(),
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                accept = listOf(MediaType.APPLICATION_JSON)
            }
        )

        return try {
            val response = restTemplate.postForEntity(
                "https://github.com/login/oauth/access_token",
                request,
                AccessTokenInfo::class.java
            )

            response.body
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            null
        }
    }

    override fun getRawProfile(token: String): GithubProfile =
        restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            HttpEntity<Any>(HttpHeaders().apply {
                setBearerAuth(token)
                accept = listOf(MediaType.APPLICATION_JSON)
            }),
            GithubProfile::class.java
        ).body!!

    override fun getProfile(token: String): GithubProfile {
        val githubProfile = restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            HttpEntity<Any>(HttpHeaders().apply {
                setBearerAuth(token)
                accept = listOf(MediaType.APPLICATION_JSON)
            }),
            GithubProfile::class.java
        ).body!!
        val profileEntity = profileRepository.findById(githubProfile.id).orElseGet {
            val profile = Profile(githubProfile.id)
            profileRepository.save(profile)
            profile
        }
        githubProfile.pastes = profileEntity.pastes
        return githubProfile
    }

    override fun renderProfilePage(profile: GithubProfile): String = StringOutput().apply {
        templateEngine.render("profile.jte", profile, this)
    }.toString()
}