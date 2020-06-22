package me.kcybulski.chinachat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.kcybulski.chinachat.api.Server
import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.ChatFactory
import me.kcybulski.chinachat.domain.ChatsList
import me.kcybulski.chinachat.domain.Security
import me.kcybulski.chinachat.domain.interceptors.CompositeInterceptor
import me.kcybulski.chinachat.infrastructure.InMemoryChatsRepository
import me.kcybulski.chinachat.infrastructure.InMemoryFilesStorage
import me.kcybulski.chinachat.infrastructure.InMemoryMessagesRepository
import me.kcybulski.chinachat.utils.TestServer
import me.kcybulski.chinachat.utils.TestUser
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import static java.time.LocalDateTime.now

class ChatSpec extends Specification {

    Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
    ChatFactory chatFactory = new ChatFactory(new InMemoryMessagesRepository(), new CompositeInterceptor(), clock)
    Chat generalChat = chatFactory.create('General')

    def 'should send message'() {
        given:
            def server = new TestServer(server())
            def bob = new TestUser(server, 'Bob')
            def anna = new TestUser(server, 'Anna')

        when:
            bob.join(generalChat.id)
            anna.join(generalChat.id)
        and:
            anna.sendMessage(messageAction('Hello there! 👋'))

        then:
            bob.getLastMessage() == joinedEvent('Anna')
        and:
            bob.getLastMessage() == messageEvent('Hello there! 👋', 'Anna')

        cleanup:
            server.stop()
    }

    def 'should convert emoji'() {
        given:
            def server = new TestServer(server())
            def bob = new TestUser(server, 'Bob')

        when:
            bob.join(generalChat.id)
        and:
            bob.sendMessage(messageAction(':) :D :('))

        then:
            bob.getLastMessage() == messageEvent('🙂 😁 🙁', 'Bob')

        cleanup:
            server.stop()
    }

    def 'should create and list chats'() {
        given:
            def server = new TestServer(server())
            def bob = new TestUser(server, 'Bob')

        when:
            bob.post('chats', [name: 'Bobs chat'])
        and:
            def chats = bob.get('chats')['chats']

        then:
            chats.collect { it['name'] } == ['General', 'Bobs chat']

        cleanup:
            server.stop()
    }

    private Map<String, Object> messageAction(String message) {
        [
                action : 'message',
                content: message
        ]
    }

    private Map<String, Object> messageEvent(String message, String author) {
        [
                type   : 'MESSAGE',
                payload: [
                        content : message,
                        author  : author,
                        mediaUrl: '',
                        time    : now(clock).toString()
                ]
        ]
    }

    private Map<String, Object> joinedEvent(String user) {
        [
                type   : 'USER_JOINED',
                payload: [
                        user: user
                ]
        ]
    }

    private Server server() {
        def chatRepository = new InMemoryChatsRepository()
        chatRepository.save(generalChat)
        new Server(
                new ChatsList(chatRepository),
                new ChatFactory(new InMemoryMessagesRepository(), new CompositeInterceptor(), clock),
                new InMemoryFilesStorage(),
                new Security("secret"),
                new ObjectMapper().registerModule(new KotlinModule())
        )
    }

}
