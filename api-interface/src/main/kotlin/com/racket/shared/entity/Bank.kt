package com.racket.shared.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "bank")
class Bank (

    @Id @GeneratedValue
    val id: Long,

    val name: String

)