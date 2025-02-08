package iut.nantes.project.stores.controller

import iut.nantes.project.stores.DatabaseProxy
import iut.nantes.project.stores.StoreEntity
import iut.nantes.project.stores.toDto
import iut.nantes.project.stores.toEntity
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
            products = emptySet()
        )

        val savedStore = db.saveStore(storeToSave)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStore)
    }

    @GetMapping("/api/v1/stores")
    fun getAllStores(): ResponseEntity<List<Any>> {
        val stores = db.findAllStores()
        return ResponseEntity.ok(stores)
    }

    @GetMapping("/api/v1/stores/{id}")
    fun getStoreById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val storeId = id.toIntOrNull() ?: return ResponseEntity.badRequest().build()
            val store = db.getStoreById(storeId)
            ResponseEntity.ok(store)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/api/v1/stores/{id}")
    fun updateStore(
        @PathVariable id: String,
        @RequestBody @Valid storeUpdateRequest: StoreUpdateRequest
    ): ResponseEntity<Any> {
        val storeId = id.toIntOrNull() ?: return ResponseEntity.badRequest().build()

        return try {
            val updatedStore = db.updateStore(storeId, storeUpdateRequest)
            ResponseEntity.ok(updatedStore)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }


    @DeleteMapping("/api/v1/stores/{id}")
    fun deleteStore(@PathVariable id: String): ResponseEntity<Void> {
        val storeId = id.toIntOrNull() ?: return ResponseEntity.badRequest().build()

        return try {
            db.deleteStore(storeId)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }
    
}