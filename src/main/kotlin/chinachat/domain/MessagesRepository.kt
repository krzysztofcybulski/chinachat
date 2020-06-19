package chinachat.domain

import io.reactivex.Flowable
import java.util.concurrent.CompletionStage

interface MessagesRepository {

    fun getMessages(chat: Chat): Flowable<MessageEvent>
    fun save(chat: Chat, message: MessageEvent): CompletionStage<MessageEvent>

}
