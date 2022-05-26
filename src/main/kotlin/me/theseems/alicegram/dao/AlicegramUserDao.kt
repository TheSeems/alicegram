package me.theseems.alicegram.dao

import me.theseems.alicegram.dao.base.BaseDao
import me.theseems.alicegram.entity.AlicegramUser
import org.springframework.stereotype.Repository

@Repository
interface AlicegramUserDao : BaseDao<Long, AlicegramUser> {
    fun findByYandexId(yandexId: Long): AlicegramUser?

    fun findByTelegramChatId(telegramChatId: Long): AlicegramUser?
}
