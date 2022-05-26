package me.theseems.alicegram.service.verify

import me.theseems.alicegram.dao.VerificationRequestDao
import me.theseems.alicegram.entity.AlicegramUser
import me.theseems.alicegram.entity.telegram.VerificationRequest
import me.theseems.alicegram.type.UserState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class VerificationService {
    @Autowired
    private lateinit var verificationRequestDao: VerificationRequestDao

    @Transactional(readOnly = true)
    fun findRequest(code: String) = verificationRequestDao.findByCode(code)

    @Transactional
    fun createRequest(user: AlicegramUser): VerificationRequest {
        if (user.verificationRequest != null) {
            throw IllegalStateException("User already has a verification request")
        }

        // TODO: maybe lock table?
        // TODO: we can definitely do that at SQL-level which will improve performance and robustness
        var generatedCode: String
        do {
            generatedCode = UUID.randomUUID().toString()
        } while (verificationRequestDao.findByCode(generatedCode) != null)

        val request = verificationRequestDao.save(VerificationRequest(user, generatedCode))
        user.verificationRequest = request
        return request
    }

    @Transactional
    fun acceptRequest(requestId: Long, telegramChatId: Long) {
        val request = verificationRequestDao
            .findById(requestId)
            .orElse(null)
            ?: throw IllegalStateException("Request is not found")

        request.user.telegramChatId = telegramChatId
        request.user.verificationRequest = null

        request.user.state = UserState.ACTIVE
        verificationRequestDao.delete(request)
    }
}
