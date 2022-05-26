package me.theseems.alicegram.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AliceUser(
    @JsonProperty("user_id")
    val userId: String,

    @JsonProperty("access_token")
    val accessToken: String?
)

data class AliceSession(
    @JsonProperty("message_id")
    val messageId: String?,
    @JsonProperty("session_id")
    val sessionId: String?,
    @JsonProperty("skill_id")
    val skillId: String?,
    @JsonProperty("user")
    val user: AliceUser?,
    @JsonProperty("user_id")
    val userId: String?
)

data class AliceRequestContent(
    val command: String,
    @JsonProperty("original_utterance")
    val originalUtterance: String?,
    val nlu: Any?,
    val markup: Any?,
    val type: String
)

data class AliceRequest(
    val meta: Any?,
    val session: AliceSession?,
    @JsonProperty("request")
    val content: AliceRequestContent?,
    val state: Any?,
    val version: String?
)
