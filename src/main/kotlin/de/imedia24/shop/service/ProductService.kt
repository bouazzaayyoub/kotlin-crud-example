package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findAllProducts(skus: List<String>): List<ProductResponse>{
        var productsList = productRepository.findBySkuIn(skus)
        return productsList.map { productEntity ->
            ProductResponse(
                name = productEntity.name,
                price = productEntity.price,
                description = productEntity.description.orEmpty(),
                sku = productEntity.sku,
                stockQuantity = productEntity.stockQuantity,
            )
        }
    }
    fun findProductBySku(sku: String): ProductResponse? {
        val product = productRepository.findBySku(sku)
        return product?.let {
            ProductResponse(
                name=it.name,
                sku = it.sku,
                description = it.description.orEmpty(),
                price = it.price,
                stockQuantity = it.stockQuantity
            )
        }
    }

    fun addProduct(productRequest: ProductRequest): ProductResponse?{
        val existingProduct = productRepository.findBySku(productRequest.sku)
        if (existingProduct != null) {
            return null
        }
        val product = ProductEntity(
            productRequest.sku,
            productRequest.name,
            productRequest.description,
            productRequest.price,
           0,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )
        productRepository.save(product)
        return product.let { ProductResponse(
            name = it.name,
            stockQuantity = it.stockQuantity,
            sku = it.sku,
            description = it.sku,
            price = it.price
        ) }
    }

    fun updateProduct(sku: String, productUpdateRequest: ProductUpdateRequest): ProductResponse?{
        val existingProduct = productRepository.findBySku(sku)

        if (existingProduct != null) {
            val updatedProduct = existingProduct.copy(
                name = productUpdateRequest.name ?: existingProduct.name,
                description = productUpdateRequest.description ?: existingProduct.description,
                price = productUpdateRequest.price ?: existingProduct.price
            )

            val savedProduct = productRepository.save(updatedProduct)
            return savedProduct.toProductResponse()
        }

        return null
    }




}
