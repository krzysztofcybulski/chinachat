package me.kcybulski.chinachat.domain

import java.util.concurrent.CompletableFuture

interface ChatsRepository {

    fun get(key: String): CompletableFuture<Chat>
    fun list(): CompletableFuture<List<Chat>>
    fun save(chat: Chat): CompletableFuture<Chat>

}
