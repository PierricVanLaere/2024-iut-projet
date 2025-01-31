package iut.nantes.project.stores.controller

import iut.nantes.project.stores.DatabaseProxy
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ContactController(val db: DatabaseProxy) {

    @PostMapping("/api/v1/contacts")
    fun createContact(@RequestBody contact: ContactDto): ResponseEntity<ContactDto> {
        val withId = db.saveContact(contact)
        return ResponseEntity.status(HttpStatus.CREATED).body(withId)
    }

    @GetMapping("/api/v1/contacts")
    fun getContacts(@RequestParam city: String?): ResponseEntity<List<ContactDto>> {
        val result = db.findAllContacts()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/api/v1/contacts/{id}")
    fun getContact(@PathVariable id: Int) = db.findContactById(id)?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.notFound().build()

    @PutMapping("/api/v1/contacts/{id}")
    fun updateContact(@RequestBody contact: ContactDto, @PathVariable id: Int): ResponseEntity<ContactDto> {
        val previous = db.findContactById(id)
        if (previous == null) {
            throw IllegalStateException("Contact with ID ${id} not found")
        }else{
            contact.contactId = previous.contactId
            db.saveContact(contact).let { return ResponseEntity.ok(it) }
        }
    }

    // Gérer le cas ou un magasin est lié (409)
    @DeleteMapping("/api/v1/contacts/{id}")
    fun deleteStore(@PathVariable id: Int): ResponseEntity<Void> {
        val previous = db.findContactById(id)
        return if (previous != null) {
            db.deleteContactById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}