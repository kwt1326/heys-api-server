package com.api.heys.utils

import com.api.heys.security.domain.CustomUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.Getter
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.crypto.SecretKey

open class JwtUtil(
        expireHour: Int
) {
    @Value("\${custom.secret}")
    lateinit var SECRET: String

    private val expireTime: Long = 1000L * 60 * 60 * expireHour;

    private fun getSecretKey(): SecretKey {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET))
    }

    private fun subStringToken(origin: String): String {
        return if (origin.contains("Bearer", ignoreCase = true)) origin.substring(7) else origin
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(subStringToken(token))
                .body
    }

    open fun extractUsername(token: String): String {
        return extractClaims(token).subject
    }

    open fun extractExpiration(token: String): Date {
        return extractClaims(token).expiration
    }

    open fun extractAuthorities(token: String): List<SimpleGrantedAuthority> {
        return (extractClaims(token)["roles"] as List<*>).map {
            SimpleGrantedAuthority(it as String)
        }
    }

    open fun isExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    open fun makeUserDetail(token: String): UserDetails {
        val prefixRemovedToken: String = subStringToken(token)
        return CustomUser(extractUsername(prefixRemovedToken), "", true, extractAuthorities(prefixRemovedToken))
    }

    open fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isExpired(token)
    }

    open fun createJwt(username: String, role: List<String>): String? {
        val claims: Map<String, Any> = mapOf("roles" to role)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256).compact()
    }
}