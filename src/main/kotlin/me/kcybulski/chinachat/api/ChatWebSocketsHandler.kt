package me.kcybulski.chinachat.api

import chinachat.api.dto.Action
import chinachat.api.dto.EventDTO
import chinachat.api.dto.MessageAction
import chinachat.api.dto.WritingAction
import chinachat.domain.Chat
import chinachat.domain.Event
import chinachat.domain.MessageRequest
import chinachat.domain.User
import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.handling.Chain
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketHandler
import ratpack.websocket.WebSocketMessage

class ChatWebSocketsHandler(
    chain: Chain,
    private val user: User,
    private val chat: Chat
) : WebSocketHandler<String> {

    private val objectMapper = chain.registry.get(ObjectMapper::class.java)
    private val clients: MutableMap<String, WebSocket> = mutableMapOf()

    override fun onOpen(webSocket: WebSocket): String? {
        chat.join(user)
            .thenAccept { stream ->
                stream.subscribe { event ->
                    onEvent(event, webSocket)
                }
            }
            .thenAccept { clients[user.name] = webSocket }
            .thenRun { retransmitMessages(webSocket) }
        return null
    }

    override fun onMessage(frame: WebSocketMessage<String>) {
        when (val action = getAction(frame)) {
            is MessageAction -> chat.sendMessage(user, MessageRequest(action.content))
            is WritingAction -> chat.startWriting(user)
        }
    }

    override fun onClose(close: WebSocketClose<String>) {
        chat.leave(user)
    }

    private fun retransmitMessages(webSocket: WebSocket) {
        chat.getMessages().subscribe { message -> onEvent(message, webSocket) }
    }

    private fun onEvent(event: Event, webSocket: WebSocket) {
        val body = objectMapper.writeValueAsString(EventDTO.fromEvent(event))
        webSocket.send(body)
    }

    private fun getAction(frame: WebSocketMessage<String>): Action =
        objectMapper.readValue(frame.text, Action::class.java)

}
