package com.terabyte.routing

import com.terabyte.model.CostResponse
import com.terabyte.service.StatisticService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.statisticsRouting(
    statisticService: StatisticService
) {
    route("/api/statistics") {
        authenticate {
            get("/costByReceiptId/{id}") {
                val receiptId = call.parameters["id"]?.toIntOrNull()
                if (receiptId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val cost = statisticService.getCostByReceiptId(receiptId)
                val response = CostResponse(cost)
                call.respond(response)
            }
        }
    }
}
