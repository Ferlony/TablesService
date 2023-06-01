package com.tables.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

object JWTConfig {
    const val audience = "http://0.0.0.0/login"
    private const val issuer = "ktor.io"
    private const val secret = "secret"
    private val algorithm = Algorithm.HMAC512(secret)



    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun makeToken(user: UserRegister): String = JWT
        .create()
        .withIssuer(issuer)
        .withClaim("email", user.email)
        .withClaim("username", user.username)
        .withClaim("password", user.password)
        .withClaim("role", user.role)
        .withAudience(audience)
        .sign(algorithm)
}
