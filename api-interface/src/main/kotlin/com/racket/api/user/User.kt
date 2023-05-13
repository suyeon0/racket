package com.racket.api.user

import javax.persistence.*

@Entity
@Table(name="users")
class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    var id: Long? = null
}