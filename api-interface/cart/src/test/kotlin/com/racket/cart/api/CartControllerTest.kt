package com.racket.cart.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.cart.api.domain.CartRepository
import com.racket.cart.api.exception.CartStockException
import com.racket.cart.api.request.CartCreateRequestCommand
import com.racket.cart.api.request.CartUpdateRequestCommand
import com.racket.cart.api.response.CartResponseView
import com.racket.cart.api.vo.DeliveryInformationVO
import com.racket.cart.config.WireMockServerConfig
import com.racket.cart.mock.api.MockProductApi
import com.racket.share.domain.user.User
import com.racket.share.domain.user.UserRepository
import com.racket.share.vo.MobileVO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = [WireMockServerConfig::class])
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CartControllerTest {

    private val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var productRepository: ProductRepository
    @Autowired
    lateinit var optionRepository: OptionRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var cartRepository: CartRepository
    @Autowired
    lateinit var productWireMockServer: WireMockServer

    companion object {
        const val cartRequestURL = "/api/v1/cart"
    }

    @AfterEach
    fun deleteAll() {
        this.userRepository.deleteAll()
        this.optionRepository.deleteAll()
        this.productRepository.deleteAll()
        this.cartRepository.deleteAll()
    }

    @Test
    fun `Cart Test - UserID로 장바구니를 조회한다 장바구니 아이템이 없으면 200 성공과 empty list 를 리턴한다`() {
        // given
        this.saveProducts()
        this.saveOptions()
        this.saveUser()
        val userId = 1L

        // when
        val sut = this.mockMvc.get("${cartRequestURL}/{userId}", userId)
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val responseViewList: List<CartResponseView> =
            objectMapper.readValue(sut.response.contentAsString, object : TypeReference<List<CartResponseView>>() {})
        Assertions.assertTrue(responseViewList.isEmpty())
    }

    @Test
    fun `Cart Test - 장바구니에 아이템을 넣은 이후 장바구니를 조회하면 200 성공과 아이템이 조회된다`() {
        // given
        this.saveProducts()
        val options = this.saveOptions()
        val user = this.saveUser()
        val createCommand = CartCreateRequestCommand(
            userId = user.id!!,
            productId = options[0].productId,
            optionId = options[0].id!!,
            optionName = options[0].name,
            originalPrice = options[0].optionAdditionalPrice,
            calculatedPrice = 10000,
            orderQuantity = 1,
            deliveryInformation = DeliveryInformationVO(deliveryCost = 3000, expectedDate = LocalDate.now())
        )

        // 장바구니 아이템 추가
        MockProductApi.setupGetOptionResponse(this.productWireMockServer)
        this.mockMvc.post(cartRequestURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(createCommand)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        // when
        val sut = this.mockMvc.get("${cartRequestURL}/{userId}", user.id)
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val responseViewList: List<CartResponseView> =
            objectMapper.readValue(sut.response.contentAsString, object : TypeReference<List<CartResponseView>>() {})
        Assertions.assertTrue(responseViewList.size == 1)
    }

    @Test
    fun `Cart Test - 장바구니 아이템 추가시, 주문 수량이 0 이하이면 404 exception 발생`() {
        // given
        this.saveProducts()
        val options = this.saveOptions()
        val user = this.saveUser()
        val createCommand = CartCreateRequestCommand(
            userId = user.id!!,
            productId = options[0].productId,
            optionId = options[0].id!!,
            optionName = options[0].name,
            originalPrice = options[0].optionAdditionalPrice,
            calculatedPrice = 10000,
            orderQuantity = 0,
            deliveryInformation = DeliveryInformationVO(deliveryCost = 3000, expectedDate = LocalDate.now())
        )
        MockProductApi.setupGetOptionResponse(this.productWireMockServer)

        // when
        val sut = this.mockMvc.post(cartRequestURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(createCommand)
        }.andExpect {
            status { isBadRequest() }
        }.andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is IllegalArgumentException)
    }

    @Test
    fun `Cart Test - 장바구니 아이템 수량 수정이 성공하면 200 리턴 & 변경 수량으로 반영되어야 한다`() {
        // given
        val updateQuantity = 30L
        this.saveProducts()
        val options = this.saveOptions()
        val user = this.saveUser()
        val createCommand = CartCreateRequestCommand(
            userId = user.id!!,
            productId = options[0].productId,
            optionId = options[0].id!!,
            optionName = options[0].name,
            originalPrice = options[0].optionAdditionalPrice,
            calculatedPrice = 10000,
            orderQuantity = 20,
            deliveryInformation = DeliveryInformationVO(deliveryCost = 3000, expectedDate = LocalDate.now())
        )
        MockProductApi.setupGetOptionResponse(this.productWireMockServer)

        val createdCartItem = this.mockMvc.post(cartRequestURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(createCommand)
        }.andExpect {
            status { HttpStatus.CREATED }
        }.andReturn()
            .response
            .contentAsString
        val createdCartItemId = objectMapper.readValue(createdCartItem, CartResponseView::class.java).id

        // when
        val updateCommand = CartUpdateRequestCommand(orderQuantity = updateQuantity)
        val sut = this.mockMvc.patch("$cartRequestURL/{cartItemId}/quantity", createdCartItemId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(updateCommand)
        }.andExpect {
            status { isOk() }
        }. andReturn()
            .response
            .contentAsString

        // then
        val updateCart = objectMapper.readValue(sut, CartResponseView::class.java)
        Assertions.assertEquals(updateQuantity, updateCart.orderQuantity)
    }

    @Test
    fun `Cart Test - 장바구니 아이템 수량 수정시 가용재고보다 많으면, CartStockException 발생`() {
        // given
        this.saveProducts()
        val options = this.saveOptions()
        val user = this.saveUser()
        val createCommand = CartCreateRequestCommand(
            userId = user.id!!,
            productId = options[0].productId,
            optionId = options[0].id!!,
            optionName = options[0].name,
            originalPrice = options[0].optionAdditionalPrice,
            calculatedPrice = 10000,
            orderQuantity = 20,
            deliveryInformation = DeliveryInformationVO(deliveryCost = 3000, expectedDate = LocalDate.now())
        )
        MockProductApi.setupGetOptionResponse(this.productWireMockServer)

        val createdCartItem = this.mockMvc.post(cartRequestURL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(createCommand)
        }.andExpect {
            status { HttpStatus.CREATED }
        }.andReturn()
            .response
            .contentAsString
        val createdCartItemId = objectMapper.readValue(createdCartItem, CartResponseView::class.java).id

        // when
        val updateQuantity = options[0].stock + 100
        val updateCommand = CartUpdateRequestCommand(orderQuantity = updateQuantity.toLong())
        val sut = this.mockMvc.patch("$cartRequestURL/{cartItemId}/quantity", createdCartItemId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(updateCommand)
        }.andExpect {
            status { isBadRequest() }
        }. andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is CartStockException)
    }

    private fun saveUser() =
        this.userRepository.save(
            User(
                userName = "user1",
                email = "user1@naver.com",
                password = "1234567",
                mobileVO = MobileVO(number = "01012341234")
            )
        )

    private fun saveProducts(): ArrayList<Product> {
        val productList = ArrayList<Product>()
        for (i in 1 until 3) {
            val product =
                Product(price = 1000 * i.toLong(), name = "product${i}", customerProductCode = "customerProductCode${i}")
            productList.add(this.productRepository.save(product))
        }
        return productList
    }

    private fun saveOptions(): List<Option> {
        val optionList = ArrayList<Option>()
        for (i in 1 until 3) {   // product
            for (j in 1 until 3) {   // option
                val option = Option(
                    productId = i.toLong(),
                    optionNo = "product${i} - option${j}",
                    name = "product${i} - option${j}",
                    optionAdditionalPrice = 1000 * j.toLong(),
                    stock = 200,
                    sort = j
                )
                optionList.add(this.optionRepository.save(option))
            }
        }
        return optionList
    }


}