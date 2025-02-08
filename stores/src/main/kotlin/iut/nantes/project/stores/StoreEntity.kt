package iut.nantes.project.stores

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.jpa.repository.Query


interface StoreJpa : JpaRepository<StoreEntity, Int> {}

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Int> {
    @Modifying
    @Query("DELETE FROM ProductEntity p WHERE p.store.id = :storeId")
    fun deleteByStoreId(storeId: Int)
}


@Entity
@Table(name = "STORES", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "Le nom ne peut pas être vide.")
    @field:Size(min = 3, max = 30, message = "Le nom doit contenir entre 3 et 30 caractères.")
    var name: String,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "contact_id", nullable = false)
    @field:NotNull(message = "Le contact ne peut pas être nul.")
    var contact: ContactEntity,

    @OneToMany(mappedBy = "store", cascade = [CascadeType.ALL], orphanRemoval = true)
    val products: Set<ProductEntity> = mutableSetOf()
)

@Entity
@Table(name = "PRODUCTS")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    @field:NotBlank(message = "Le nom du produit ne peut pas être vide.")
    val name: String,

    @Column(nullable = false)
    val quantity: Int,

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false) // Clé étrangère vers StoreEntity
    val store: StoreEntity
)
