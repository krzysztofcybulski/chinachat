package me.kcybulski.chinachat.domain

import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import me.kcybulski.chinachat.api.Command
import java.util.concurrent.CompletableFuture.completedFuture
import java.util.concurrent.CompletionStage

class Chat(val id: String, val name: String, private val messagesRepository: MessagesRepository) {

    private val chatSubject = PublishSubject.create<Event>()
    private val users: MutableSet<User> = mutableSetOf()

    private val whitespaceRegex = "\\s+".toRegex()

    fun join(user: User): CompletionStage<Flowable<Event>> {
        if (users.add(user)) {
            sendEvent(UserJoinedEvent(user))
        }
        return completedFuture(chatStream())
    }

    fun leave(user: User): CompletionStage<Void> {
        if (users.remove(user)) {
            sendEvent(UserLeftEvent(user))
        }
        return completedFuture(null)
    }

    fun sendMessage(author: User, messageRequest: MessageRequest) {
        val message = messageRequest.toEvent(author)
        if (message.hasContent() && isNotCommand(message)) {
            messagesRepository.save(this, message)
                .thenAccept { sendEvent(it) }
        }
    }

    fun startWriting(user: User) = sendEvent(UserWritingEvent(user))

    fun getMessages(): Flowable<MessageEvent> = messagesRepository.getMessages(this)

    fun addPlugin(plugin: Plugin) {
        chatStream()
            .filter { it is MessageEvent }
            .map { Command(plugin, it as MessageEvent) }
            .filter { it.isCommand() }
            .subscribe { plugin.run(this, *it.getArguments()) }
    }

    private fun sendEvent(event: Event) = chatSubject.onNext(event)

    private fun chatStream() = chatSubject.toFlowable(LATEST)

    private fun isNotCommand(event: MessageEvent) = !event.content.startsWith("/")

}
