package me.kcybulski.chinachat.infrastructure

import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.ports.ChatsRepository
import java.util.concurrent.CompletableFuture

class InMemoryChatsRepository : ChatsRepository {
    private val chats: MutableMap<String, Chat> = mutableMapOf()

    override fun save(chat: Chat): CompletableFuture<Chat> {
        chats[chat.id] = chat
        return CompletableFuture.completedFuture(chat)
    }

    override fun list(): CompletableFuture<List<Chat>> = CompletableFuture.completedFuture(chats.values.toList())

    override fun get(key: String): CompletableFuture<Chat> = CompletableFuture.completedFuture(chats[key])

}
