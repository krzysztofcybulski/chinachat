package me.kcybulski.chinachat.api.dto

data class LoginRequest(val username: String)
data class LoginResponse(val accessToken: String)
