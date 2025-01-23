package iut.nantes.project.stores

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

interface ContactJpa : JpaRepository<ContactEntity, Int> {}

@Entity
@Table(name = "CONTACTS")
data class ContactEntity (
    @Id @GeneratedValue
    @Column(name = "contact_id")
    val id: Int?,
    val email: String,
    val phone: String,
    @OneToMany(cascade = [CascadeType.ALL])
    var address: List<AddressEntity> = listOf(),
)