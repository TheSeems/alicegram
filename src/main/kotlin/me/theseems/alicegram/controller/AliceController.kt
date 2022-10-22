package me.theseems.alicegram.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import me.theseems.alicegram.dto.AliceRequest
import me.theseems.alicegram.dto.AliceResponse
import me.theseems.alicegram.dto.AliceResponseBody
import me.theseems.alicegram.dto.AliceResponseButton
import me.theseems.alicegram.entity.AlicegramUser
import me.theseems.alicegram.service.user.AlicegramUserService
import me.theseems.alicegram.service.verify.VerificationService
import me.theseems.alicegram.service.yandex.YandexAuthenticationService
import me.theseems.alicegram.type.UserState
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AliceController {
    @Autowired
    private lateinit var bot: Bot

    @Autowired
    private lateinit var authenticationService: YandexAuthenticationService

    @Autowired
    private lateinit var verificationService: VerificationService

    @Autowired
    private lateinit var userService: AlicegramUserService

    @PostMapping("/")
    @ResponseBody
    fun handleAliceRequest(@RequestBody request: AliceRequest): AliceResponse {
        val token = request.session
            ?.user
            ?.accessToken
            ?: return handleUnauthorizedRequest(request)

        val user = authenticationService.findByToken(token)
            ?: authenticationService.createUserByToken(token)

        return when (user.state) {
            UserState.WAIT_TELEGRAM_VERIFY -> handleWaitTelegramVerify(request, user)
            UserState.ACTIVE -> handleActive(request, user)
        }
    }

    private fun handleActive(request: AliceRequest, user: AlicegramUser): AliceResponse {
        val text = request.content?.originalUtterance
        if (text.isNullOrEmpty()) {
            return AliceResponse("Скажите что-нибудь, и я отправлю это вам в телеграм")
        }

        val (result, exception) = bot.sendMessage(ChatId.fromId(user.telegramChatId!!), text)
        val error = result?.errorBody()
        val code = result?.code()

        if (error != null) {
            if (code == HttpStatus.FORBIDDEN.value()) {
                userService.unlink(user.id!!)
                return AliceResponse.Terminal("К сожалению, вы запретили сообщения, Ваш аккаунт отвязан")
            }

            exception?.printStackTrace()
            return AliceResponse.Terminal("Произошла ошибка при отправке.")
        }

        return AliceResponse.Terminal(text)
    }

    fun handleWaitTelegramVerify(request: AliceRequest, user: AlicegramUser): AliceResponse {
        var verificationRequest = user.verificationRequest

        if (verificationRequest == null) {
            verificationRequest = verificationService.createRequest(user)
        }

        val encoded = Base64.encodeBase64URLSafeString(verificationRequest.code.toByteArray())
        return AliceResponse(
            body = AliceResponseBody(
                text = "Пожалуйста, подтвердите привязку в телеграмм",
                buttons = listOf(
                    AliceResponseButton(
                        title = "Перейти в телеграм",
                        payload = mapOf<Any, Any>(),
                        url = "https://t.me/alicetelebot?start=$encoded",
                        hide = true
                    )
                )
            )
        )
    }

    fun handleUnauthorizedRequest(request: AliceRequest): AliceResponse {
        return AliceResponse(
            body = AliceResponseBody(
                text = "Привет! Давайте авторизируем Вас."
            ),
            startAccountLinking = mapOf()
        )
    }
}
