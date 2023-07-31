package com.racket.api.admin.product

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.admin.product.request.ProductCreateRequestCommand
import com.racket.api.admin.product.request.ProductUpdateRequestCommand
import com.racket.api.product.CreateProductService
import com.racket.api.product.response.ProductResponseView
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AdminProductControllerTest {

    val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var createProductService: CreateProductService

    companion object {
        private val productCreateRequestCommand = ProductCreateRequestCommand(
            customerProductCode = "TestProductCode",
            productName = "productName",
            productPrice = 100000
        )
    }

    private fun saveProduct(): ProductResponseView =
        this.createProductService.registerProduct(
            CreateProductService.ProductRegisterDTO(
                customerProductCode = "TestProductCode", name = "TestName", price = 10000
            )
        )

    @Test
    fun `Admin Product Test - 상품을 생성한다 생성이 완료되면 HttpStatus 201이 나와야 하며 DB 에 존재해야 한다`() {
        // given
        // when
        val sut = this.mockMvc.post("/api/v1/admin/product") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(productCreateRequestCommand)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        // then
        val resultView =
            objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, ProductResponseView::class.java)
        assertNotNull(resultView.id)
    }

    @Test
    fun `Admin Product Test - 상품을 수정한다 수정이 완료되면 HttpStatus 200이 나와야 하며 DB 에 존재해야 한다`() {
        // given
        val product = this.saveProduct()
        val updateRequestCommand = ProductUpdateRequestCommand(
            productPrice = 555555, productName = "changedName"
        )

        // when
        val sut = this.mockMvc.patch("/api/v1/admin/product/info/{id}", product.id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(updateRequestCommand)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val resultView =
            objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, ProductResponseView::class.java)
        assertEquals(updateRequestCommand.productName, resultView.name)
        assertEquals(updateRequestCommand.productPrice, resultView.price)
    }


}