package com.racket.share.domain.cash.repository

import com.racket.share.domain.cash.entity.CashBalance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CashBalanceRepository: CrudRepository<CashBalance, Long>