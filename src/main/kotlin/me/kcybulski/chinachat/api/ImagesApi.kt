package me.kcybulski.chinachat.api

import io.netty.handler.codec.http.HttpHeaderNames
import me.kcybulski.chinachat.api.dto.ImageResponse
import me.kcybulski.chinachat.domain.Security
import me.kcybulski.chinachat.domain.model.User
import me.kcybulski.chinachat.domain.ports.FilesStorage
import ratpack.exec.Promise.toPromise
import ratpack.form.Form
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.jackson.Jackson.json

class ImagesApi(
    private val security: Security,
    private val filesStorage: FilesStorage
) : Handler {

    override fun handle(ctx: Context) = secured(ctx) {
        ctx.parse(Form::class.java)
            .flatMap { form -> toPromise(filesStorage.upload(form.file("file").bytes)) }
            .then { ctx.render(json(ImageResponse.fromFile(it))) }
    }

    private fun secured(ctx: Context, func: (User) -> Unit) = getAccessToken(ctx)
        ?.let { security.getUser(it) }
        ?.run(func)
        ?: ctx.clientError(401)

    private fun getAccessToken(ctx: Context) =
        ctx.request.headers[HttpHeaderNames.AUTHORIZATION]
            ?.removePrefix("Bearer ")
            ?: ctx.request.queryParams["token"]

}
