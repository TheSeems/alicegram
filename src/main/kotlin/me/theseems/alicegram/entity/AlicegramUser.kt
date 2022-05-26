package me.theseems.alicegram.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToOne
import me.theseems.alicegram.entity.base.BaseEntity
import me.theseems.alicegram.entity.telegram.VerificationRequest
import me.theseems.alicegram.type.UserState

@Entity
class AlicegramUser(
    @field:Column(nullable = false, length = UserState.MAX_LENGTH)
    @field:Enumerated(EnumType.STRING)
    var state: UserState,

    @field:Column(nullable = false)
    val name: String,

    @field:Column
    val yandexId: Long? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "verification_requests",
        joinColumns = [
            JoinColumn(name = "user_id", referencedColumnName = "id")
        ],
        inverseJoinColumns = [
            JoinColumn(name = "request_id", referencedColumnName = "id")
        ]
    )
    var verificationRequest: VerificationRequest? = null,

    var telegramChatId: Long? = null
) : BaseEntity<Long>()
