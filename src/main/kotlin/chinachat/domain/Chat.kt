package chinachat.domain

import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
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

    fun sendMessage(user: User, messageRequest: MessageRequest) {
        val message = MessageEvent(messageRequest.content, user)
        if (message.content.isNotBlank()) {
            messagesRepository.save(this, message)
                .thenAccept { sendEvent(it) }
        }
    }

    fun getMessages(): Flowable<MessageEvent> = messagesRepository.getMessages(this)

    fun startWriting(user: User) {
        sendEvent(UserWritingEvent(user))
    }

    fun addPlugin(plugin: Plugin) {
        chatStream()
            .filter { it is MessageEvent && it.content.startsWith("/${plugin.command()} ") }
            .subscribe { plugin.run(this, *getArguments(it)) }
    }

    private fun getArguments(it: Event?) =
        (it as MessageEvent).content.split(whitespaceRegex).drop(1).toTypedArray()

    private fun sendEvent(event: Event) = chatSubject.onNext(event)

    private fun chatStream() = chatSubject.toFlowable(LATEST)

}
