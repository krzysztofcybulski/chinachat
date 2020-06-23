package me.kcybulski.chinachat.domain

import me.kcybulski.chinachat.domain.ports.ChatsRepository
import java.util.concurrent.CompletableFuture

class ChatsList(private val chatsRepository: ChatsRepository) {

    fun add(chat: Chat): CompletableFuture<Chat> = chatsRepository.save(chat)

    fun list(): CompletableFuture<List<Chat>> = chatsRepository.list()

    fun get(key: String): CompletableFuture<Chat> = chatsRepository.get(key)

}
