package iut.nantes.project.stores.controller

import io.mockk.every
import iut.nantes.project.stores.DatabaseProxy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.Commit
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class ContactControllerTest {

 @Autowired
 lateinit var mockMvc: MockMvc


 @BeforeEach
 fun setup() {
 }

 @Test
 @Commit
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
 fun getContact() {
 mockMvc.get("/api/v1/contacts/{id}", 1) {
 }.andExpect {
  status { isOk() }
  content { contentType("application/json") }
  jsonPath("$.email") { value("test@gmail.com") }
 }
 }

@Test
 fun updateContact() {
 mockMvc.put("/api/v1/contacts/{id}", 1) {
  contentType = APPLICATION_JSON
  content = contactJson("testUpdate@gmail.com")
 }.andExpect {
  status { isOk() }
  content { contentType("application/json") }
  jsonPath("$.email") { value("testUpdate@gmail.com") }
 }
 }

 @Test
 fun verifyContactBeforeDelete() {
  mockMvc.get("/api/v1/contacts/{id}", 1)
   .andExpect {
    status { isOk() }
    jsonPath("$.email") { value("test@gmail.com") }
   }
 }

@Test
 fun deleteStore() {
 mockMvc.delete("/api/v1/contacts/{id}", 1) {
 }.andExpect {
  status { isNoContent() }
 }
 }

 private fun contactJson(email: String = "test@gmail.com", phone: String = "0123456789", street: String = "1 rue test", city: String = "Nantes", postalCode: String = "4430") = """
        {
            "email": "$email",
            "phone": "$phone",
            "address": {
              "street": "$street",
              "city": "$city",
              "postalCode": "$postalCode"
            }
        }
    """.trimIndent()
}