package me.theseems.alicegram.service.user

import me.theseems.alicegram.dao.AlicegramUserDao
import me.theseems.alicegram.entity.AlicegramUser
import me.theseems.alicegram.type.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlicegramUserService {
    @Autowired
    private lateinit var userDao: AlicegramUserDao

    @Transactional
    fun findByTelegramChatId(telegramChatId: Long): AlicegramUser? {
        return userDao.findByTelegramChatId(telegramChatId)
    }

    @Transactional
    fun unlink(userId: Long) {
        userDao.findById(userId).ifPresent {
            it.state = UserState.WAIT_TELEGRAM_VERIFY
            it.telegramChatId = null
        }
    }
}
