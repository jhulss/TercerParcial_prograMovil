package com.ucb.domain

data class Plan(
    val name: String,
    val priceBefore: String,
    val priceNow: String,
    val bandwidth: String,
    val features: List<Feature>,
)