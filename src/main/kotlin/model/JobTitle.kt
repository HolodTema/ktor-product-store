package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object JobTitles : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class JobTitle(
    val id: Int,
    val name: String
)
