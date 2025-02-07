package iut.nantes.project.stores.controller

data class ContactDto(var contactId: Int?, var email: String, var phone: String, var address: AddressDto)

data class AddressDto(val addressId: Int?, val street: String, val city: String, val postalCode: String)