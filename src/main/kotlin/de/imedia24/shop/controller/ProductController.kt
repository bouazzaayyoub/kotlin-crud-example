package de.imedia24.shop.controller

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/products/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product)
        }
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    fun findAllProducts(@RequestParam skus: List<String>) : ResponseEntity<List<ProductResponse>>{
        var productsList = productService.findAllProducts(skus)
        return ResponseEntity.ok(productsList)
    }

    @PostMapping("/products")
    fun addProduct(@RequestBody productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        var product = productService.addProduct(productRequest)
        return if (product != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(product)
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PatchMapping("/products/{sku}")
    fun updateProduct(
        @PathVariable sku: String,
        @RequestBody productUpdateRequest: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        val updatedProduct = productService.updateProduct(sku, productUpdateRequest)

        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }

}
