package com.racket.api.product

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.shared.vo.CursorResult
import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.domain.Product
import com.racket.api.product.domain.ProductRepository
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.product.option.reponse.OptionResponseView
import com.racket.api.product.response.ProductResponseView
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
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductControllerTest {

    val objectMapper = jacksonObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var optionRepository: OptionRepository

    private val cursorSize = 10

    private fun saveProduct(): Product {
        val savedProduct = Product(price = 10000, name = "TestProduct")
        return this.productRepository.save(savedProduct)
    }

    private fun saveProductAndOption(): Iterable<Option> {
        val savedProduct = this.saveProduct()
        val options = listOf(
            Option(
                product = savedProduct,
                optionNo = "Option 2",
                name = "Option 2",
                additionalPrice = 2000,
                stock = 200
            ),
            Option(
                product = savedProduct,
                optionNo = "Option 3",
                name = "Option 3",
                additionalPrice = 3000,
                stock = 300
            )
        )
        return optionRepository.saveAll(options)
    }


    @Test
    fun `Product Test - productId 로 상품 단건을 조회한다 조회정보가 없으면 상태코드 400, NotFountProductException 발생한다`() {
        // given
        this.saveProduct()

        // when
        val invalidProductId = 0
        val sut = this.mockMvc.get("/api/product/{product_id}", invalidProductId) {}
            .andExpect { status { isBadRequest() } }
            .andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is NotFoundProductException)
    }

    @Test
    fun `Product Test - productId 로 상품 단건을 조회한다 조회가 성공하면 200 상태코드와 상품정보를 리턴받는다`() {
        // given
        val savedProduct = this.saveProduct()

        // when
        val productId = savedProduct.id
        val sut = this.mockMvc.get("/api/product/{product_id}", productId) {}
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, ProductResponseView::class.java)
        Assertions.assertEquals(savedProduct.id, resultView.id)
    }

    @Test
    fun `Product Option Test - productId 로 옵션 리스트를 조회한다 조회정보가 없으면 상태코드 400, NotFountOptionException 발생한다 `() {
        // given
        val savedProduct = this.saveProduct()

        // when
        val productId = savedProduct.id
        val sut = this.mockMvc.get("/api/product/options/{productId}", productId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isBadRequest() } }
            .andReturn()

        // then
        val resolvedException = sut.resolvedException
        assert(resolvedException is NotFoundOptionException)
    }

    @Test
    fun `Product Option Test - productId 로 옵션 리스트를 조회한다 조회가 성공하면 200 상태코드와 리스트를 리턴받는다 옵션이름과 가격이 존재해야 한다`() {
        // given
        val options = this.saveProductAndOption()

        // when
        val productId = options.first().product.id
        val sut = this.mockMvc.get("/api/product/options/{productId}", productId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val responseJson = sut.response.contentAsString
        val objectMapper = jacksonObjectMapper()
        val responseList: List<OptionResponseView> =
            objectMapper.readValue(responseJson, object : TypeReference<List<OptionResponseView>>() {})
        Assertions.assertTrue(responseList.isNotEmpty())
        Assertions.assertTrue(responseList.size == options.count())
        Assertions.assertEquals(options.first().name, responseList[0].name)
        Assertions.assertEquals(options.first().price, responseList[0].price)
    }

    @Test
    fun `Product Option Test - optionId 로 옵션 조회한다 조회가 성공하면 200 상태코드와 옵션 정보를 리턴받는다`() {
        // given
        val options = this.saveProductAndOption()

        // when
        val optionId = options.first().id
        val sut = this.mockMvc.get("/api/product/option/{optionId}", optionId) {}
            .andExpect { status { isOk() } }
            .andReturn()

        // then
        val resultView = objectMapper.readValue(sut.response.contentAsString, OptionResponseView::class.java)
        Assertions.assertEquals(optionId, resultView.id)

    }

    @Test
    fun `Product Test - 상품 정보 리스트 조회시 페이징 처리되어 조회한다 최초 조회시(cursorId가 null) cursorSize 만큼 조회되어야 한다`() {
        // given
        for (i in 1..25) {
            productRepository.save(Product(price = 10000, name = "TestProduct"))
        }

        // when
        val sut = this.mockMvc.get("/api/product/list") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val productList: CursorResult<ProductResponseView> = objectMapper.readValue(
            sut.response.contentAsString,
            object : TypeReference<CursorResult<ProductResponseView>>() {}
        )

        Assertions.assertEquals(cursorSize, productList.values.size)
    }

    @Test
    fun `Product Test - 상품 리스트 조회시 조회결과물이 없는 경우 결과 리스트 사이즈는 0이고 마지막 페이지임을 리턴한다 - hasNext 가 False`() {
        // given-when
        val cursorId: Long? = null
        val sut = this.mockMvc.get("/api/product/list") {
            contentType = MediaType.APPLICATION_JSON
            content = cursorId
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val productList: CursorResult<ProductResponseView> = objectMapper.readValue(
            sut.response.contentAsString,
            object : TypeReference<CursorResult<ProductResponseView>>() {}
        )

        Assertions.assertEquals(0, productList.values.size)
        Assertions.assertFalse(productList.hasNext)
    }

    @Test
    fun `Product Test - 상품 리스트 조회시 cursorId 를 지정한 경우 결과 리스트 첫번째 인덱스의 상품 ID 는 cursorId - 1 값과 동일해야 한다`() {
        // given
        for (i in 1..30) {
            productRepository.save(Product(price = 10000, name = "TestProduct"))
        }

        // when
        val cursorId = 21
        val cursorSize = 10

        val sut = this.mockMvc.get("/api/product/list") {
            contentType = MediaType.APPLICATION_JSON
            param("cursorId", cursorId.toString())
        }.andExpect {
            status { isOk() }
        }.andReturn()

        // then
        val productList: CursorResult<ProductResponseView> = objectMapper.readValue(
            sut.response.contentAsString,
            object : TypeReference<CursorResult<ProductResponseView>>() {}
        )

        Assertions.assertEquals(cursorSize, productList.values.size)
        Assertions.assertEquals((cursorId - 1).toLong(), productList.values[0].id)
    }


}