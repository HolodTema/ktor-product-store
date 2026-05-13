package com.terabyte

import com.terabyte.plugin.configureDatabase
import com.terabyte.plugin.configureJWT
import com.terabyte.routing.employeeAuthRouting
import com.terabyte.service.EmployeeService
import com.terabyte.service.JobTitleService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureJWT()

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true   // временно для диагностики
            isLenient = true
        })
    }

    val employeeService = EmployeeService()
    val jobTitleService = JobTitleService()

    routing {
        employeeAuthRouting(employeeService, jobTitleService)
    }
}
