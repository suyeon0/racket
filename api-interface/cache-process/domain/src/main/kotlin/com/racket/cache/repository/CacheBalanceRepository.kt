package com.racket.cache.repository

import com.racket.cache.entity.CacheBalance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CacheBalanceRepository: CrudRepository<CacheBalance, Long>