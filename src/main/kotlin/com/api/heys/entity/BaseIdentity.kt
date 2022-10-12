package com.api.heys.entity

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.domain.Persistable
import java.util.*
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseIdentity(existId: UUID? = null): Persistable<UUID> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private val id: UUID = UUID.randomUUID()

    @Transient
    private var persisted: Boolean = existId != null

    override fun getId(): UUID = id

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