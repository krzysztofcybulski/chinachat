package me.kcybulski.chinachat.plugins

import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.model.MessageRequest
import me.kcybulski.chinachat.domain.model.User
import me.kcybulski.chinachat.domain.ports.Plugin

class WeatherPlugin : Plugin {

    private val apiKey = System.getenv("OPEN_WEATHER_API_KEY")

    override fun command() = "weather"

    override fun run(chat: Chat, vararg args: String) {
        chat.sendMessage(
            User("weather"),
            MessageRequest(args[0], null)
        )
    }
}
