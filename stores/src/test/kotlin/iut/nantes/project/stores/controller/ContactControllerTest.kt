package iut.nantes.project.stores.controller

import io.mockk.every
import iut.nantes.project.stores.DatabaseProxy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

 @Autowired
 lateinit var mockMvc: MockMvc

 @Autowired
 lateinit var databaseProxy: DatabaseProxy

 @BeforeEach
 fun setup() {
 }

 @Test
 fun createContact() {
  mockMvc.post("/api/v1/contacts") {
   contentType = APPLICATION_JSON
   content = contactJson()
  }.andExpect {
   status { isCreated() }
   content { contentType("application/json") }
   jsonPath("$.email") { value("test@gmail.com") }
  }
 }

@Test
 fun getContacts() {
  mockMvc.get("/api/v1/contacts") {
  }.andExpect {
   status { isOk() }
   content { contentType("application/json") }
   jsonPath("$[0].email") { value("test@gmail.com") }
  }
 }

@Test
 fun getContact() {}

@Test
 fun updateContact() {}

@Test
 fun deleteStore() {}

 private fun contactJson(email: String = "test@gmail.com", phone: String = "0123456789", street: String = "1 rue test", city: String = "Nantes", postalCode: String = "4430") = """
        {
            "email": "$email",
            "phone": "$phone",
            "address": [
                {
                    "street": "$street",
                    "city": "$city",
                    "postalCode": "$postalCode"
                }
            ]
        }
    """.trimIndent()
}