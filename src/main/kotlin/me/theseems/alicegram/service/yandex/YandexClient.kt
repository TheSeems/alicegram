package me.theseems.alicegram.service.yandex

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface YandexClient {
    @GetExchange("/info")
    @ResponseBody
    fun getUserInfo(
        @RequestParam("oauth_token") oauthToken: String
    ): YandexAuthResponse?
}
