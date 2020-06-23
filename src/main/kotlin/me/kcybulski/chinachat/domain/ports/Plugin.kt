package me.kcybulski.chinachat.domain.ports

import me.kcybulski.chinachat.domain.Chat

interface Plugin {

    fun command(): String
    fun run(chat: Chat, vararg args: String)

}
