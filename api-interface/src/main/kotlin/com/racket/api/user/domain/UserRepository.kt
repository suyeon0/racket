package com.racket.api.user.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    fun findByEmailAndPassword(email: String, password: String): Optional<User>
}


