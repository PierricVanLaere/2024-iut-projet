package iut.nantes.project.stores.controller

import iut.nantes.project.stores.DatabaseProxy
import iut.nantes.project.stores.toDto
import iut.nantes.project.stores.toEntity
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StoreController(val db : DatabaseProxy) {
    @PostMapping("/api/v1/stores")
    fun createStore(@RequestBody storeRequest: StoreDto): ResponseEntity<StoreDto> {
        if (storeRequest.name.isBlank()) {
            return ResponseEntity.badRequest().build()
        }

        val existingStore = db.findAllStores().find { it.name == storeRequest.name }
        if (existingStore != null) {
            return ResponseEntity.badRequest().build()
        }

        val existingContact = db.findContactById(storeRequest.contact.id!!)
            ?: db.saveContact(storeRequest.contact.toDto())

        val storeToSave = StoreDto(
            id = 0,
            name = storeRequest.name,
            contact = existingContact.toEntity(),
            products = emptySet() // On ignore la liste des produits
        )

        val savedStore = db.saveStore(storeToSave)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStore)
    }
}