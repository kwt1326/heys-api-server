package com.api.heys.entity

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*
import kotlin.jvm.Transient

@MappedSuperclass
abstract class BaseIdentityDate(existId: UUID? = null): Persistable<UUID> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private val id: UUID = UUID.randomUUID()

    @JsonFormat(pattern = "yyyy-mm-dd")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()

    @JsonFormat(pattern = "yyyy-mm-dd")
    @CreationTimestamp
    @Column(name = "updated_at", updatable = false)
    var updatedAt: LocalDateTime? = createdAt

    @JsonFormat(pattern = "yyyy-mm-dd")
    @CreationTimestamp
    @Column(name = "removed_at", updatable = false)
    var removedAt: LocalDateTime? = null

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