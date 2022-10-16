package com.api.heys.entity

import org.springframework.data.domain.Persistable
import javax.persistence.*
import kotlin.jvm.Transient

@MappedSuperclass
abstract class BaseIdentity(existId: Long? = null): Persistable<Long> {

// UUID ID GENERATOR
// abstract class BaseIdentity(existId: UUID? = null): Persistable<UUID> {
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
//    private val id: UUID = UUID.randomUUID()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private val id: Long = 0

    @Transient
    private var persisted: Boolean = existId != null

    override fun getId(): Long = id

    override fun isNew(): Boolean = !persisted

    override fun hashCode(): Int = id.hashCode()

    override fun equals(right: Any?): Boolean {
        return when {
            this === right -> true
            right == null -> false
            right !is BaseIdentityDate -> false
            else -> getId() == right.getId()
        }
    }
}