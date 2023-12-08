package com.racket.api.product.service

import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.response.ProductDetailResponseView
import com.racket.api.product.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.product.vo.ProductRegisterVO
import org.springframework.data.domain.Pageable

interface ProductService {

    /**
     * 상품 등록
     */
    fun register(productRegisterVO: ProductRegisterVO): ProductResponseView

    /**
     * 상품 단건 조회
     */
    fun get(id: String): ProductResponseView

    /**
     * 상품 리스트 조회
     */
    fun getList(cursorId: String?, page: Pageable): ProductCursorResultVO

    /**
     * 상품 상세 조회
     */
    fun getDetail(id: String): ProductDetailResponseView

    /**
     * 상품 카탈로그 조회
     */
    fun getCatalog(id: String): ProductCatalogResponseView

    /**
     * 상품 수정
     */
    fun updateProductInfo(id: String, name: String, price: Long): ProductResponseView

    /**
     * 상품 상태 수정
     */
    fun updateOnlyStatus(id: String, status: ProductStatusType): ProductResponseView

}