package me.kcybulski.chinachat

import me.kcybulski.chinachat.api.Server
import me.kcybulski.chinachat.domain.*
import me.kcybulski.chinachat.domain.interceptors.CompositeInterceptor
import me.kcybulski.chinachat.domain.interceptors.MessageEventInterceptor
import me.kcybulski.chinachat.infrastructure.CloudinaryFilesStorage
import me.kcybulski.chinachat.infrastructure.InMemoryChatsRepository
import me.kcybulski.chinachat.infrastructure.InMemoryMessagesRepository
import me.kcybulski.chinachat.plugins.WeatherPlugin
import java.time.Clock

fun main() {
    val clock = Clock.systemDefaultZone()

    val messagesRepository: MessagesRepository = InMemoryMessagesRepository()
    val chatsRepository: ChatsRepository = InMemoryChatsRepository()

    val messageInterceptor: MessageEventInterceptor = CompositeInterceptor()

    val chatFactory = ChatFactory(messagesRepository, messageInterceptor, clock)
    val chats = ChatsList(chatsRepository)

    chats.add(createGeneralChat(messagesRepository, messageInterceptor, clock))

    val fileStorage: FilesStorage = CloudinaryFilesStorage()

    Server(chats, chatFactory, fileStorage).run { start() }
}

private fun createGeneralChat(
    messagesRepository: MessagesRepository,
    messageInterceptor: MessageEventInterceptor,
    clock: Clock
) =
    Chat("general", "General", messagesRepository, messageInterceptor, clock)
        .also { it.addPlugin(WeatherPlugin()) }
