package me.kcybulski.chinachat.infrastructure

import chinachat.domain.Chat
import chinachat.domain.MessageEvent
import chinachat.domain.MessagesRepository
import io.reactivex.Flowable
import java.util.concurrent.CompletableFuture.completedFuture
import java.util.concurrent.CompletionStage

class InMemoryMessagesRepository : MessagesRepository {

    private val messages: MutableMap<Chat, MutableList<MessageEvent>> = mutableMapOf()

    override fun getMessages(chat: Chat): Flowable<MessageEvent> = Flowable.fromIterable(messages[chat])

    override fun save(chat: Chat, message: MessageEvent): CompletionStage<MessageEvent> {
        messages.putIfAbsent(chat, mutableListOf())
        messages[chat]?.add(message)
        return completedFuture(message)
    }
}
