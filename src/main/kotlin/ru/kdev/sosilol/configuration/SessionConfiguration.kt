package ru.kdev.sosilol.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer

@Configuration
@EnableRedisHttpSession
class SessionConfiguration : AbstractHttpSessionApplicationInitializer() {

    @Bean
    fun connectionFactory(properties: RedisProperties) = LettuceConnectionFactory(RedisStandaloneConfiguration(properties.host, properties.port))
}