package com.terabyte.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.terabyte.model.Employee
import com.terabyte.model.EmployeeLoginRequest
import com.terabyte.model.Employees
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class EmployeeService {

    fun login(request: EmployeeLoginRequest): Employee? = transaction {
        val employee = getEmployeeByEmail(request.email)
        if (employee == null) {
            return@transaction null
        }

        val isPasswordValid = BCrypt.verifyer()
            .verify(request.password.toCharArray(), employee.passwordHash)
            .verified

        if (isPasswordValid) {
            employee
        }
        else {
            null
        }
    }

    fun getEmployeeByEmail(email: String): Employee? = transaction {
        Employees.select { Employees.email eq email }
            .map {
                Employee(
                    id = it[Employees.id],
                    name = it[Employees.name],
                    surname = it[Employees.surname],
                    email = it[Employees.email],
                    passwordHash = it[Employees.passwordHash],
                    jobTitleId = it[Employees.jobTitleId]
                )
            }.singleOrNull()
    }
}