package com.racket.cache.repository

import com.racket.cache.entity.UserChargingWayInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserChargingWayInfoRepository: CrudRepository<UserChargingWayInfo, Long>