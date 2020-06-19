package chinachat.api.dto

import chinachat.domain.Chat

data class ChatListResponse(val chats: List<ChatResponse>) {

    companion object {

        fun fromChats(chats: List<Chat>) = ChatListResponse(chats.map {
            ChatResponse.fromChat(it)
        })

    }

}

data class ChatResponse(val id: String, val name: String) {

    companion object {

        fun fromChat(chat: Chat) = ChatResponse(chat.id, chat.name)

    }

}


data class ChatRequest(val name: String)
