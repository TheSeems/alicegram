package me.theseems.alicegram.service.yandex

import me.theseems.alicegram.dao.AlicegramUserDao
import me.theseems.alicegram.entity.AlicegramUser
import me.theseems.alicegram.type.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class YandexAuthenticationService {
    @Autowired
    private lateinit var client: YandexClient

    @Autowired
    private lateinit var alicegramUserDao: AlicegramUserDao

    @Transactional
    fun findByToken(token: String): AlicegramUser? {
        return try {
            alicegramUserDao.findByYandexId(
                client.getUserInfo(token)?.id ?: throw RuntimeException("Failed to contact with Yandex API")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Transactional
    fun createUserByToken(token: String): AlicegramUser {
        return try {
            val userInfo = client.getUserInfo(token)
                ?: throw RuntimeException("Failed to contact with Yandex API")

            alicegramUserDao.save(
                AlicegramUser(
                    state = UserState.WAIT_TELEGRAM_VERIFY,
                    name = userInfo.displayName,
                    yandexId = userInfo.id
                )
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to create user", e)
        }
    }
}
