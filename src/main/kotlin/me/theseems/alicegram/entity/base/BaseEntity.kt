package me.theseems.alicegram.entity.base

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.util.ProxyUtils
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity<T : Serializable> {
    companion object {
        const val DEFAULT_HASH = 25
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: T? = null

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (this === other) {
            return true
        }
        if (javaClass != ProxyUtils.getUserClass(other)) {
            return false
        }

        other as BaseEntity<*>
        return this.id == other.id
    }

    override fun hashCode(): Int = DEFAULT_HASH

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id)"
    }
}
