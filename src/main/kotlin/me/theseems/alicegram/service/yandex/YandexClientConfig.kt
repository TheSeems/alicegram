package me.theseems.alicegram.service.yandex

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class YandexClientConfig {
    companion object {
        private const val YANDEX_AUTH_USER_URL = "https://login.yandex.ru"
    }

    @Bean
    fun yandexClient(): YandexClient {
        val webClient = WebClient.builder()
            .baseUrl(YANDEX_AUTH_USER_URL)
            .build()

        return HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient)).build()
            .createClient(YandexClient::class.java)
    }
}
