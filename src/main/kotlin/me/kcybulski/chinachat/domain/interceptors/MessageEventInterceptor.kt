package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.MessageEvent

interface MessageEventInterceptor {

    fun intercept(event: MessageEvent): MessageEvent?

}
