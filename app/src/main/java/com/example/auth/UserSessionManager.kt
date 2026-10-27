package com.example.auth

import android.util.Log
import com.example.model.Shop
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * Represents the active session status of the user in ShahpurGo.
 */
enum class SessionStatus {
  INITIALIZING,
  AUTHENTICATED,
  UNAUTHENTICATED
}

/**
 * Immutable snapshot of the current user session, profile details, and role permissions.
 */
data class UserSession(
  val status: SessionStatus = SessionStatus.INITIALIZING,
  val uid: String? = null,
  val displayName: String = "Guest Customer",
  val email: String? = null,
  val photoUrl: String? = null,
  val isGoogleSignedIn: Boolean = false,
  val isPlatformOwner: Boolean = false,
  val isStoreAdmin: Boolean = false,
  val assignedShopId: String? = null
) {
  val initials: String
    get() {
      val parts = displayName.trim().split(" ").filter { it.isNotBlank() }
      return when {
        parts.size >= 2 -> "${parts[0].first().uppercase()}${parts[1].first().uppercase()}"
        parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
        else -> "SG"
      }
    }

  val displayRole: String
    get() = when {
      isPlatformOwner -> "Platform Super Admin"
      isStoreAdmin -> "Store Partner Admin"
      isGoogleSignedIn -> "Verified Customer"
      else -> "Guest"
    }
}

/**
 * UserSessionManager listens to FirebaseAuth state changes via [FirebaseAuth.AuthStateListener]
 * and automatically publishes updated [UserSession] snapshots through a [StateFlow] to drive UI reactivity.
 */
class UserSessionManager(
  private val authProvider: () -> FirebaseAuth? = {
    try {
      Firebase.auth
    } catch (e: Exception) {
      null
    }
  }
) {
  private val _sessionFlow = MutableStateFlow(UserSession())
  val sessionFlow: StateFlow<UserSession> = _sessionFlow.asStateFlow()

  val currentSession: UserSession
    get() = _sessionFlow.value

  val firebaseUser: FirebaseUser?
    get() = authProvider()?.currentUser

  private var authListener: FirebaseAuth.AuthStateListener? = null

  init {
    startListening()
  }

  fun startListening() {
    val auth = authProvider()
    if (auth == null) {
      _sessionFlow.value = UserSession(status = SessionStatus.UNAUTHENTICATED)
      return
    }

    // Capture initial state from currentUser if already authenticated
    updateSession(auth.currentUser)

    authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      Log.d(TAG, "FirebaseAuth AuthStateListener triggered: user=${user?.email ?: "null"}")
      updateSession(user)
    }
    auth.addAuthStateListener(authListener!!)
  }

  fun stopListening() {
    authListener?.let {
      authProvider()?.removeAuthStateListener(it)
      authListener = null
    }
  }

  /**
   * Translates a [FirebaseUser] instance into an immutable [UserSession] model.
   * Can be invoked directly in unit tests to verify session transformation.
   */
  fun updateSession(
    uid: String?,
    email: String?,
    displayName: String?,
    photoUrl: String?
  ) {
    if (uid != null) {
      val cleanEmail = email?.trim()
      val isOwner = cleanEmail?.equals(Shop.PLATFORM_OWNER_EMAIL, ignoreCase = true) == true
      val matchingShop = Shop.defaultShops.firstOrNull { it.adminEmail.equals(cleanEmail, ignoreCase = true) }
      val isStoreAdmin = isOwner || matchingShop != null
      val assignedShop = if (isOwner) "shahpur-central-hub" else matchingShop?.id

      val cleanName = displayName?.trim()?.ifBlank { null }
        ?: cleanEmail?.substringBefore("@")
        ?: "ShahpurGo Customer"

      _sessionFlow.value = UserSession(
        status = SessionStatus.AUTHENTICATED,
        uid = uid,
        displayName = cleanName,
        email = cleanEmail,
        photoUrl = photoUrl,
        isGoogleSignedIn = true,
        isPlatformOwner = isOwner,
        isStoreAdmin = isStoreAdmin,
        assignedShopId = assignedShop
      )
    } else {
      _sessionFlow.value = UserSession(
        status = SessionStatus.UNAUTHENTICATED,
        uid = null,
        displayName = "Guest Customer",
        email = null,
        photoUrl = null,
        isGoogleSignedIn = false,
        isPlatformOwner = false,
        isStoreAdmin = false,
        assignedShopId = null
      )
    }
  }

  fun updateSession(user: FirebaseUser?) {
    if (user != null) {
      updateSession(
        uid = user.uid,
        email = user.email,
        displayName = user.displayName,
        photoUrl = user.photoUrl?.toString()
      )
    } else {
      updateSession(null, null, null, null)
    }
  }

  /**
   * Refreshes user profile data directly from Firebase Auth servers.
   */
  suspend fun refreshProfile(): UserSession {
    val auth = authProvider()
    val user = auth?.currentUser
    if (user != null) {
      try {
        user.reload().await()
      } catch (e: Exception) {
        Log.w(TAG, "Notice reloading user profile: ${e.message}")
      }
      updateSession(auth.currentUser)
    } else {
      updateSession(null)
    }
    return _sessionFlow.value
  }

  companion object {
    private const val TAG = "UserSessionManager"
  }
}
