package me.theseems.alicegram.bot

import com.github.kotlintelegrambot.Bot
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BotConfig {
    @Autowired
    private lateinit var bot: Bot

    @PostConstruct
    fun poll() = bot.startPolling()
}
