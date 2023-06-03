package com.tables.plugins

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable


@Serializable
data class UserRegister(
    val username: String,
    val email: String,
    val password: String,
    val role: String = "user"
): Principal


@Serializable
data class UserLogin(
    val username: String,
    val password: String
): Principal

@Serializable
data class UserRespond(
    val username: String,
    val accessToken: String,
    val id: String,
    val email: String,
    val role: String
): Principal

data class TableWithMarks(
    val id: String,
    val topic: String,
    val description: String,
    val userstatus: String,
    val checked: String,
    val tabletittle: String
): Principal

