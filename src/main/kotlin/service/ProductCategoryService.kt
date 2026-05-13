package com.terabyte.service

import com.terabyte.model.CreateProductCategoryRequest
import com.terabyte.model.ProductCategories
import com.terabyte.model.ProductCategory
import com.terabyte.model.UpdateProductCategoryRequest
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ProductCategoryService {

    fun createProductCategory(request: CreateProductCategoryRequest): ProductCategory? = transaction {
        val result = ProductCategories.insert {
            it[ProductCategories.name] = request.name
        }

        val id = result[ProductCategories.id]
        getProductCategoryById(id)
    }

    fun updateProductCategory(request: UpdateProductCategoryRequest): Boolean = transaction {
        val numberUpdatedRows = ProductCategories.update(
            where = { ProductCategories.id eq request.id },
        ) {
            it[ProductCategories.name] = request.name
        }
        numberUpdatedRows > 0
    }

    fun deleteProductCategory(id: Int): Boolean = transaction {
        val numberDeletedRows = ProductCategories.deleteWhere {
            ProductCategories.id eq id
        }
        numberDeletedRows > 0
    }

    fun getProductCategoryById(id: Int): ProductCategory? = transaction {
        ProductCategories.select { ProductCategories.id eq id }
            .map { row ->
                ProductCategory(
                    id = row[ProductCategories.id],
                    name = row[ProductCategories.name]
                )
            }.singleOrNull()
    }

    fun getAllProductCategories(): List<ProductCategory> = transaction {
        ProductCategories.selectAll()
            .map { row ->
                ProductCategory(
                    id = row[ProductCategories.id],
                    name = row[ProductCategories.name]
                )
            }
    }

}
