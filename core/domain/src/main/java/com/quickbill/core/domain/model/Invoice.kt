package com.quickbill.core.domain.model

data class Invoice(
    val id: Long = 0,
    val customerId: Long? = null,
    val customerName: String = "",
    val jobDescription: String = "",
    val lineItems: List<LineItem> = emptyList(),
    val total: Double = 0.0,
    val dueDate: Long? = null,
    val status: InvoiceStatus = InvoiceStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    val paidAt: Long? = null,
    val isQuote: Boolean = false
)

data class LineItem(
    val id: Long = 0,
    val description: String,
    val quantity: Double = 1.0,
    val unitPrice: Double,
    val amount: Double = quantity * unitPrice
)

enum class InvoiceStatus {
    DRAFT,
    SENT,
    OVERDUE,
    PAID,
    CANCELLED
}
