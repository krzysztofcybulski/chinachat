package me.kcybulski.chinachat.domain

import java.util.UUID.randomUUID

class ChatFactory(private val messagesRepository: MessagesRepository) {

    fun create(name: String) = Chat(randomUUID().toString(), name, messagesRepository)

}
