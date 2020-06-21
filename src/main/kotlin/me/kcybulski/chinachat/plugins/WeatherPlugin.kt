package me.kcybulski.chinachat.plugins

import me.kcybulski.chinachat.domain.Chat
import me.kcybulski.chinachat.domain.MessageRequest
import me.kcybulski.chinachat.domain.Plugin
import me.kcybulski.chinachat.domain.User

class WeatherPlugin : Plugin {

    private val apiKey = System.getenv("OPEN_WEATHER_API_KEY")

    override fun command() = "weather"

    override fun run(chat: Chat, vararg args: String) {
        chat.sendMessage(User("weather"), MessageRequest(args[0]))
    }
}
