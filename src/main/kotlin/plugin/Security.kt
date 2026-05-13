package com.terabyte.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*


fun Application.configureJWT() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()

    install(Authentication) {
        jwt {
            realm = jwtRealm

            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .build()
            )

            validate { credential ->
                val employeeId = credential.payload.getClaim("employeeId").asInt()
                val isAdmin = credential.payload.getClaim("isAdmin").asBoolean()
                if (employeeId == null || isAdmin == null) {
                    return@validate null
                }
                return@validate JWTPrincipal(credential.payload)
            }
        }
    }
}

fun Application.generateToken(employeeId: Int, isAdmin: Boolean): String {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()

    return JWT.create()
        .withIssuer(jwtIssuer)
        .withClaim("employeeId", employeeId)
        .withClaim("isAdmin", isAdmin)
        .withExpiresAt(Date(System.currentTimeMillis() + 86_400_000))
        .sign(Algorithm.HMAC256(jwtSecret))
}

fun ApplicationCall.getEmployeeId(): Int {
    val principal = principal<JWTPrincipal>()

    return principal?.payload?.getClaim("employeeId")?.asInt()
        ?: throw IllegalArgumentException("No employeeId in this token.")
}

fun ApplicationCall.getIsAdmin(): Boolean {
    val principal = principal<JWTPrincipal>()

    return principal?.payload?.getClaim("isAdmin")?.asBoolean()
        ?: throw IllegalArgumentException("No isAdmin in this token.")
}
