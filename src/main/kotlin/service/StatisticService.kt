package com.terabyte.service

import com.terabyte.model.Products
import com.terabyte.model.SaleOperation
import com.terabyte.model.SaleOperations
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class StatisticService {

    fun getCostByReceiptId(receiptId: Int): Double = transaction {
        val saleOperations = SaleOperations.select { SaleOperations.receiptId eq receiptId }.map { row ->
            SaleOperation(
                id = row[SaleOperations.id],
                quantity = row[SaleOperations.quantity],
                productId = row[SaleOperations.productId],
                receiptId = row[SaleOperations.receiptId]
            )
        }

        var result = 0.0
        for (saleOperation in saleOperations) {
            val productPrice = Products.select { Products.id eq saleOperation.productId }.map { row ->
                row[Products.price]
            }.singleOrNull()?.toDouble()

            if (productPrice == null) {
                continue
            }

            result += productPrice * saleOperation.quantity
        }
        result
    }
}