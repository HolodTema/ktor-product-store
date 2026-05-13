package com.terabyte.service

import com.terabyte.model.Receipt
import com.terabyte.model.Receipts
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class ReceiptService {

    fun createReceiptCurrentDateTime(employeeId: Int): Receipt? = transaction {
        val result = Receipts.insert {
            it[Receipts.createdAt] = LocalDateTime.now()
            it[Receipts.employeeId] = employeeId
        }

        val id = result[Receipts.id]
        getReceiptById(id)
    }

    fun getReceiptById(id: Int): Receipt? = transaction {
        Receipts.select { Receipts.id eq id }
            .map { row ->
                Receipt(
                    id = row[Receipts.id],
                    createdAt = row[Receipts.createdAt],
                    employeeId = row[Receipts.employeeId],
                )
            }.singleOrNull()
    }

    fun getAllReceipts(): List<Receipt> = transaction {
        Receipts.selectAll()
            .map { row ->
                Receipt(
                    id = row[Receipts.id],
                    createdAt = row[Receipts.createdAt],
                    employeeId = row[Receipts.employeeId],
                )
            }
    }

}
