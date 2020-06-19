package me.kcybulski.chinachat.domain

interface Plugin {

    fun command(): String
    fun run(chat: Chat, vararg args: String)

}
