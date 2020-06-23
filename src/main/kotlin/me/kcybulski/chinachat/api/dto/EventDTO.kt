package me.kcybulski.chinachat.api.dto

import me.kcybulski.chinachat.api.dto.EventType.*
import me.kcybulski.chinachat.domain.model.*

data class EventDTO<T>(val type: EventType, val payload: T) {

    companion object {

        fun fromEvent(event: Event): EventDTO<Any> = when (event) {
            is MessageEvent -> EventDTO(
                MESSAGE,
                MessagePayload(event.content, event.mediaUrl, event.author.name, event.time.toString())
            )
            is UserJoinedEvent -> EventDTO(
                USER_JOINED,
                UserEventPayload(event.user.name)
            )
            is UserLeftEvent -> EventDTO(
                USER_LEFT,
                UserEventPayload(event.user.name)
            )
            is UserWritingEvent -> EventDTO(
                USER_WRITING,
                UserEventPayload(event.user.name)
            )
            else -> throw RuntimeException("")
        }

    }

}

data class MessagePayload(val content: String, val mediaUrl: String, val author: String, val time: String)
data class UserEventPayload(val user: String)

enum class EventType {
    MESSAGE, USER_JOINED, USER_LEFT, USER_WRITING
}
