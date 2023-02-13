package ru.kdev.sosilol.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(value = "redis")
class RedisProperties {

    lateinit var host: String
    var port by Delegates.notNull<Int>()
}