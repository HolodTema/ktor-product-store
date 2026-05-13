package com.terabyte.routing

import com.terabyte.model.EmployeeAuthResponse
import com.terabyte.model.EmployeeLoginRequest
import com.terabyte.plugin.generateToken
import com.terabyte.service.EmployeeService
import com.terabyte.service.JobTitleService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.employeeAuthRouting(
    employeeService: EmployeeService,
    jobTitleService: JobTitleService
) {
    route("/api/auth/employee") {
        post("/login") {
            val request = call.receive<EmployeeLoginRequest>()
            val employee = employeeService.login(request)

            if (employee == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }


            val jobTitle = jobTitleService.getJobTitleById(employee.jobTitleId)
            if (jobTitle == null) {
                call.respond(HttpStatusCode.InternalServerError)
                return@post
            }

            val token = application.generateToken(employee.id, jobTitle.isAdmin)

            val response = EmployeeAuthResponse(
                token = token,
                employeeId = employee.id,
                name = employee.name,
                surname = employee.surname,
                email = employee.email,
                jobTitleName = jobTitle.name,
                isAdmin = jobTitle.isAdmin
            )
            call.respond(response)
        }
    }
}
