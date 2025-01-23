package iut.nantes.project.stores

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ADRESS")
data class AddressEntity(
    @Id @GeneratedValue
    @Column(name = "adress_id")
    val street: String,
    val city: String,
    val postalCode: String,
)
