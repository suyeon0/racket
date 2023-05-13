package com.racket.api.user

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class UserRepository(
    @PersistenceContext
    private val em: EntityManager
) {
    fun save(user: User) {
        em.persist(user)
    }
}


