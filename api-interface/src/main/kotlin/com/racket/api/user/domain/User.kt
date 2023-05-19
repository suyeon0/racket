package com.racket.api.user.domain

import com.racket.api.common.vo.Address
import com.racket.api.common.vo.Mobile
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

    @Embedded
    var mobile: Mobile? = null,

    @Embedded
    var address: Address? = null,

    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,

    @Enumerated(EnumType.STRING)
    var grade: UserGrade = UserGrade.USER
) {
    fun updateStatus(status:UserStatus): User {
        this.status = status
        return this
    }

    fun updateGrade(grade: UserGrade): User {
        this.grade = grade
        return this
    }

    fun updateUserInfo(userName: String, password: String): User {
        this.userName = userName
        this.password = password
        return this
    }

    fun updateUserAdditionalInfo(mobile: Mobile?, address: Address?): User {
        this.mobile = mobile
        this.address = address
        return this
    }

    // 유저가 삭제 상태인지 확인
    fun isDeleted() = this.status == UserStatus.DELETED


}