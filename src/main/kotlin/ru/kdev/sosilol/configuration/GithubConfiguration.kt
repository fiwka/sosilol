package ru.kdev.sosilol.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "github")
class GithubConfiguration {

    lateinit var clientId: String
    lateinit var clientSecret: String
    lateinit var redirectUri: String
}