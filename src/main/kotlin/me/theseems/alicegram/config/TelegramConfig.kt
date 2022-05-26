package me.theseems.alicegram.config

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.logging.LogLevel
import me.theseems.alicegram.service.user.AlicegramUserService
import me.theseems.alicegram.service.verify.VerificationService
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramConfig {
    @Value("\${alicegram.botAccessToken}")
    private lateinit var botAccessToken: String

    @Autowired
    private lateinit var verificationService: VerificationService

    @Autowired
    private lateinit var userService: AlicegramUserService

    @Bean
    fun telegramBot(): Bot {
        return bot {
            logLevel = LogLevel.All()
            token = botAccessToken

            dispatch {
                command("start") {
                    handleStartCommand()
                }

                command("unlink") {
                    handleUnlinkCommand()
                }
            }
        }
    }

    fun CommandHandlerEnvironment.handleUnlinkCommand() {
        val chatId = message.chat.id
        val userId = userService.findByTelegramChatId(chatId)?.id
        if (userId == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = """
                    Ваш аккаунт не связан с Яндекс.
                    Привязать можно в навыке:
                     
                    https://dialogs.yandex.ru/share?key=aO4YiMz1YQewyu0NUdfA
                """.trimIndent()
            )
            return
        }

        userService.unlink(userId)
        bot.sendMessage(chatId = ChatId.fromId(chatId), text = "Телеграм аккаунт был отвязан")
    }

    fun CommandHandlerEnvironment.handleStartCommand() {
        val chatId = message.chat.id
        if (userService.findByTelegramChatId(chatId) != null) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = "Ваш аккаунт уже привязан."
            )
            return
        }

        val text = message.text ?: return
        val result = kotlin.runCatching {
            String(Base64.decodeBase64URLSafe(text.removePrefix("/start ")))
        }

        val request = result
            .map { verificationService.findRequest(it) }
            .getOrNull()

        if (request == null) {
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = """
                    Пожалуйста, перейдите по ссылке из навыка.
                    
                    https://dialogs.yandex.ru/share?key=aO4YiMz1YQewyu0NUdfA
                """.trimIndent()
            )
        } else {
            verificationService.acceptRequest(request.id!!, chatId)
            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text = """
                    Добрый день, ${request.user.name}.
                     
                    Привязка успешно осуществлена.
                    Можете возвращаться в навык.
                    
                    https://dialogs.yandex.ru/share?key=aO4YiMz1YQewyu0NUdfA
                """.trimIndent()
            )
        }
    }
}
