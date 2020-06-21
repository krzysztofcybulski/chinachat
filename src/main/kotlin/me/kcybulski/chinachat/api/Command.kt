package me.kcybulski.chinachat.api

import me.kcybulski.chinachat.domain.MessageEvent
import me.kcybulski.chinachat.domain.Plugin

class Command(
    private val plugin: Plugin,
    private val event: MessageEvent
) {

    fun isCommand() = event.content.startsWith("/${plugin.command()} ")

    fun getArguments(): Array<String> = event.content.split(WHITESPACE).drop(1).toTypedArray()

    companion object {

        private val WHITESPACE = "\\s+".toRegex()

    }

}
