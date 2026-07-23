package com.quickbill.core.domain.model

data class Material(
    val id: Long = 0,
    val projectCategory: ProjectCategory,
    val name: String,
    val description: String = "",
    val unitPrice: Double,
    val unit: String = "ea",
    val imageUrl: String? = null
)

enum class ProjectCategory {
    FRAMING,
    GAZEBO,
    PERGOLA,
    HVAC,
    FENCE,
    DECK,
    OTHER
}
