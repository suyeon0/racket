package com.racket.cash

import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.exception.InsertCashTransactionException
import com.racket.cash.exception.NotFoundCashTransactionException
import com.racket.cash.repository.CashTransactionRepository
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.vo.ChargeVO
import com.racket.cash.vo.makeCashTransactionEntity
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CashTransactionLogServiceImpl(
    private val cashTransactionRepository: CashTransactionRepository
) : CashTransactionLogService {

    override fun getTransactionListByTransactionId(transactionId: ObjectId): List<CashTransactionResponseView> {
        val transactionList = this.cashTransactionRepository.findAllByTransactionId(transactionId)

        return transactionList.stream().map { transaction ->
            CashTransactionResponseView(
                id = transaction.id!!,
                userId = transaction.userId,
                amount = transaction.amount,
                createdAt = transaction.id!!.date,
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
                chargeVO.transactionId = ObjectId()
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
            createdAt = transaction.id!!.date,
            transactionId = transaction.transactionId,
            accountId = transaction.accountId,
            status = transaction.status,
            eventType = transaction.eventType
        )
    }


}