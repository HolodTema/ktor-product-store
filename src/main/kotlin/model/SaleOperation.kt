package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object SaleOperations : Table() {
    val id = integer("id").autoIncrement()
    val quantity = integer("quantity")
    val product = integer("productId").references(Products.id)
    val receiptId = integer("receiptId").references(Receipts.id)
}

@Serializable
data class SaleOperation(
    val id: Int,
    val quantity: Int,
    val productId: Int,
    val receiptId: Int
)
