package me.theseems.alicegram.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class AliceResponseButton(
    val title: String,
    val payload: Any? = null,
    val url: String? = null,
    val hide: Boolean? = null
)

data class AliceResponseBody(
    val text: String,
    val tts: String = text,
    @JsonProperty("end_session")
    val endSession: Boolean? = false,
    val buttons: List<AliceResponseButton>? = null,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AliceResponse(
    @JsonProperty("response")
    val body: AliceResponseBody,

    val version: String = "1.0",

    @JsonProperty("start_account_linking")
    val startAccountLinking: Map<Any, Any?>? = null
) {
    companion object {
        operator fun invoke(text: String): AliceResponse {
            return AliceResponse(AliceResponseBody(text))
        }
    }

    object Terminal {
        operator fun invoke(text: String): AliceResponse {
            return AliceResponse(AliceResponseBody(text, endSession = true))
        }
    }
}
