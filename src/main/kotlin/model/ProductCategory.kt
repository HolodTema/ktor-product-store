package com.terabyte.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object ProductCategories : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class ProductCategory(
    val id: Int,
    val name: String
)

@Serializable
data class CreateProductCategoryRequest(
    val name: String
)

@Serializable
data class UpdateProductCategoryRequest(
    val id: Int,
    val name: String
)
