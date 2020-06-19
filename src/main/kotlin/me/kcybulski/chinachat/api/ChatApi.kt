package me.kcybulski.chinachat.api

import io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION
import me.kcybulski.chinachat.api.dto.ChatListResponse.Companion.fromChats
import me.kcybulski.chinachat.api.dto.ChatRequest
import me.kcybulski.chinachat.api.dto.ChatResponse.Companion.fromChat
import me.kcybulski.chinachat.domain.ChatFactory
import me.kcybulski.chinachat.domain.ChatsList
import me.kcybulski.chinachat.domain.Security
import me.kcybulski.chinachat.domain.User
import ratpack.exec.Promise.toPromise
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.jackson.Jackson.json
import ratpack.websocket.WebSockets.websocket

class ChatApi(
    private val chain: Chain,
    private val chats: ChatsList,
    private val chatFactory: ChatFactory
) {

    val security = chain.registry.get(Security::class.java)

    init {
        chain
            .get(":key") { ctx ->
                secured(ctx) { user ->
                    val key = ctx.pathTokens["key"] ?: ""
                    toPromise(
                        chats.get(key).thenApply { chat -> websocket(ctx, ChatWebSocketsHandler(chain, user, chat)) })
                }
            }
            .all { ctx ->
                ctx.byMethod { methods ->
                    methods.post(CreateChat())
                    methods.get(ListChats())
                }
            }
    }

    inner class CreateChat : Handler {

        override fun handle(ctx: Context) =
            secured(ctx) {
                ctx
                    .parse(ChatRequest::class.java)
                    .map { chatFactory.create(it.name) }
                    .flatMap { toPromise(chats.add(it)) }
                    .then { ctx.render(json(fromChat(it))) }
            }

    }

    inner class ListChats : Handler {

        override fun handle(ctx: Context) =
            secured(ctx) {
                toPromise(chats.list())
                    .then { ctx.render(json(fromChats(it))) }
            }

    }

    private fun secured(ctx: Context, func: (User) -> Unit) = getAccessToken(ctx)
        ?.let { security.getUser(it) }
        ?.run(func)
        ?: throw RuntimeException()

    private fun getAccessToken(ctx: Context) =
        ctx.request.headers[AUTHORIZATION]
            ?.removePrefix("Bearer ")
            ?: ctx.request.queryParams["token"]

}
