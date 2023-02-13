package ru.kdev.sosilol.test

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import ru.kdev.sosilol.configuration.RedisProperties

@TestConfiguration
class EmbeddedRedisConfiguration(redisProperties: RedisProperties) {

    private val redisServer = RedisServer(redisProperties.port)

    @PostConstruct
    fun init() {
        redisServer.start()
    }

    @PreDestroy
    fun destroy() {
        redisServer.stop()
    }
}