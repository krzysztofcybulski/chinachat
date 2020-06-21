package me.kcybulski.chinachat

import me.kcybulski.chinachat.api.Server
import me.kcybulski.chinachat.domain.*
import me.kcybulski.chinachat.infrastructure.CloudinaryFilesStorage
import me.kcybulski.chinachat.infrastructure.InMemoryChatsRepository
import me.kcybulski.chinachat.infrastructure.InMemoryMessagesRepository
import me.kcybulski.chinachat.plugins.WeatherPlugin

fun main() {

    val messagesRepository: MessagesRepository = InMemoryMessagesRepository()
    val chatsRepository: ChatsRepository = InMemoryChatsRepository()

    val chatFactory = ChatFactory(messagesRepository)
    val chats = ChatsList(chatsRepository)

    chats.add(createGeneralChat(messagesRepository))

    val fileStorage: FilesStorage = CloudinaryFilesStorage()

    Server(chats, chatFactory, fileStorage).run { start() }
}

private fun createGeneralChat(messagesRepository: MessagesRepository) =
    Chat("general", "General", messagesRepository)
        .also { it.addPlugin(WeatherPlugin()) }
