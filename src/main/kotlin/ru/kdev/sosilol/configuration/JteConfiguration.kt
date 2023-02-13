package ru.kdev.sosilol.configuration

import gg.jte.ContentType
import gg.jte.TemplateEngine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Path

@Configuration
class JteConfiguration {

    @Bean
    fun templateEngine(): TemplateEngine {
        if (System.getenv("PRODUCTION") != null)
            return TemplateEngine.createPrecompiled(ContentType.Html)

        return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html)
    }
}