package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.model.MessageEvent

class NoContentInterceptor : MessageEventInterceptor {

    override fun intercept(event: MessageEvent): MessageEvent? = event.takeIf { it.hasContent() }

}
