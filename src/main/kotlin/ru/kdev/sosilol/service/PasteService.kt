package ru.kdev.sosilol.service

import org.springframework.stereotype.Service
import ru.kdev.sosilol.SosiLolApplication
import ru.kdev.sosilol.data.Paste
import te4j.Te4j
import te4j.template.option.output.Output
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

sealed interface PasteService {

    /**
     * "If the file doesn't exist, create it, then write the text to it."
     *
     * The first thing we do is call the `ensureCreatedSaveDir()` function. This function is defined in the `FileSystem`
     * class, and it's purpose is to make sure that the `saveDir` directory exists. If it doesn't exist, it creates it
     *
     * @param text The text to save
     * @return The id of the file that was saved.
     */
    fun save(text: String): String

    /**
     * It loads the template, replaces the placeholder with the code, and returns the result
     *
     * @param id The id of the snippet.
     * @return The template is being returned with the code-here section replaced with the contents of the file with the
     * given id.
     */
    fun load(id: String): String

}

@Service
class PasteServiceImpl : PasteService {

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val saveDir = Paths.get("pastes")
    private val template = Te4j.custom()
        .minifyAll()
        .useResources()
        .output(Output.STRING)
        .disableAutoReloading()
        .build()
        .load(Paste::class.java)
        .from("static/template.html")
    private val pasteCache = hashMapOf<String, Paste>()

    private fun ensureCreatedSaveDir() {
        if (!Files.exists(saveDir) || !Files.isDirectory(saveDir))
            Files.createDirectory(saveDir)
    }

    private fun randomString() = (1..12)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    override fun save(text: String): String {
        ensureCreatedSaveDir()
        val id = randomString()
        val path = saveDir.resolve(id)

        if (!Files.exists(path) || Files.isDirectory(path))
            Files.createFile(path)

        Files.write(path, text.toByteArray())

        return id
    }

    override fun load(id: String): String {
        ensureCreatedSaveDir()
        return template.renderAsString(pasteCache[id] ?: Paste(Files.readAllLines(saveDir.resolve(id)).joinToString("\n")).apply { pasteCache[id] = this })
    }
}