package com.terabyte

import com.terabyte.plugin.configureJWT
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureJWT()


    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true   // временно для диагностики
            isLenient = true
        })
    }
}
