package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("user")
class UserController(
        @Autowired private val userService: UserService
) {
    @PostMapping("signUp")
    fun signUp(@Valid @RequestBody body: SignUpData): ResponseEntity<Users> {
        val users: Users? = userService.signUp(body, listOf("COMMON_USER"))
        if (users != null) {
            return ResponseEntity.ok(users)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("signUp/admin")
    fun signUpAdmin(@Valid @RequestBody body: SignUpData): ResponseEntity<Users> {
        val users: Users? = userService.signUp(body, listOf("ADMIN_USER", "COMMON_USER"))
        if (users != null) {
            return ResponseEntity.ok(users)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}