package com.racket.cart.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.cart.response.CartResponseView
import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.user.UserService
import com.racket.api.user.presentation.response.UserResponseView
import com.racket.cart.api.client.delivery.DeliveryClient
import com.racket.cart.api.client.product.ProductClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.RecordApplicationEvents
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

    @Autowired
    lateinit var userService: UserService

    @Autowired lateinit var productClient: ProductClient
    @Autowired lateinit var deliveryClient: DeliveryClient

    @Test
    fun `Cart Test - UserID로 장바구니를 조회한다 데이터가 존재하면 200 성공과 리스트를 출력한다`() {
        // given
        this.saveProduct()
        val optionList = this.saveOption()
        val userId = 1L

//        // when
//        val sut = this.mockMvc.get("/api/v1/cart/{userId}", userId)
//            .andExpect { status { isOk() } }
//            .andReturn()
//
//        // then
//        val resultView = objectMapper.registerModule(JavaTimeModule()).readValue(sut.response.contentAsString, CartResponseView::class.java)
//        val responseViewList: List<CartResponseView> =
//            objectMapper.readValue(sut.response.contentAsString, object : TypeReference<List<CartResponseView>>() {})
//        Assertions.assertTrue(responseViewList.isNotEmpty())
//        assertThat(responseViewList.size == optionList.size)
    }

    fun saveProduct(): Product {
        val savedProduct = Product(price = 10000, name = "TestProduct", customerProductCode = "customerProductCode")
        return this.productRepository.save(savedProduct)
    }

    fun saveOption(): List<Option> {
        val savedProduct = this.saveProduct()
        val options = listOf(
            Option(
                productId = savedProduct.id!!,
                optionNo = "Option 2",
                name = "Option 2",
                optionAdditionalPrice = 2000,
                stock = 200,
                sort = 1
            ),
            Option(
                productId = savedProduct.id!!,
                optionNo = "Option 3",
                name = "Option 3",
                optionAdditionalPrice = 3000,
                stock = 300,
                sort = 2
            )
        )
        return this.optionRepository.saveAll(options).toList()
    }

    //TODO :   MobileVO 가 없어서 shared 를 import 해야했음. 이렇게 하는 것이 맞낭...?
//    fun saveUser() {
//        val userRegisterDTO = UserService.UserRegisterDTO(
//            userName = "tdd_user",
//            email = "tdd_user@naver.com",
//            password = "1234567",
//            mobileVO = MobileVO(number = "01012341234")
//        )
//        return this.userService.registerUser(userRegisterDTO)
//    }




}