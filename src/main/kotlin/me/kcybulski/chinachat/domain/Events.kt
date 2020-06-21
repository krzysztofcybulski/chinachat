package me.kcybulski.chinachat.domain

import java.time.LocalDateTime
import java.time.LocalDateTime.now

interface Event {
    val time: LocalDateTime
}

data class MessageEvent(
    val content: String = "",
    val mediaUrl: String = "",
    val author: User,
    override val time: LocalDateTime = now()
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

    fun toEvent(author: User) =
        MessageEvent(
            content = content
                ?.replace(":)", "\uD83D\uDE42")
                ?.replace(":(", "\uD83D\uDE41")
                ?.replace(":D", "\uD83D\uDE01")
                ?: "", mediaUrl = mediaUrl ?: "", author = author
        )

}
