package com.example

import com.example.auth.SessionStatus
import com.example.auth.UserSession
import com.example.auth.UserSessionManager
import com.example.model.Shop
import com.example.viewmodel.GroceryViewModel
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
class UserSessionManagerTest {

  @Test
  fun `verify UserSession default values`() {
    val defaultSession = UserSession()
    assertEquals(SessionStatus.INITIALIZING, defaultSession.status)
    assertNull(defaultSession.uid)
    assertEquals("Guest Customer", defaultSession.displayName)
    assertNull(defaultSession.email)
    assertNull(defaultSession.photoUrl)
    assertFalse(defaultSession.isGoogleSignedIn)
    assertFalse(defaultSession.isPlatformOwner)
    assertFalse(defaultSession.isStoreAdmin)
    assertEquals("GC", defaultSession.initials)
    assertEquals("Guest", defaultSession.displayRole)
  }

  @Test
  fun `verify UserSession initials derivation`() {
    val session1 = UserSession(displayName = "Amit Sharma")
    assertEquals("AS", session1.initials)

    val session2 = UserSession(displayName = "Raman")
    assertEquals("RA", session2.initials)

    val session3 = UserSession(displayName = "")
    assertEquals("SG", session3.initials)
  }

  @Test
  fun `verify updateSession with null user sets unauthenticated state`() {
    val sessionManager = UserSessionManager(authProvider = { null })
    sessionManager.updateSession(null)

    val session = sessionManager.currentSession
    assertEquals(SessionStatus.UNAUTHENTICATED, session.status)
    assertNull(session.uid)
    assertFalse(session.isGoogleSignedIn)
    assertFalse(session.isPlatformOwner)
    assertFalse(session.isStoreAdmin)
  }

  @Test
  fun `verify updateSession for customer sets authenticated details`() {
    val sessionManager = UserSessionManager(authProvider = { null })
    sessionManager.updateSession(
      uid = "cust_123",
      email = "kavita@example.com",
      displayName = "Kavita Thakur",
      photoUrl = null
    )
    val session = sessionManager.currentSession

    assertEquals(SessionStatus.AUTHENTICATED, session.status)
    assertEquals("cust_123", session.uid)
    assertEquals("Kavita Thakur", session.displayName)
    assertEquals("kavita@example.com", session.email)
    assertTrue(session.isGoogleSignedIn)
    assertFalse(session.isPlatformOwner)
    assertEquals("KT", session.initials)
    assertEquals("Verified Customer", session.displayRole)
  }

  @Test
  fun `verify updateSession for platform owner sets super admin role`() {
    val sessionManager = UserSessionManager(authProvider = { null })
    sessionManager.updateSession(
      uid = "owner_admin_999",
      email = Shop.PLATFORM_OWNER_EMAIL, // shavuak111@gmail.com
      displayName = "Shavu Owner",
      photoUrl = "https://example.com/avatar.jpg"
    )
    val session = sessionManager.currentSession

    assertEquals(SessionStatus.AUTHENTICATED, session.status)
    assertEquals("owner_admin_999", session.uid)
    assertEquals("shavuak111@gmail.com", session.email)
    assertTrue(session.isGoogleSignedIn)
    assertTrue(session.isPlatformOwner)
    assertTrue(session.isStoreAdmin)
    assertEquals("shahpur-central-hub", session.assignedShopId)
    assertEquals("Platform Super Admin", session.displayRole)
  }

  @Test
  fun `verify ViewModel synchronizes UserSession updates into UI state`() {
    val viewModel = GroceryViewModel()
    assertNotNull(viewModel.userSessionManager)

    viewModel.userSessionManager.updateSession(
      uid = "test_vm_user_456",
      email = "customer@shahpurgo.in",
      displayName = "Test Customer",
      photoUrl = null
    )

    val currentUiSession = viewModel.uiState.value.userSession
    assertEquals("test_vm_user_456", currentUiSession.uid)
    assertEquals("Test Customer", currentUiSession.displayName)
    assertEquals("customer@shahpurgo.in", currentUiSession.email)
    assertTrue(currentUiSession.isGoogleSignedIn)
  }
}
