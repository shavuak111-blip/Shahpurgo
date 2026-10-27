package com.example.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AdminAuthManager
import com.example.auth.UserSession
import com.example.auth.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for Authentication operations in ShahpurGo.
 */
data class AuthUiState(
  val session: UserSession = UserSession(),
  val isLoading: Boolean = false,
  val errorMessage: String? = null,
  val isSuccess: Boolean = false
)

/**
 * AuthViewModel drives Google Sign-In and user authentication state management.
 * Integrates with [UserSessionManager] to ensure the UI updates instantly
 * across customer, merchant, and super admin interfaces.
 */
class AuthViewModel(
  val sessionManager: UserSessionManager = UserSessionManager(),
  val authManager: AdminAuthManager = AdminAuthManager()
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    AuthUiState(
      session = sessionManager.currentSession,
      isSuccess = sessionManager.currentSession.isGoogleSignedIn
    )
  )
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

  init {
    // Observe session transitions and maintain UI reactivity
    sessionManager.sessionFlow
      .onEach { session ->
        _uiState.update { current ->
          current.copy(
            session = session,
            isSuccess = session.isGoogleSignedIn
          )
        }
      }
      .launchIn(viewModelScope)
  }

  /**
   * Initiates Google Sign-In using Android Jetpack CredentialManager.
   * On success, updates the session and fires [onSuccess].
   */
  fun signInWithGoogle(context: Context, onSuccess: () -> Unit = {}) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null) }
      try {
        val result = authManager.signInWithGoogle(context)
        result.onSuccess { user ->
          Log.d(TAG, "Google Sign-In successful for UID: ${user.uid}")
          sessionManager.updateSession(user)
          _uiState.update {
            it.copy(
              isLoading = false,
              errorMessage = null,
              isSuccess = true,
              session = sessionManager.currentSession
            )
          }
          onSuccess()
        }.onFailure { ex ->
          val msg = when (ex) {
            is GetCredentialCancellationException -> null // Silent user dismiss
            else -> {
              Log.w(TAG, "Google Sign-In error: ${ex.message}")
              ex.localizedMessage ?: "Unable to complete Google Sign-In. Please try again."
            }
          }
          _uiState.update {
            it.copy(
              isLoading = false,
              errorMessage = msg,
              isSuccess = false
            )
          }
        }
      } catch (t: Throwable) {
        Log.e(TAG, "Unexpected error during Google Sign-In", t)
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = t.localizedMessage ?: "An unexpected error occurred. Please try again.",
            isSuccess = false
          )
        }
      }
    }
  }

  /**
   * Signs the active user out of Firebase and resets session state to Guest.
   */
  fun signOut(onSignedOut: () -> Unit = {}) {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        authManager.signOut()
        sessionManager.updateSession(null)
        _uiState.update {
          it.copy(
            isLoading = false,
            isSuccess = false,
            errorMessage = null,
            session = sessionManager.currentSession
          )
        }
        onSignedOut()
      } catch (e: Exception) {
        Log.e(TAG, "Error during sign out", e)
        _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
      }
    }
  }

  /**
   * Dismisses any active error notification banner in the UI.
   */
  fun clearError() {
    _uiState.update { it.copy(errorMessage = null) }
  }

  /**
   * Manually refreshes session details directly from Firebase Auth servers.
   */
  fun refreshProfile() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }
      try {
        val refreshed = sessionManager.refreshProfile()
        _uiState.update {
          it.copy(
            isLoading = false,
            session = refreshed
          )
        }
      } catch (e: Exception) {
        Log.w(TAG, "Failed refreshing profile: ${e.message}")
        _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  companion object {
    private const val TAG = "AuthViewModel"
  }
}
