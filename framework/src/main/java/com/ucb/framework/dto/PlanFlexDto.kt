package com.ucb.framework.dto


data class PlanFlexDto(
    val nombre: String,
    val precioAnterior: String,
    val precioAhora: String,
    val anchoBanda: String,
    val features: List<FeatureDto>
)