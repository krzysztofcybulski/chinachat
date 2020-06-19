package me.kcybulski.chinachat.domain

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

class ChatsList {

    private val chats: MutableMap<String, Chat> = mutableMapOf()

    fun add(chat: Chat): CompletableFuture<Chat> {
        chats[chat.id] = chat
        return completedFuture(chat)
    }

    fun list(): CompletableFuture<List<Chat>> = completedFuture(chats.values.toList())

    fun get(key: String): CompletableFuture<Chat> = completedFuture(chats[key])

}
