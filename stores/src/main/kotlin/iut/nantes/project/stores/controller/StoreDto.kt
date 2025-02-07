package iut.nantes.project.stores.controller

import iut.nantes.project.stores.ContactEntity
import iut.nantes.project.stores.ProductEntity

data class StoreDto (
    var id: Int,
    var name: String,
    var contact: ContactEntity,
    var products: Set<ProductEntity>,
)