package me.kcybulski.chinachat

import me.kcybulski.chinachat.api.Server
import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.ChatFactory
import me.kcybulski.chinachat.domain.ChatsList
import me.kcybulski.chinachat.domain.MessagesRepository
import me.kcybulski.chinachat.infrastructure.InMemoryMessagesRepository
import me.kcybulski.chinachat.plugins.WeatherPlugin

fun main() {

    val messagesRepository: MessagesRepository = InMemoryMessagesRepository()
    val chatFactory = ChatFactory(messagesRepository)

    val chats = ChatsList()
    chats.add(createGeneralChat(messagesRepository))

    Server(chats, chatFactory).run { start() }
}

private fun createGeneralChat(messagesRepository: MessagesRepository) =
    Chat("general", "General", messagesRepository)
        .also { it.addPlugin(WeatherPlugin()) }
