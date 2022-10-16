package com.api.heys.entity

import lombok.Getter
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Getter
@Entity
@Table(name = "interest")
class Interest(
        name: String,
): BaseIdentity(), Serializable {
        @Column(name = "name")
        val name: String = name
}