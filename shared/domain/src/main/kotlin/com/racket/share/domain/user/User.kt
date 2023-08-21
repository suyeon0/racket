package com.racket.share.domain.user

import com.racket.share.domain.user.enums.UserRoleType
import com.racket.share.domain.user.enums.UserStatusType
import com.racket.share.vo.AddressVO
import com.racket.share.vo.MobileVO
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
    var status: UserStatusType = UserStatusType.ACTIVE,

    @Enumerated(EnumType.STRING)
    var role: UserRoleType = UserRoleType.USER
) {
    fun updateStatus(status: UserStatusType): User {
        this.status = status
        return this
    }

    fun updateRole(role: UserRoleType): User {
        this.role = role
        return this
    }

    fun updateUserInfo(userName: String, password: String): User {
        this.userName = userName
        this.password = password
        return this
    }

    fun updateUserAdditionalInfo(addressVO: AddressVO?): User {
        this.addressVO = addressVO
        return this
    }

    // 유저가 삭제 상태인지 확인
    fun isDeletedStatus() = this.status == UserStatusType.DELETED


}