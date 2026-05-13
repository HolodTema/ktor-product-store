package com.terabyte.service

import com.terabyte.model.CreateSaleOperationReceiptExistsRequest
import com.terabyte.model.SaleOperation
import com.terabyte.model.SaleOperations
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SaleOperationService {

    fun createSaleOperation(request: CreateSaleOperationReceiptExistsRequest): SaleOperation? = transaction {
        val result = SaleOperations.insert {
            it[SaleOperations.quantity] = request.quantity
            it[SaleOperations.productId] = request.productId
            it[SaleOperations.receiptId] = request.receiptId
        }

        val id = result[SaleOperations.id]
        getSaleOperationById(id)
    }

    fun getSaleOperationById(id: Int): SaleOperation? = transaction {
        SaleOperations.select { SaleOperations.id eq id }
            .map { row ->
                SaleOperation(
                    id = row[SaleOperations.id],
                    quantity = row[SaleOperations.quantity],
                    productId = row[SaleOperations.productId],
                    receiptId = row[SaleOperations.receiptId],
                )
            }.singleOrNull()
    }

    fun getAllSaleOperations(): List<SaleOperation> = transaction {
        SaleOperations.selectAll()
            .map { row ->
                SaleOperation(
                    id = row[SaleOperations.id],
                    quantity = row[SaleOperations.quantity],
                    productId = row[SaleOperations.productId],
                    receiptId = row[SaleOperations.receiptId],
                )
            }
    }
}