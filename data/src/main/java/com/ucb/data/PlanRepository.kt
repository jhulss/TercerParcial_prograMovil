package com.ucb.data

import com.ucb.data.plan.IPlanFlexRemoteDataSource
import com.ucb.domain.Plan

class PlanRepository(
    val remoteDataSource: IPlanFlexRemoteDataSource
) {

    suspend fun getPlans(): List<Plan> {
        return this.remoteDataSource.fetchPlans()
    }
}