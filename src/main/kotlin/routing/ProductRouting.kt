package com.terabyte.routing

import com.terabyte.model.CreateProductRequest
import com.terabyte.model.UpdateProductQuantityRequest
import com.terabyte.model.UpdateProductRequest
import com.terabyte.plugin.getIsAdmin
import com.terabyte.service.ProductService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRouting(
    productService: ProductService,
) {
    route("/api/product") {
        get {
            val products = productService.getAllProducts()
            call.respond(products)
        }

        get("/{id}") {
            val productId = call.parameters["id"]?.toIntOrNull()
            if (productId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid productId")
                return@get
            }

            val product = productService.getProductById(productId)
            if (product == null) {
                call.respond(HttpStatusCode.NotFound, "Product not found")
            } else {
                call.respond(product)
            }
        }

        authenticate {
            post {
                val createProductRequest = call.receive<CreateProductRequest>()

                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden, "You have no rights to create products")
                    return@post
                }

                val product = productService.createProduct(createProductRequest)
                if (product == null) {
                    call.respond(HttpStatusCode.BadRequest, "Unable to create product")
                } else {
                    call.respond(HttpStatusCode.Created, product)
                }
            }

            put {
                val request = call.receive<UpdateProductRequest>()

                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden, "You have no rights to create products")
                    return@put
                }

                val isUpdated = productService.updateProduct(request)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete("/{id}") {
                val isAdmin = call.getIsAdmin()
                if (!isAdmin) {
                    call.respond(HttpStatusCode.Forbidden, "You have no rights to create products")
                    return@delete
                }

                val productId = call.parameters["id"]?.toIntOrNull()
                if (productId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid productId")
                    return@delete
                }

                val isDeleted = productService.deleteProduct(productId)
                if (isDeleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("/updateQuantity") {
                val request = call.receive<UpdateProductQuantityRequest>()

                val isUpdated = productService.updateProductQuantity(request)
                if (isUpdated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}