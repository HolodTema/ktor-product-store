package com.terabyte.model

import com.terabyte.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Receipts : Table() {
    val id = integer("id").autoIncrement()
    val createdAt = datetime("createdAt")
    val employeeId = integer("employeeId").references(Employees.id)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Receipt(
    val id: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val employeeId: Int,
)
