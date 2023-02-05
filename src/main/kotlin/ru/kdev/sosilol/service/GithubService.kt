package ru.kdev.sosilol.service

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.kdev.sosilol.configuration.GithubConfiguration
import ru.kdev.sosilol.data.Paste
import te4j.Te4j
import te4j.template.option.output.Output

sealed interface GithubService {

    fun authorize(code: String, state: String): AccessTokenInfo?
    fun getProfile(token: String): GithubProfile
    fun renderProfilePage(profile: GithubProfile): String
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GithubProfile(
    val id: Int,
    val login: String,
    val avatarUrl: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AccessTokenInfo(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)

@Service
class GithubServiceImpl(val configuration: GithubConfiguration) : GithubService {

    private val restTemplate = RestTemplate()
    private val template = Te4j.custom()
        .minifyAll()
        .useResources()
        .output(Output.STRING)
        .disableAutoReloading()
        .build()
        .load(GithubProfile::class.java)
        .from("static/profile.html")

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

    override fun getProfile(token: String): GithubProfile =
        restTemplate.exchange(
            "https://api.github.com/user",
            HttpMethod.GET,
            HttpEntity<Any>(HttpHeaders().apply {
                setBearerAuth(token)
                accept = listOf(MediaType.APPLICATION_JSON)
            }),
            GithubProfile::class.java
        ).body!!

    override fun renderProfilePage(profile: GithubProfile): String = template.renderAsString(profile)
}