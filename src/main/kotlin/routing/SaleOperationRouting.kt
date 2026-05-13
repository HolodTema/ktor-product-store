package com.terabyte.routing

import com.terabyte.model.CreateSaleOperationReceiptExistsRequest
import com.terabyte.model.CreateSaleOperationRequest
import com.terabyte.service.ReceiptService
import com.terabyte.service.SaleOperationService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.saleOperationRouting(
    saleOperationService: SaleOperationService,
    receiptService: ReceiptService
) {
    route("/api/saleOperation") {
        authenticate {
            get {
                val saleOperations = saleOperationService.getAllSaleOperations()
                call.respond(saleOperations)
            }

            post {
                val request = call.receive<CreateSaleOperationRequest>()

                val receipt = receiptService.createReceiptCurrentDateTime(request.productId)
                if (receipt == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }

                val createSaleOperationReceiptExistsRequest = CreateSaleOperationReceiptExistsRequest(
                    quantity = request.quantity,
                    productId = request.productId,
                    receiptId = receipt!!.id
                )

                val saleOperation = saleOperationService.createSaleOperation(createSaleOperationReceiptExistsRequest)
                if (saleOperation == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }
                else {
                    call.respond(HttpStatusCode.Created, saleOperation)
                }
            }
        }
    }
}