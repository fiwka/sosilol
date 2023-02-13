package ru.kdev.sosilol.service

import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.security.crypto.codec.Hex
import org.springframework.stereotype.Service
import ru.kdev.sosilol.data.Paste
import ru.kdev.sosilol.entity.Profile
import ru.kdev.sosilol.repository.PasteRepository
import ru.kdev.sosilol.repository.ProfileRepository
import java.nio.file.Files
import java.nio.file.Paths
import java.security.SecureRandom
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

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
    suspend fun save(text: String, token: String? = null): Future<String>
    suspend fun saveAsync(id: String, text: String, token: String? = null)

    /**
     * It loads the template, replaces the placeholder with the code, and returns the result
     *
     * @param id The id of the snippet.
     * @return The template is being returned with the code-here section replaced with the contents of the file with the
     * given id.
     */
    suspend fun load(id: String): String

    suspend fun loadRaw(id: String): String

}

@Service
class PasteServiceImpl(val templateEngine: TemplateEngine, val githubService: GithubService, val pasteRepository: PasteRepository, val profileRepository: ProfileRepository) : PasteService {

    private val saveDir = Paths.get("pastes")
    private val pasteCache = hashMapOf<String, Paste>()
    private val secureRandom = SecureRandom()

    private fun ensureCreatedSaveDir() {
        if (!Files.exists(saveDir) || !Files.isDirectory(saveDir))
            Files.createDirectory(saveDir)
    }

    private fun randomString(): String {
        val bytes = ByteArray(7)
        secureRandom.nextBytes(bytes)
        return String(Hex.encode(bytes))
    }

    override suspend fun saveAsync(id: String, text: String, token: String?) {
        coroutineScope {
            launch {
                val paste = ru.kdev.sosilol.entity.Paste(id, text)

                if (token != null) {
                    val githubProfile = githubService.getRawProfile(token)
                    val profileEntity = profileRepository.findById(githubProfile.id).orElse(Profile(githubProfile.id))

                    profileEntity.pastes.add(paste)
                    pasteRepository.save(paste)
                    profileRepository.save(profileEntity)
                } else {
                    pasteRepository.save(paste)
                }
            }
        }
    }

    override suspend fun save(text: String, token: String?): Future<String> {
        val id = randomString()

        withContext(Dispatchers.Default) {
            launch {
                saveAsync(id, text, token)
            }
        }

        return CompletableFuture.completedFuture(id)
    }

    suspend fun getPaste(id: String): Paste {
        return if (pasteCache[id] != null) {
            pasteCache[id]!!
        } else {
            if (id.length != 12) { // new ids
                Paste(pasteRepository.findById(id).get().code).apply { pasteCache[id] = this }
            } else { // old ids
                Paste(Files.readAllLines(saveDir.resolve(id)).joinToString("\n")).apply { pasteCache[id] = this }
            }
        }
    }

    override suspend fun load(id: String): String {
        ensureCreatedSaveDir()
        return StringOutput().apply {
            templateEngine.render("template.jte", getPaste(id), this)
        }.toString()
    }

    override suspend fun loadRaw(id: String): String {
        return getPaste(id).code
    }
}