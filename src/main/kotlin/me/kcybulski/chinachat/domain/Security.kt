package me.kcybulski.chinachat.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC256
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture


class Security(secret: String = System.getenv("APP_SECRET")) {

    private val algorithm = HMAC256(secret)
    private val verifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build()

    fun login(username: String): CompletableFuture<AccessToken> {
        val token = JWT.create()
            .withIssuer(ISSUER)
            .withClaim("name", username)
            .sign(algorithm)
        return completedFuture(AccessToken(token))
    }

    fun getUser(accessToken: String): User? = try {
        val jwt = verifier.verify(accessToken)
        User(jwt.claims["name"]?.asString() ?: "")
    } catch (e: JWTVerificationException) {
        null
    }

    companion object {

        private const val ISSUER = "chinachat"

    }

    data class AccessToken(val accessToken: String)
}
