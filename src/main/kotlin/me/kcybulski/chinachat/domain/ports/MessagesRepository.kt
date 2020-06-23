package me.kcybulski.chinachat.domain.ports

import io.reactivex.Flowable
import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.model.MessageEvent
import java.util.concurrent.CompletionStage

interface MessagesRepository {

    fun getMessages(chat: Chat): Flowable<MessageEvent>
    fun save(chat: Chat, message: MessageEvent): CompletionStage<MessageEvent>

}
