
package com.example.s8131650assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.s8131650assignment2.data.repository.AppRepository

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
 * Represents the possible states of the Login screen.
 */
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val keypass: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

/**
 * Handles student login and manages UI state.
 * Uses Hilt to receive the repository automatically.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun login(studentId: String, firstName: String) {

        val username = studentId.trim()

        // Prevent repeated login requests while one is running.
        if (_uiState.value is LoginUiState.Loading) return

        // Student ID must contain digits only, without the 's'.
        if (username.isEmpty() || !username.all { it in '0'..'9' }) {
            _uiState.value = LoginUiState.Error(
                "Enter your student ID using numbers only."
            )
            return
        }

        if (firstName.isBlank()) {
            _uiState.value = LoginUiState.Error(
                "Please enter your first name."
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = LoginUiState.Loading

            try {
                // Password is case-sensitive, so keep it unchanged.
                val keypass = repository.login(
                    username,
                    firstName
                )

                _uiState.value =
                    LoginUiState.Success(keypass)

            } catch (e: CancellationException) {
                throw e

            } catch (e: HttpException) {
                _uiState.value = LoginUiState.Error(
                    "Login failed (HTTP ${e.code()}). Check your credentials."
                )

            } catch (e: IOException) {
                _uiState.value = LoginUiState.Error(
                    "Unable to reach the server. Please wait and try again."

                )

            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(
                    "Something went wrong. Please try again."
                )
            }
        }
    }
}
