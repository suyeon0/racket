package com.racket.delivery.common.enums

import org.springframework.core.convert.converter.Converter

class DeliveryCompanyTypeEnumConverter: Converter<String, DeliveryCompanyType> {

    override fun convert(source: String) = DeliveryCompanyType.valueOf(source.uppercase())
}