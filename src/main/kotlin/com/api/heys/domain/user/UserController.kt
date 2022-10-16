package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import kotlin.jvm.Throws

@RestController
@RequestMapping("user")
class UserController(
        @Autowired private val userService: UserService
) {
    @PostMapping("signUp")
    fun signUp(@Valid @RequestBody body: SignUpData): ResponseEntity<User> {
        val user: User? = userService.signUp(body)
        if (user != null) {
            return ResponseEntity.ok(user)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}