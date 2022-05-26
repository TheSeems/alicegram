package me.theseems.alicegram.dao

import me.theseems.alicegram.dao.base.BaseDao
import me.theseems.alicegram.entity.telegram.VerificationRequest
import org.springframework.stereotype.Repository

@Repository
interface VerificationRequestDao : BaseDao<Long, VerificationRequest> {
    fun findByCode(code: String): VerificationRequest?
}
