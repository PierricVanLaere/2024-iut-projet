package iut.nantes.project.stores.controller

import iut.nantes.project.stores.AddressEntity

data class ContactDto(val contactId: Int?, val email: String, val phone: String, val address: List<AddressEntity>)

data class AddressDto(val street: String, val city: String, val postalCode: String)