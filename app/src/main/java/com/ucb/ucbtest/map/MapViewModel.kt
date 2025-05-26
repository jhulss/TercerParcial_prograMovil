package com.ucb.ucbtest.map

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val selectedLatLng: LatLng? = null,
    val phone: String = "",
    val showConfirmation: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val userMessage: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    // Coordenadas por defecto (Cochabamba, Bolivia)
    private val defaultLocation = LatLng(-17.3814, -66.15826)

    init {
        loadInitialLocation()
    }

    private fun loadInitialLocation() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                if (hasLocationPermission()) {
                    fetchDeviceLocation()
                } else {
                    setDefaultLocation()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al obtener ubicación",
                        selectedLatLng = defaultLocation
                    )
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchDeviceLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    _uiState.update {
                        it.copy(
                            selectedLatLng = userLatLng,
                            isLoading = false,
                            userMessage = "Ubicación actual obtenida"
                        )
                    }
                } else {
                    setDefaultLocation()
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        selectedLatLng = defaultLocation,
                        isLoading = false,
                        errorMessage = "No se pudo obtener la ubicación: ${e.localizedMessage}"
                    )
                }
            }
    }

    private fun setDefaultLocation() {
        _uiState.update {
            it.copy(
                selectedLatLng = defaultLocation,
                isLoading = false,
                userMessage = "Usando ubicación por defecto"
            )
        }
    }

    fun onMapClick(latLng: LatLng) {
        _uiState.update {
            it.copy(
                selectedLatLng = latLng,
                userMessage = "Ubicación seleccionada: ${latLng.latitude}, ${latLng.longitude}"
            )
        }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    fun onSend() {
        _uiState.update {
            it.copy(
                showConfirmation = true,
                userMessage = "Confirmando envío de ubicación"
            )
        }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(showConfirmation = false) }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onUserMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}