package me.kcybulski.chinachat.api

import me.kcybulski.chinachat.api.dto.LoginRequest
import me.kcybulski.chinachat.api.dto.LoginResponse
import me.kcybulski.chinachat.domain.Security
import ratpack.exec.Promise.toPromise
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.jackson.Jackson.json

class LoginApi(
    private val security: Security
) : Handler {

    override fun handle(ctx: Context) = ctx.parse(LoginRequest::class.java)
        .flatMap { toPromise(security.login(it.username)) }
        .map { LoginResponse(it.accessToken) }
        .then { ctx.render(json(it)) }

}
