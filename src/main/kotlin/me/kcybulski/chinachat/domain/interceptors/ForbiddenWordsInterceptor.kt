package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.MessageEvent


class ForbiddenWordsInterceptor : MessageEventInterceptor {

    override fun intercept(event: MessageEvent): MessageEvent = event.copy(
        content = replaceBadWords(event.content)
    )

    private fun replaceBadWords(content: String): String = content
        .replace("fuck", "fu*k")

}
