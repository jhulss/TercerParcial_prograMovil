package com.ucb.ucbtest.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.Plan
import com.ucb.usecases.GetPlans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanUiState(
    val plans: List<Plan> = emptyList(),
    val currentIndex: Int = 0
)

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val getPlansUseCase: GetPlans
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val list = getPlansUseCase()
            _uiState.value = _uiState.value.copy(plans = list)
        }
    }

    fun nextPlan() {
        _uiState.value = _uiState.value.copy(
            currentIndex = (_uiState.value.currentIndex + 1).coerceAtMost(_uiState.value.plans.lastIndex),
        )
    }

    fun previousPlan() {
        _uiState.value = _uiState.value.copy(
            currentIndex = (_uiState.value.currentIndex - 1).coerceAtLeast(0),
        )
    }
}