package com.racket.api.cache.domain

import com.racket.api.cache.domain.entity.CacheBalance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CacheBalanceRepository: CrudRepository<CacheBalance, Long>