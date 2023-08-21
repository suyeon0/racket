package com.racket.share.domain.payment

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface AccountPaymentRepository: CrudRepository<AccountPayment, Long> {
    fun findAllByUserIdOrderById(userId: Long): Optional<List<AccountPayment>>
}