package me.kcybulski.chinachat.domain.model

import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalDateTime.now

interface Event {
    val time: LocalDateTime
}

data class MessageEvent(
    val content: String = "",
    val mediaUrl: String = "",
    val author: User,
    override val time: LocalDateTime
) : Event {

    fun hasContent() = content.isNotBlank() || mediaUrl.isNotBlank()

}

data class UserJoinedEvent(
    val user: User,
    override val time: LocalDateTime = now()
) : Event

data class UserLeftEvent(
    val user: User,
    override val time: LocalDateTime = now()
) : Event

data class UserWritingEvent(
    val user: User,
    override val time: LocalDateTime = now()
) : Event

data class MessageRequest(val content: String?, val mediaUrl: String?) {

    fun toEvent(author: User, clock: Clock) =
        MessageEvent(
            content ?: "",
            mediaUrl ?: "",
            author,
            now(clock)
        )

}
