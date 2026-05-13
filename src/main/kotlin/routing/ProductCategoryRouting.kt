package com.terabyte.routing

import com.terabyte.service.ProductCategoryService
import io.ktor.server.routing.*

fun Route.productCategoryRouting(
    productCategoryService: ProductCategoryService
) {
    route("/api/productCategory") {
        
    }
}