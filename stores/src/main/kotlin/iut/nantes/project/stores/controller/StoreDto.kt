package iut.nantes.project.stores.controller

import iut.nantes.project.stores.ContactEntity
import iut.nantes.project.stores.ProductEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class StoreDto (
    var id: Int,
    var name: String,
    var contact: ContactEntity,
    var products: Set<ProductEntity>,
)

data class StoreUpdateRequest(
    @field:NotBlank(message = "Le nom ne peut pas être vide.")
    @field:Size(min = 3, max = 30, message = "Le nom doit contenir entre 3 et 30 caractères.")
    val name: String,

    @field:NotNull(message = "Le contact ne peut pas être nul.")
    val contactId: Int
)