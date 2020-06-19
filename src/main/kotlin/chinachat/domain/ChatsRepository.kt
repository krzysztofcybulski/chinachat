package chinachat.domain

import java.util.concurrent.CompletionStage

interface ChatsRepository {

    fun getChats(): CompletionStage<List<Chat>>
    fun save(): CompletionStage<Chat>

}
