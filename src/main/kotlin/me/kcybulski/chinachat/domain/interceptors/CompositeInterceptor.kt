package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.model.MessageEvent

class CompositeInterceptor : MessageEventInterceptor {

    private val interceptors = listOf(
        NoContentInterceptor(),
        NoCommandInterceptor(),
        EmojisMessageInterceptor(),
        ForbiddenWordsInterceptor()
    )

    override fun intercept(event: MessageEvent): MessageEvent? = interceptors
        .foldRight(event) { interceptor, acc: MessageEvent? -> acc?.let { interceptor.intercept(it) } }

}
