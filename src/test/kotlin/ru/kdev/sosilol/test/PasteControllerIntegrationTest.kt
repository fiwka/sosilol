package ru.kdev.sosilol.test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.kdev.sosilol.SosiLolApplication

const val CODE_TO_SAVE = "My super dooper code"

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = [SosiLolApplication::class, EmbeddedRedisConfiguration::class]
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = ["classpath:testapp.properties"]
)
class PasteControllerIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun saveAndLoadRaw() {
        mvc.perform(
            post("/save")
                .contentType(MediaType.TEXT_PLAIN)
                .content(CODE_TO_SAVE)
        )
            .andExpect(status().isOk)
            .andDo {
                val body = mvc.perform(get("/raw/${it.response.contentAsString}"))
                    .andExpect(status().isOk)
                    .andReturn()
                    .response
                    .contentAsString

                assertEquals(CODE_TO_SAVE, body)
            }
    }

    @Test
    fun saveAndLoad() {
        mvc.perform(
            post("/save")
                .contentType(MediaType.TEXT_PLAIN)
                .content(CODE_TO_SAVE)
        )
            .andExpect(status().isOk)
            .andDo { mvcResult ->
                mvc.perform(get("/view/${mvcResult.response.contentAsString}"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andDo {
                        assertTrue(it.response.contentAsString.contains(CODE_TO_SAVE))
                    }
            }
    }
}