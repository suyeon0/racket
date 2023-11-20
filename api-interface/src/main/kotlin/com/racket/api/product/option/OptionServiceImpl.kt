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
    override fun getListByProductId(productId: String): List<OptionResponseView> {
        val optionList = optionRepository.findByProductId(productId)
        require(optionList.isNotEmpty()) { throw NotFoundOptionException() }

        val resultList = ArrayList<OptionResponseView>()
        for (option in optionList) {
            resultList.add(this.makeOptionResponseViewFromOption(option))
        }
        return resultList
    }

    override fun getByOptionId(optionId: String): OptionResponseView {
        val option = this.getOptionEntity(optionId)
        return this.makeOptionResponseViewFromOption(option)
    }

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

    override fun patchDisplayYn(id: String, displayYn: Boolean?): OptionResponseView {
        require(displayYn != null) { "displayYn must be not null" }
        val option: Option = this.optionRepository.findById(id).orElseThrow { NotFoundOptionException() }

        option.updateDisplayYn(displayYn)
        return this.makeOptionResponseViewFromOption(this.optionRepository.save(option))
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