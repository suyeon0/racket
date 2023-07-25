package com.racket.shared.notification.domain

import org.springframework.web.multipart.MultipartFile
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("email")
class EmailNotification(

    private var emailAddress: String,
    private var title: String,
    private var file: String?

) : Notification() {

    constructor(
        receiver: Receiver,
        message: String,
        emailAddress: String,
        title: String,
        file: String?
    ) : this(emailAddress, title, file) {
        this.receiver = receiver
        this.message = message
    }

    override fun toString(): String {
        return "EmailNotification(emailAddress='$emailAddress', title='$title', file=$file)"
    }

}