package com.racket.api.cash

import com.racket.share.domain.cash.exception.InsertCashTransactionException
import com.racket.api.cash.response.CashTransactionResponseView
import com.racket.api.cash.vo.ChargeVO
import com.racket.api.cash.vo.makeCashTransactionEntity
import com.racket.share.domain.cash.entity.CashTransaction
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.NotFoundCashTransactionException
import com.racket.share.domain.cash.repository.CashTransactionRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CashTransactionLogServiceImpl(
    private val cashTransactionRepository: CashTransactionRepository
) : CashTransactionLogService {

    override fun getTransactionListByTransactionId(transactionId: String): List<CashTransactionResponseView> {
        val transactionList = this.cashTransactionRepository.findAllByTransactionId(transactionId)

        return transactionList.stream().map { transaction ->
            CashTransactionResponseView(
                id = transaction.id!!,
                userId = transaction.userId,
                amount = transaction.amount,
                transactionId = transaction.transactionId,
                accountId = transaction.accountId,
                status = transaction.status,
                eventType = transaction.eventType
            )
        }.toList()
    }

    override fun insertChargeTransaction(chargeVO: ChargeVO): CashTransaction {
        return try {
            if (chargeVO.status == CashTransactionStatusType.REQUEST) {
                chargeVO.transactionId = UUID.randomUUID().toString()
            }
            this.cashTransactionRepository.save(chargeVO.makeCashTransactionEntity())
        } catch (e: Exception) {
            throw InsertCashTransactionException()
        }
    }

    override fun getTransactionById(eventId: ObjectId): CashTransactionResponseView {
        val transaction = this.cashTransactionRepository.findById(eventId)
            .orElseThrow { NotFoundCashTransactionException() }
        return CashTransactionResponseView(
            id = transaction.id!!,
            userId = transaction.userId,
            amount = transaction.amount,
            transactionId = transaction.transactionId,
            accountId = transaction.accountId,
            status = transaction.status,
            eventType = transaction.eventType
        )
    }


}