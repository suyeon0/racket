package com.racket.api.auth.login.session.domain

import org.springframework.data.repository.CrudRepository

interface SessionRedisRepository: CrudRepository<SessionUser, String> {
}