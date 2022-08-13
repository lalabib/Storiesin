package com.project.lalabib.storiesin.data.response

data class LoginResponse (
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult? = null
)

data class LoginResult (
    val name: String,
    val token: String
)
