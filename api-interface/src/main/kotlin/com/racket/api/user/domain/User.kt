package com.racket.api.user.domain

import com.racket.api.common.vo.AddressVO
import com.racket.api.common.vo.MobileVO
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
    var mobileVO: MobileVO? = null,

    @Embedded
    var addressVO: AddressVO? = null,

    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER
) {
    fun updateStatus(status:UserStatus): User {
        this.status = status
        return this
    }

    fun updateRole(role: UserRole): User {
        this.role = role
        return this
    }

    fun updateUserInfo(userName: String, password: String): User {
        this.userName = userName
        this.password = password
        return this
    }

    fun updateUserAdditionalInfo(mobileVO: MobileVO?, addressVO: AddressVO?): User {
        this.mobileVO = mobileVO
        this.addressVO = addressVO
        return this
    }

    // 유저가 삭제 상태인지 확인
    fun isDeletedStatus() = this.status == UserStatus.DELETED


}