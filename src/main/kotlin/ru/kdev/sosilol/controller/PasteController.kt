package ru.kdev.sosilol.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kdev.sosilol.service.PasteService
import java.nio.file.NoSuchFileException
import java.util.concurrent.Future

@RestController
@Tag(name = "Контроллер паст", description = "В этом контроллере происходит сохранение/чтение паст")
class PasteController(private var pasteService: PasteService) {

    @PostMapping("/save")
    @Operation(summary = "Сохранение новой пасты", description = "Сохранение новой пасты")
    @ApiResponse(description = "ID новой пасты", content = [Content(mediaType = "text/plain")])
    suspend fun saveRequest(@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Текст пасты", content = [Content(mediaType = "text/plain")]) @RequestBody text: String, session: HttpSession): Future<String> {
        val accountType = session.getAttribute("account_type")

        return if (accountType != null) {
            val accessToken = session.getAttribute("access_token") as String
            pasteService.save(text, accessToken)
        } else {
            pasteService.save(text)
        }
    }

    @GetMapping("/view/{id}", produces = ["text/html"])
    suspend fun loadRequest(@PathVariable id: String) = pasteService.load(id)

    @GetMapping("/raw/{id}", produces = ["text/plain"])
    @Operation(summary = "Получение пасты в сыром виде", description = "Получение пасты в сыром виде")
    @ApiResponse(description = "Паста в сыром виде", content = [Content(mediaType = "text/plain")])
    suspend fun rawRequest(@Parameter(name = "id", description = "ID пасты") @PathVariable id: String) = pasteService.loadRaw(id)

    @ExceptionHandler(NoSuchFileException::class)
    fun handleExceptions() = ResponseEntity("404 не найдено", HttpHeaders(), HttpStatus.NOT_FOUND)
}