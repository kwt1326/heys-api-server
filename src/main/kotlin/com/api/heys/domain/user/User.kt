package com.api.heys.domain.user

import com.api.heys.domain.common.entity.BaseIdentityDateEntity
import lombok.Getter
import lombok.Setter
import org.springframework.data.domain.Persistable
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Table

@Getter
@Setter
@Entity
@Table(name = "User")
class User: BaseIdentityDateEntity(), Persistable<UUID> {

}