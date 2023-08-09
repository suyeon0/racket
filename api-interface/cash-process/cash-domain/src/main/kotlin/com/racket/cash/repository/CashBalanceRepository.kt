package com.racket.cash.repository

import com.racket.cash.entity.CashBalance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CashBalanceRepository: CrudRepository<CashBalance, Long>