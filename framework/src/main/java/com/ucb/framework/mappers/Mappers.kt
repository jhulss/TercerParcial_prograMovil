package com.ucb.framework.mappers

import com.ucb.domain.Feature
import com.ucb.domain.Plan
import com.ucb.framework.dto.FeatureDto
import com.ucb.framework.dto.PlanFlexDto

fun PlanFlexDto.toDomain(): Plan = Plan(
    name        = nombre,
    priceBefore = precioAnterior,
    priceNow    = precioAhora,
    bandwidth   = anchoBanda,
    features    = features.map { it.toDomain() }
)
fun FeatureDto.toDomain(): Feature = Feature(
    description = descripcion
)