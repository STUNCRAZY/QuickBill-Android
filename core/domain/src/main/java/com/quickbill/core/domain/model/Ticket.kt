package com.quickbill.core.domain.model

/**
 * Lightweight view of an unpaid invoice for the hanging ticket rail.
 */
data class Ticket(
    val id: Long,
    val invoiceId: Long,
    val customerName: String,
    val jobDescription: String,
    val amount: Double,
    val daysOutstanding: Int,
    val isOverdue: Boolean = daysOutstanding > 14,
    val createdAt: Long
)
