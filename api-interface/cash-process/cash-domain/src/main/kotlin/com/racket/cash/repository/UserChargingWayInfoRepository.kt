package com.racket.cash.repository

import com.racket.cash.entity.UserChargingWayInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserChargingWayInfoRepository: CrudRepository<UserChargingWayInfo, Long>