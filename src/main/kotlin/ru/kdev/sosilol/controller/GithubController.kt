package ru.kdev.sosilol.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.kdev.sosilol.configuration.GithubConfiguration
import ru.kdev.sosilol.service.GithubService

@RestController
class GithubController(private var githubService: GithubService, private var githubConfiguration: GithubConfiguration) {

    @GetMapping("/auth*")
    fun auth(session: HttpSession, response: HttpServletResponse, @RequestParam("code") code: String, @RequestParam("state") state: String): ResponseEntity<*> {
        if (state != session.id) {
            return ResponseEntity("Invalid state", HttpStatus.BAD_REQUEST)
        }

        githubService.authorize(code, state)?.let {
            session.setAttribute("account_type", "github")
            session.setAttribute("access_token", it.accessToken)
            response.sendRedirect("/profile")
        }

        return ResponseEntity("Redirecting...", HttpStatus.MOVED_PERMANENTLY)
    }

    @GetMapping("/requestAuth")
    fun requestAuth(session: HttpSession, response: HttpServletResponse) {
        response.sendRedirect("https://github.com/login/oauth/authorize?client_id=${githubConfiguration.clientId}&redirect_uri=${githubConfiguration.redirectUri}&scope=user&state=${session.id}")
    }
}