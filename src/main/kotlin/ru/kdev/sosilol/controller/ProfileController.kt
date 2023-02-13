package ru.kdev.sosilol.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.kdev.sosilol.service.GithubService

@RestController
class ProfileController(private var githubService: GithubService) {

    @GetMapping("/profile")
    suspend fun profile(session: HttpSession, response: HttpServletResponse): String? {
        val accountType = session.getAttribute("account_type")

        if (accountType == null) {
            response.sendRedirect("/requestAuth")
            return null
        }

        val accessToken = session.getAttribute("access_token")

        return githubService.renderProfilePage(githubService.getProfile(accessToken as String))
    }
}