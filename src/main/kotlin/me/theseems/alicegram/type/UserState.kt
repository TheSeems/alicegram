package me.theseems.alicegram.type

enum class UserState {
    WAIT_TELEGRAM_VERIFY, ACTIVE;

    companion object {
        const val MAX_LENGTH = 64
    }
}
