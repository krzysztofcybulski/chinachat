package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.MessageEvent

class NoCommandInterceptor : MessageEventInterceptor {

    override fun intercept(event: MessageEvent): MessageEvent? = event.takeUnless { it.content.startsWith("/") }

}
