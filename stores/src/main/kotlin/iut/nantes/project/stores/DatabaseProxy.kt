package iut.nantes.project.stores

import iut.nantes.project.stores.controller.AddressDto
import iut.nantes.project.stores.controller.ContactDto
import org.springframework.stereotype.Service

private fun ContactEntity.toDto(): ContactDto =
    ContactDto(id,email,phone, address.toDto())

private fun ContactDto.toEntity(): ContactEntity {
    val c = ContactEntity(contactId,email,phone, listOf())
    c.address = address
    return c
}

private fun List<AddressEntity>.toDto(): AddressDto =
    listOf(AddressDto(street, city, postalCode))

private fun List<AddressDto>.toEntity(): AddressEntity =
    AdressEntity(street,city,postalCode)

@Service
class DatabaseProxy(private val contactJpa: ContactJpa) {
    fun saveContact(contactDto: ContactDto): ContactDto {
        val contactEntity = contactDto.toEntity()
        val savedContact = contactJpa.save(contactEntity)
        return savedContact.toDto()
    }

    fun findContactById(id: Int): ContactDto? {
        val contactEntity = contactJpa.findById(id)
        return contactEntity.map { it.toDto() }.orElse(null)
    }

    fun findAllContacts(): List<ContactDto> {
        return contactJpa.findAll().map { it.toDto() }
    }

    fun deleteContactById(id: Int) {
        contactJpa.deleteById(id)
    }
}