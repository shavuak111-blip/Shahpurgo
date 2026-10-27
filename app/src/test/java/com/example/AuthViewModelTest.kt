package com.example

import com.example.auth.AdminAuthManager
import com.example.auth.SessionStatus
import com.example.auth.UserSession
import com.example.auth.UserSessionManager
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthViewModelTest {

  @Test
  fun `verify AuthUiState default values`() {
    val state = AuthUiState()
    assertFalse(state.isLoading)
    assertNull(state.errorMessage)
    assertFalse(state.isSuccess)
    assertEquals(SessionStatus.INITIALIZING, state.session.status)
    assertEquals("Guest Customer", state.session.displayName)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `verify AuthViewModel initial state matches sessionManager`() = runTest {
    val sessionManager = UserSessionManager(authProvider = { null })
    val authManager = AdminAuthManager(authProvider = { null })
    val viewModel = AuthViewModel(sessionManager = sessionManager, authManager = authManager)

    advanceUntilIdle()

    val currentState = viewModel.uiState.value
    assertFalse(currentState.isLoading)
    assertNull(currentState.errorMessage)
    assertFalse(currentState.isSuccess)
    assertEquals(SessionStatus.UNAUTHENTICATED, currentState.session.status)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `verify sessionFlow updates AuthUiState reactively`() = runTest {
    val sessionManager = UserSessionManager(authProvider = { null })
    val authManager = AdminAuthManager(authProvider = { null })
    val viewModel = AuthViewModel(sessionManager = sessionManager, authManager = authManager)

    advanceUntilIdle()

    // Simulate authenticated session emission
    sessionManager.updateSession(
      uid = "user-12345",
      email = "customer@shahpurgo.com",
      displayName = "Aarav Sharma",
      photoUrl = "https://example.com/avatar.png"
    )

    advanceUntilIdle()

    val updatedState = viewModel.uiState.value
    assertTrue(updatedState.isSuccess)
    assertTrue(updatedState.session.isGoogleSignedIn)
    assertEquals("user-12345", updatedState.session.uid)
    assertEquals("customer@shahpurgo.com", updatedState.session.email)
    assertEquals("Aarav Sharma", updatedState.session.displayName)
    assertEquals("https://example.com/avatar.png", updatedState.session.photoUrl)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `verify clearError resets error state in AuthViewModel`() = runTest {
    val sessionManager = UserSessionManager(authProvider = { null })
    val authManager = AdminAuthManager(authProvider = { null })
    val viewModel = AuthViewModel(sessionManager = sessionManager, authManager = authManager)

    // Trigger clearError
    viewModel.clearError()
    assertNull(viewModel.uiState.value.errorMessage)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `verify signOut resets session and success flags`() = runTest {
    val sessionManager = UserSessionManager(authProvider = { null })
    val authManager = AdminAuthManager(authProvider = { null })
    val viewModel = AuthViewModel(sessionManager = sessionManager, authManager = authManager)

    sessionManager.updateSession(
      uid = "user-test-77",
      email = "test@shahpurgo.com",
      displayName = "Pooja Devi",
      photoUrl = null
    )
    advanceUntilIdle()
    assertTrue(viewModel.uiState.value.session.isGoogleSignedIn)

    var signedOutCallbackFired = false
    viewModel.signOut {
      signedOutCallbackFired = true
    }
    advanceUntilIdle()

    assertTrue(signedOutCallbackFired)
    val stateAfterSignOut = viewModel.uiState.value
    assertFalse(stateAfterSignOut.isLoading)
    assertFalse(stateAfterSignOut.isSuccess)
    assertFalse(stateAfterSignOut.session.isGoogleSignedIn)
    assertNull(stateAfterSignOut.session.uid)
    assertEquals("Guest Customer", stateAfterSignOut.session.displayName)
  }
}
