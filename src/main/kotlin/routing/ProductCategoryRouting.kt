package com.terabyte.routing

import com.terabyte.model.CreateProductCategoryRequest
import com.terabyte.model.UpdateProductCategoryRequest
import com.terabyte.plugin.getIsAdmin
import com.terabyte.service.ProductCategoryService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productCategoryRouting(
    productCategoryService: ProductCategoryService
) {
    route("/api/productCategory") {
        get {
            val productCategories = productCategoryService.getAllProductCategories()
            call.respond(productCategories)
        }

        authenticate {
            post {
                val request = call.receive<CreateProductCategoryRequest>()

                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }

                val productCategory = productCategoryService.createProductCategory(request)
                if (productCategory == null) {
                    call.respond(HttpStatusCode.BadRequest)
                }
                else {
                    call.respond(HttpStatusCode.Created, productCategory)
                }
            }

            put {
                val request = call.receive<UpdateProductCategoryRequest>()

                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@put
                }

                val isUpdated = productCategoryService.updateProductCategory(request)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete("/{id}") {
                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }

                val productCategoryId = call.parameters["id"]?.toIntOrNull()
                if (productCategoryId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                val isDeleted = productCategoryService.deleteProductCategory(productCategoryId)
                if (isDeleted) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}