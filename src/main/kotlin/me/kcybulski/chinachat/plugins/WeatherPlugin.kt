package me.kcybulski.chinachat.plugins

import chinachat.domain.Chat
import chinachat.domain.MessageRequest
import chinachat.domain.Plugin
import chinachat.domain.User

class WeatherPlugin : Plugin {

    override fun command() = "weather"

    override fun run(chat: Chat, vararg args: String) {
        chat.sendMessage(User("weather"), MessageRequest(args[0]))
    }
}
