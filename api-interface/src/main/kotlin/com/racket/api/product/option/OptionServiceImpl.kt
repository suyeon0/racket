package com.racket.api.product.option

import com.racket.api.product.domain.Option
import com.racket.api.product.domain.OptionRepository
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.option.response.OptionResponseView
import com.racket.api.product.option.response.OptionWithProductView
import com.racket.api.product.service.ProductBaseService
import com.racket.api.product.service.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OptionServiceImpl(
    private val productBaseService: ProductBaseService,
    private val optionRepository: OptionRepository
) : OptionService {
    override fun getOptionList(productId: String): List<OptionResponseView> {
        val optionList = optionRepository.findByProductIdOrderBySortAscPriceAsc(productId)
        require(optionList.isNotEmpty()) { throw NotFoundOptionException() }

        val resultList = ArrayList<OptionResponseView>()
        for (option in optionList) {
            resultList.add(this.makeOptionResponseViewFromOption(option))
        }
        return resultList
    }

    override fun getById(optionId: String) = this.makeOptionResponseViewFromOption(this.getOptionEntity(optionId))

    @Transactional
    override fun addOption(optionCreateDTO: OptionService.OptionCreateDTO): OptionResponseView {
        val option = this.optionRepository.save(
            Option(
                id = optionCreateDTO.id,
                productId = optionCreateDTO.productId!!,
                name = optionCreateDTO.name!!,
                sort = optionCreateDTO.sort!!,
                price = optionCreateDTO.price!!,
                stock = optionCreateDTO.stock!!,
                status = optionCreateDTO.status!!,
                description = optionCreateDTO.description!!,
                displayYn = optionCreateDTO.displayYn!!
            )
        )
        return this.makeOptionResponseViewFromOption(option)
    }

    @Transactional
    override fun patchOption(id: String, optionUpdateDTO: OptionService.OptionUpdateDTO): OptionResponseView {
        val option: Option = this.optionRepository.findById(id).orElseThrow { NotFoundOptionException() }
        option.updateInfo(
            productId = optionUpdateDTO.productId!!,
            name = optionUpdateDTO.name!!,
            sort = optionUpdateDTO.sort!!,
            price = optionUpdateDTO.price!!,
            stock = optionUpdateDTO.stock!!,
            status = optionUpdateDTO.status!!,
            description = optionUpdateDTO.description!!,
            displayYn = optionUpdateDTO.displayYn!!
        )
        return this.makeOptionResponseViewFromOption(this.optionRepository.save(option))
    }

    @Transactional
    override fun patchDisplayYn(id: String, displayYn: Boolean?): OptionResponseView {
        require(displayYn != null) { "displayYn must be not null" }
        val option: Option = this.optionRepository.findById(id).orElseThrow { NotFoundOptionException() }

        option.updateDisplayYn(displayYn)
        return this.makeOptionResponseViewFromOption(this.optionRepository.save(option))
    }

    override fun getOptionWithProductView(optionId: String, productId: String): OptionWithProductView {
        val product = this.productBaseService.get(productId)
        val option = this.makeOptionResponseViewFromOption(this.optionRepository.findById(optionId).get())
        return OptionWithProductView.of(product, option)
    }

    private fun getOptionEntity(id: String) = this.optionRepository.findById(id).orElseThrow { NotFoundOptionException() }

    private fun makeOptionResponseViewFromOption(option: Option) =
        OptionResponseView(
            id = option.id,
            productId = option.productId,
            name = option.name,
            price = option.price,
            stock = option.stock,
            description = option.description,
            sort = option.sort,
            displayYn = option.displayYn
        )
}