package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.model.MessageEvent

class EmojisMessageInterceptor : MessageEventInterceptor {

    override fun intercept(event: MessageEvent): MessageEvent = event.copy(
        content = replaceWithEmoji(event.content)
    )

    private fun replaceWithEmoji(content: String): String = content
        .replace(":)", "\uD83D\uDE42") // 🙂
        .replace(":(", "\uD83D\uDE41") // 🙁
        .replace(":D", "\uD83D\uDE01") // 🙁

}
