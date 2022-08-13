package com.project.lalabib.storiesin.data.response

data class LoginResponse (
    val loginResult: LoginResult? = null,
    val error: Boolean,
    val message: String,
)

data class LoginResult (
    val name: String,
    val token: String
)
