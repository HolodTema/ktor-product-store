package com.terabyte.routing

import com.terabyte.service.ReceiptService
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.receiptRouting(
    receiptService: ReceiptService
) {
    route("/api/receipt") {
        authenticate {
            get {
                val receipts = receiptService.getAllReceipts()
                call.respond(receipts)
            }
        }
    }
}