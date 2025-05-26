package com.ucb.data.plan

import com.ucb.domain.Plan

interface IPlanFlexRemoteDataSource {
    suspend fun fetchPlans(): List<Plan>
}