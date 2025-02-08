package iut.nantes.project.gateway

import jakarta.persistence.*


@Entity
@Table(name = "USERS")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val login: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val isAdmin: Boolean
)
