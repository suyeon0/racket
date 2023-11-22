package com.racket.api.product.catalog

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.racket.api.product.vo.ProductCatalogContents
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ProductCatalogVOConverter : AttributeConverter<ProductCatalogContents, String> {

    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: ProductCatalogContents?): String? {
        return attribute?.let { objectMapper.writeValueAsString(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): ProductCatalogContents? {
        return dbData?.let { objectMapper.readValue(it, ProductCatalogContents::class.java) }
    }
}