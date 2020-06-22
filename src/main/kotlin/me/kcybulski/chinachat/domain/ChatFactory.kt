package me.kcybulski.chinachat.domain

import me.kcybulski.chinachat.domain.interceptors.MessageEventInterceptor
import java.time.Clock
import java.util.UUID.randomUUID

class ChatFactory(
    private val messagesRepository: MessagesRepository,
    private val messageInterceptor: MessageEventInterceptor,
    private val clock: Clock
) {

    fun create(name: String) = Chat(randomUUID().toString(), name, messagesRepository, messageInterceptor, clock)

}
