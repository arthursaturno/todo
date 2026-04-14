package com.example.mylist.feature.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylist.core.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun login() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(state.email, state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                }
                .onFailure { e ->
                    val message = e.message ?: "Erro ao fazer login"
                    when {
                        message.contains("E-mail") ->
                            _uiState.update { it.copy(isLoading = false, emailError = message) }
                        message.contains("Senha") ->
                            _uiState.update { it.copy(isLoading = false, passwordError = message) }
                        else ->
                            _uiState.update { it.copy(isLoading = false, error = message) }
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
