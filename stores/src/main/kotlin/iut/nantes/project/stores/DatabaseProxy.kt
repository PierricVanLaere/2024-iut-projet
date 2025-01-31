package iut.nantes.project.stores

import iut.nantes.project.stores.controller.AddressDto
import iut.nantes.project.stores.controller.ContactDto
import org.springframework.stereotype.Service

private fun ContactEntity.toDto(): ContactDto =
    ContactDto(id,email,phone, address!!.toDto())

private fun ContactDto.toEntity(): ContactEntity {
    val c = ContactEntity(contactId,email,phone,null)
    c.address = address.toEntity()
    return c
}

private fun AddressEntity.toDto(): AddressDto =
    AddressDto(id,street, city, postalCode)

private fun AddressDto.toEntity(): AddressEntity =
    AddressEntity(addressId,street,city,postalCode)

@Service
class DatabaseProxy(private val contactJpa: ContactJpa) {
    fun saveContact(contactDto: ContactDto): ContactDto {
//        val contactEntity = contactDto.toEntity()
//        val savedContact = contactJpa.save(contactEntity)
//        return savedContact.toDto()

        val existingContact = contactDto.contactId?.let { findContactById(it) }
        return if (existingContact != null) {
            existingContact.email = contactDto.email
            existingContact.phone = contactDto.phone
            existingContact.address = contactDto.address
            contactJpa.save(existingContact.toEntity()).toDto()
        } else {
            val contactEntity = contactDto.toEntity()
            contactJpa.save(contactEntity).toDto()
        }
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