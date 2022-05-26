package me.theseems.alicegram.entity.telegram

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import me.theseems.alicegram.entity.AlicegramUser
import me.theseems.alicegram.entity.base.BaseEntity

@Entity
class VerificationRequest(
    @OneToOne(mappedBy = "verificationRequest")
    val user: AlicegramUser,

    val code: String
) : BaseEntity<Long>()
