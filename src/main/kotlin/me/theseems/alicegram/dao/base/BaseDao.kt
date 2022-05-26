package me.theseems.alicegram.dao.base

import me.theseems.alicegram.entity.base.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface BaseDao<ID : Serializable, T : BaseEntity<ID>> : JpaRepository<T, ID>
