package com.example.researchanything.security.jwt

import com.example.researchanything.security.UserDetail
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import java.util.function.Function
import kotlin.reflect.KFunction

object JwtConfig {
    const val JWT_TOKEN_VALIDITY = 5 * 60 * 60
}

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret:undefined}") val jwtSecret: String
) {
    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Claims::getSubject)
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    fun <T> getClaimFromToken(token: String, claimResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body

    fun generateToken(userDetail: UserDetails): String {
        val claims = mutableMapOf<String, Any>()
        return performGenerateToken(claims, userDetail.username)
    }

    private fun performGenerateToken(claims: MutableMap<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JwtConfig.JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun validateToken(token: String, userDetail: UserDetails): Boolean {
        return getUsernameFromToken(token) == userDetail.username && !isTokenExpired(token)

    }

    private fun isTokenExpired(token: String): Boolean {
        return getExpirationDateFromToken(token).before(Date())
    }
}