package iut.nantes.project.stores.controller

import io.mockk.every
import iut.nantes.project.stores.AddressEntity
import iut.nantes.project.stores.ContactEntity
import iut.nantes.project.stores.DatabaseProxy
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class StoreControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var db: DatabaseProxy
    
    @Test
    fun createStore() {
        mockMvc.post("/api/v1/stores") {
            contentType = APPLICATION_JSON
            content = """
            {
                "name": "Test Store",
                "contact": {
                    "id": 1,
                    "email": "contact@test.com",
                    "phone": "123456789"
                }
            }
        """
        }.andExpect {
            status { isCreated() }
            content { contentType("application/json") }
            jsonPath("$.name") { value("Test Store") }
            jsonPath("$.contact.email") { value("contact@test.com") }
        }
    }


}