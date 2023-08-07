package com.racket.api.cache.domain

import com.racket.api.cache.domain.entity.UserChargingWayInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserChargingWayInfoRepository: CrudRepository<UserChargingWayInfo, Long>