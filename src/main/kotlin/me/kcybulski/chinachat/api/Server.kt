package me.kcybulski.chinachat.api

import chinachat.domain.ChatFactory
import chinachat.domain.ChatsList
import chinachat.domain.Security
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ratpack.func.Action
import ratpack.handling.Chain
import ratpack.handling.Context
import ratpack.server.RatpackServer

class Server(
    private val chats: ChatsList,
    private val chatFactory: ChatFactory,
    private val security: Security = Security(),
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
) {

    private val ratpackServer: RatpackServer = RatpackServer.of { server ->
        server
            .serverConfig { config ->
                config.threads(1)
            }
            .registryOf { registry ->
                registry
                    .add(objectMapper)
                    .add(security)
            }
            .handlers(api())
    }

    fun start() = ratpackServer.start()
    fun stop() = ratpackServer.stop()

    private fun api(): Action<Chain> = Action<Chain> { chain ->
        chain
            .all { addCORSHeaders(it) }
            .post("login", LoginApi(security))
            .prefix("chats") { ChatApi(it, chats, chatFactory) }
    }

    private fun addCORSHeaders(ctx: Context) = ctx
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Headers", "*")
        .header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE")
        .next()

}
