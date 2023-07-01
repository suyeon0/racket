package com.racket.api.product.option

import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.option.reponse.OptionResponseView
import org.springframework.stereotype.Service


@Service
class OptionServiceImpl(
    val optionRepository: OptionRepository
) : OptionService {
    override fun getListByProductId(productId: Long): List<OptionResponseView> {
        val optionList = optionRepository.findByProductId(productId)
        require(optionList.isNotEmpty()) { throw NotFoundOptionException() }

        val resultList = ArrayList<OptionResponseView>()
        for(option in optionList) {
            resultList.add(this.makeOptionResponseViewFromOption(option))
        }
        return resultList
    }

    override fun getByOptionId(optionId: Long): OptionResponseView {
        val option = this.getOptionEntity(optionId)
        return this.makeOptionResponseViewFromOption(option)
    }

    private fun getOptionEntity(id: Long) = this.optionRepository.findById(id).orElseThrow{ NotFoundOptionException() }

    private fun makeOptionResponseViewFromOption(option: Option) =
        OptionResponseView(
            id = option.id!!,
            optionNo = option.optionNo,
            productId = option.product.id!!,
            name = option.name,
            price = option.price,
            additionalPrice = option.additionalPrice,
            stock = option.stock,
            remark = option.remark
        )
}