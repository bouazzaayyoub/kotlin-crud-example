package de.imedia24.shop

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class ShopApplicationTests(
	@Autowired private val mockMvc: MockMvc,
	@Autowired private val objectMapper: ObjectMapper
	) {
	@Mock
	private lateinit var productService: ProductService

	@Test
	fun `Test updating product`(){
		val sku = "12345"
		val productRequest = ProductUpdateRequest(
			name = "updated product",
			description = "description",
			price = BigDecimal.valueOf(19.99)
		)

		val updatedProduct = ProductResponse(
			sku = sku,
			name = "updated product",
			description = "description",
			price = BigDecimal.valueOf(19.99),
			stockQuantity = 0
		)

		`when`(productService.updateProduct(sku, productRequest)).thenReturn(updatedProduct)
		mockMvc.perform(
			MockMvcRequestBuilders.put("/products/{sku}", sku)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest))
		)
			.andExpect(MockMvcResultMatchers.status().isOk)
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Product"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated description"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(19.99))

		verify(productService).updateProduct(sku, productRequest)

	}

}
