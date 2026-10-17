
package com.example.s8131650assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8131650assignment2.data.repository.AppRepository
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Represents the possible Dashboard states.
 */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Success(
        val entities: List<Map<String, JsonElement>>,
        val total: Int
    ) : DashboardUiState

    data class Error(
        val message: String
    ) : DashboardUiState
}

/**
 * Retrieves dashboard entities using the keypass from login.
 * Keeps API operations separate from the Activity.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    fun loadDashboard(keypass: String) {
        if (keypass.isBlank()) {
            _uiState.value = DashboardUiState.Error(
                "Missing authentication key. Please log in again."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading

            try {
                // Use the API keypass, never a hardcoded topic name.
                val response = repository.getDashboard(keypass)

                _uiState.value = DashboardUiState.Success(
                    entities = response.entities,
                    total = response.entityTotal
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {
                _uiState.value = DashboardUiState.Error(
                    "Unable to load data (HTTP ${e.code()})."
                )

            } catch (e: IOException) {
                _uiState.value = DashboardUiState.Error(
                    "Cannot reach the server. Please retry."
                )

            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(
                    "Something went wrong while loading the dashboard."
                )
            }
        }
    }
}
