package com.racket.cart.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.cart.api.client.delivery.DeliveryClient
import com.racket.cart.api.client.product.ProductClient
import com.racket.cart.api.request.CartCreateRequestCommand
import com.racket.cart.api.response.CartResponseView
import com.racket.cart.api.vo.DeliveryInformationVO
import com.racket.share.domain.user.User
import com.racket.share.domain.user.UserRepository
import com.racket.share.vo.MobileVO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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
    lateinit var userRepository: UserRepository

    companion object {
        const val cartRequestURL = "/api/v1/cart"
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
    fun `Cart Test - 장바구니에 아이템을 넣는다 장바구니를 조회하면 200 성공과 아이템이 조회된다`() {
        // given
        val products = this.saveProducts()
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

        // when
        val sut = this.mockMvc.post("${cartRequestURL}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.registerModule(JavaTimeModule()).writeValueAsString(createCommand)
        }.andExpect {
            status { isCreated() }
        }.andReturn()


    }

    private fun saveUser(): User {
        val user = User(
            userName = "user1",
            email = "user1@naver.com",
            password = "1234567",
            mobileVO = MobileVO(number = "01012341234")
        )
        return this.userRepository.save(user)
    }

    private fun saveProducts(): ArrayList<Product> {
        val productList = ArrayList<Product>()
        for (i in 0 until 3) {
            val product = Product(price = 1000 * i.toLong(), name = "product${i}", customerProductCode = "customerProductCode${i}")
            productList.add(this.productRepository.save(product))
        }
        return productList
    }

    private fun saveOptions(): List<Option> {
        val optionList = ArrayList<Option>()
        for(i in 0 until 3) {   // product
            for(j in 0 until 3) {   // option
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