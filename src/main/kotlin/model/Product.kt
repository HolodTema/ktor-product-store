package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Products : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val price = decimal("price", 10, 2)
    val quantity = integer("quantity")
    val categoryId = integer("categoryId").references(ProductCategories.id)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val categoryId: Int
)
