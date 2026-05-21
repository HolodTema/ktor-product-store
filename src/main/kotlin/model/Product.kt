package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Products : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val price = double("price")
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

@Serializable
data class CreateProductRequest(
    val name: String,
    val price: Double,
    val quantity: Int,
    val categoryId: Int
)

@Serializable
data class UpdateProductRequest(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val categoryId: Int
)

@Serializable
data class UpdateProductQuantityRequest(
    val id: Int,
    val quantity: Int,
)
