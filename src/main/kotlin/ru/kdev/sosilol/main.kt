package ru.kdev.sosilol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class SosiLolApplication

fun main(args: Array<String>) {
    runApplication<SosiLolApplication>(*args)
}
