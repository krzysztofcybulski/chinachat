package me.kcybulski.chinachat.domain.interceptors

import me.kcybulski.chinachat.domain.model.MessageEvent

interface MessageEventInterceptor {

    fun intercept(event: MessageEvent): MessageEvent?

}
