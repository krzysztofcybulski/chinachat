package me.kcybulski.chinachat

import chinachat.api.Server
import chinachat.domain.Chat
import chinachat.domain.ChatFactory
import chinachat.domain.ChatsList
import chinachat.domain.MessagesRepository
import chinachat.infrastructure.InMemoryMessagesRepository
import chinachat.plugins.WeatherPlugin

fun main() {

    val messagesRepository: MessagesRepository = InMemoryMessagesRepository()
    val chatFactory = ChatFactory(messagesRepository)

    val chats = ChatsList()
    chats.add(createGeneralChat(messagesRepository))

    Server(chats, chatFactory).run { start() }
}

private fun createGeneralChat(messagesRepository: MessagesRepository) = Chat("general", "General", messagesRepository)
    .also { it.addPlugin(WeatherPlugin()) }
