package com.racket.api.product.service

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.response.ProductResponseView
import com.racket.api.product.vo.ProductCursorResultVO
import com.racket.api.product.vo.ProductRegisterVO
import org.springframework.data.domain.Pageable

interface ProductBaseService {

    /**
     * 상품 등록
     */
    fun register(productRegisterDTO: ProductRegisterVO): ProductResponseView

    /**
     * 상품 정보 수정
     */
    fun update(id: String, name: String, price: Long): ProductResponseView

    /**
     * 상품 상태 수정
     */
    fun updateOnlyStatus(id: String, status: ProductStatusType): ProductResponseView

    /**
     * 상품 단건 조회
     */
    fun get(id: String): ProductResponseView

    /**
     * 상품 리스트 조회
     */
    fun getList(cursorId: String?, page: Pageable): ProductCursorResultVO

}