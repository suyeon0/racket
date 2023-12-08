package com.racket.api.product.service

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.vo.ProductRegisterVO
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productBaseService: ProductBaseService,
    private val productDetailService: ProductDetailService
) : ProductService {

    override fun register(productRegisterVO: ProductRegisterVO) = this.productBaseService.register(productRegisterVO)

    override fun get(id: String) = this.productBaseService.get(id)

    override fun getList(cursorId: String?, page: Pageable) = this.productBaseService.getList(cursorId, page)

    override fun getDetail(id: String) = this.productDetailService.getProductDetail(id)

    override fun getCatalog(id: String) = this.productDetailService.getCatalog(id)

    override fun updateProductInfo(id: String, name: String, price: Long) =
        this.productBaseService.update(id = id, name = name, price = price)

    override fun updateOnlyStatus(id: String, status: ProductStatusType) =
        this.productBaseService.updateOnlyStatus(id = id, status = status)

}