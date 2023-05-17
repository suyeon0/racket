package com.racket.api.user.domain

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    val id: Long? = null,

    var userName: String,

    val email: String,

    var password: String,

    var mobile: String,

    @Embedded
    var address: Address,

    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,

    @Enumerated(EnumType.STRING)
    var grade: UserGrade = UserGrade.USER
) {
    fun changeStatus(status:UserStatus) {
        this.status = status
    }

    fun changeGrade(grade: UserGrade) {
        this.grade = grade
    }

    fun updateUserInfo(userName: String, password: String, mobile: String, address: Address) {
        this.userName = userName
        this.password = password
        this.mobile = mobile
        this.address = address
    }

    fun delete() {
        this.status = UserStatus.DELETED
    }


}