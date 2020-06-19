package me.kcybulski.chinachat.plugins

import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.MessageRequest
import me.kcybulski.chinachat.domain.Plugin
import me.kcybulski.chinachat.domain.User

class WeatherPlugin : Plugin {

    override fun command() = "weather"

    override fun run(chat: Chat, vararg args: String) {
        chat.sendMessage(User("weather"), MessageRequest(args[0]))
    }
}
