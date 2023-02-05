package ru.kdev.sosilol.controller

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kdev.sosilol.service.PasteService
import java.nio.file.NoSuchFileException

@RestController
@Tag(name = "Контроллер паст", description = "В этом контроллере происходит сохранение/чтение паст")
class PasteController(private var pasteService: PasteService) {

    @PostMapping("/save")
    @ApiResponse(description = "ID новой пасты", content = [Content(mediaType = "text/plain")])
    fun saveRequest(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Текст пасты", content = [Content(mediaType = "text/plain")]) @RequestBody text: String) = pasteService.save(text)

    @GetMapping("/view/{id}", produces = ["text/html"])
    fun loadRequest(@PathVariable id: String) = pasteService.load(id)

    @ExceptionHandler(NoSuchFileException::class)
    fun handleExceptions() = ResponseEntity("404 не найдено", HttpHeaders(), HttpStatus.NOT_FOUND)
}