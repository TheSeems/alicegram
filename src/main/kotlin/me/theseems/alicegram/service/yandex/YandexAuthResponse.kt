package me.theseems.alicegram.service.yandex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class YandexAuthResponse(
    val login: String,
    val id: Long,

    @field:JsonProperty("default_email")
    val defaultEmail: String?,

    @field:JsonProperty("display_name")
    val displayName: String,

    @field:JsonProperty("first_name")
    val firstName: String,

    @field:JsonProperty("real_name")
    val realName: String,

    @field:JsonProperty("client_id")
    val clientId: String
)
