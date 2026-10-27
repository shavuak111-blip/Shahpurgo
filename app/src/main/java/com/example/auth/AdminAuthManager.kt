package com.example.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AdminAuthManager(
  private val authProvider: () -> FirebaseAuth? = {
    try {
      Firebase.auth
    } catch (e: Exception) {
      null
    }
  }
) {
  val currentUser: FirebaseUser?
    get() = authProvider()?.currentUser

  val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
    val auth = authProvider()
    if (auth == null) {
      trySend(null)
      awaitClose { }
      return@callbackFlow
    }
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      trySend(firebaseAuth.currentUser)
    }
    auth.addAuthStateListener(listener)
    awaitClose { auth.removeAuthStateListener(listener) }
  }

  suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
    val auth = authProvider() ?: return Result.failure(IllegalStateException("Firebase Auth is not available"))
    return try {
      val webClientId = context.getString(R.string.default_web_client_id)
      val credentialManager = CredentialManager.create(context)

      val googleIdOption = GetSignInWithGoogleOption.Builder(webClientId)
        .build()

      val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

      val response = credentialManager.getCredential(context, request)
      val credential = response.credential

      if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken
        val authCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(authCredential).await()
        val user = authResult.user
        if (user != null) {
          Log.d(TAG, "Admin Google Sign-In successful for: ${user.email}")
          Result.success(user)
        } else {
          Result.failure(Exception("Firebase user is null after sign in"))
        }
      } else {
        Result.failure(Exception("Unsupported credential type: ${credential.type}"))
      }
    } catch (e: GetCredentialCancellationException) {
      Log.i(TAG, "Admin sign-in was cancelled by user")
      Result.failure(e)
    } catch (e: Exception) {
      Log.e(TAG, "Admin Google sign-in failed", e)
      Result.failure(e)
    }
  }

  fun signOut() {
    authProvider()?.signOut()
    Log.d(TAG, "Admin signed out")
  }

  companion object {
    private const val TAG = "AdminAuthManager"
  }
}
