package com.terabyte.service

import com.terabyte.model.CreateProductRequest
import com.terabyte.model.Product
import com.terabyte.model.ProductCategories
import com.terabyte.model.Products
import com.terabyte.model.UpdateProductQuantityRequest
import com.terabyte.model.UpdateProductRequest
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ProductService {

    fun createProduct(request: CreateProductRequest): Product? = transaction {
        val result = Products.insert {
            it[Products.name] = request.name
            it[Products.price] = request.price
            it[Products.quantity] = request.quantity
            it[Products.categoryId] = request.categoryId
        }

        val id = result[Products.id]
        getProductById(id)
    }

    fun updateProduct(request: UpdateProductRequest): Boolean = transaction {
        val numberUpdatedRows = Products.update(
            where = { Products.id eq request.id },
        ) {
            it[Products.name] = request.name
            it[Products.price] = request.price
            it[Products.quantity] = request.quantity
            it[Products.categoryId] = request.categoryId
        }
        numberUpdatedRows > 0
    }

    fun updateProductQuantity(request: UpdateProductQuantityRequest): Boolean = transaction {
        val numberUpdatedRows = Products.update(
            where = { Products.id eq request.id },
        ) {
            it[Products.quantity] = request.quantity
        }
        numberUpdatedRows > 0
    }

    fun deleteProduct(id: Int): Boolean = transaction {
        val numberDeletedRows = Products.deleteWhere {
            Products.id eq id
        }
        numberDeletedRows > 0
    }

    fun getProductById(id: Int): Product? = transaction {
        Products.select { Products.id eq id }
            .map { row ->
                Product(
                    id = row[Products.id],
                    name = row[Products.name],
                    price = row[Products.price],
                    quantity = row[Products.quantity],
                    categoryId = row[Products.categoryId]
                )
            }.singleOrNull()
    }

    fun getAllProducts(): List<Product> = transaction {
        Products.selectAll()
            .map { row ->
                Product(
                    id = row[Products.id],
                    name = row[Products.name],
                    price = row[Products.price],
                    quantity = row[Products.quantity],
                    categoryId = row[Products.categoryId]
                )
            }
    }
}