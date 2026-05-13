package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Employees : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val surname = varchar("surname", 50)
    val email = varchar("email", 50)
    val passwordHash = varchar("passwordHash", 200)
    val jobTitleId = integer("jobTitleId").references(JobTitles.id)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Employee(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val passwordHash: String,
    val jobTitleId: Int
)

@Serializable
data class EmployeeLoginRequest(
    val email: String,
    val password: String
)
