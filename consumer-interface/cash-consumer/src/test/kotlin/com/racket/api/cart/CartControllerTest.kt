package com.racket.api.cart

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.product.ProductService
import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.presentation.response.ProductResponseView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CartControllerTest {

    val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var productRepository: ProductRepository
    @Autowired
    lateinit var optionRepository: OptionRepository

    private fun saveProductAndOptions(): List<Option> {
        val savedProduct = Product(price = 10000, name = "TestProduct", customerProductCode = "customerProductCode")
        this.productRepository.save(savedProduct)

        val options = listOf(
            Option(
                productId = savedProduct.id!!,
                optionNo = "Option 1",
                name = "Option 1",
                optionAdditionalPrice = 2000,
                stock = 200,
                sort = 1
            ),
            Option(
                productId = savedProduct.id!!,
                optionNo = "Option 2",
                name = "Option 2",
                optionAdditionalPrice = 3000,
                stock = 300,
                sort = 2
            )
        )
        this.optionRepository.saveAll(options)
        return this.optionRepository.findByProductId(productId = savedProduct.id!!)
    }


    @Test
    fun `Cart Test - 장바구니 아이템 추가 성공하면 200`() {
        // given
        val productId = this.saveProductAndOptions()[1].productId
//        val sut = this.mockMvc.get("/api/v1/product/{product_id}", productId) {}
//            .andExpect { status { isOk() } }
//            .andReturn()
//
//        // then
//        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, ProductResponseView::class.java)
//        Assertions.assertEquals(productId, resultView.id)

        // then

        // when
    }


}