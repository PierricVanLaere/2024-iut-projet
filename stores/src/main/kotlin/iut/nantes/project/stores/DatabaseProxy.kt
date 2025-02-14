package iut.nantes.project.stores

import iut.nantes.project.stores.controller.AddressDto
import iut.nantes.project.stores.controller.ContactDto
import iut.nantes.project.stores.controller.StoreDto
import iut.nantes.project.stores.controller.StoreUpdateRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

fun ContactEntity.toDto(): ContactDto =
    ContactDto(id,email,phone, address!!.toDto())

fun ContactDto.toEntity(): ContactEntity {
    val c = ContactEntity(contactId,email,phone,null)
    c.address = address.toEntity()
    return c
}

private fun AddressEntity.toDto(): AddressDto =
    AddressDto(id,street, city, postalCode)

private fun AddressDto.toEntity(): AddressEntity =
    AddressEntity(addressId,street,city,postalCode)

fun StoreEntity.toDto(): StoreDto =
    StoreDto(id,name,contact,products)

fun StoreDto.toEntity(): StoreEntity =
    StoreEntity(id,name,contact,products)


@Service
class DatabaseProxy(
    private val contactJpa: ContactJpa,
    private val storeJpa: StoreJpa,
    private val productRepository: ProductRepository,
) {
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

    fun saveStore(storeRequest: StoreDto): StoreDto {
        val storeEntity = storeRequest.toEntity()
        return storeJpa.save(storeEntity).toDto()
    }

    fun findAllStores(): List<StoreDto> {
        return storeJpa.findAll().map { it.toDto() }
    }

    fun getStoreById(id: Int): StoreEntity {
        return storeJpa.findById(id).orElseThrow { NoSuchElementException("Store not found") }
    }

    fun updateStore(id: Int, storeUpdateRequest: StoreUpdateRequest): StoreEntity {
        val store = storeJpa.findById(id).orElseThrow { NoSuchElementException("Store not found") }
        val contact = contactJpa.findById(storeUpdateRequest.contactId)
            .orElseThrow { IllegalArgumentException("Invalid contact ID") }

        store.name = storeUpdateRequest.name
        store.contact = contact

        return storeJpa.save(store)
    }

    @Transactional
    fun deleteStore(id: Int) {
        val store = storeJpa.findById(id).orElseThrow { NoSuchElementException("Store not found") }

        productRepository.deleteByStoreId(id)

        storeJpa.delete(store)
    }
}


