package com.example.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
  private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
  companion object {
    private const val TAG = "AuthRepository"
  }

  val currentUser: FirebaseUser?
    get() = auth.currentUser

  fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      trySend(firebaseAuth.currentUser)
    }
    auth.addAuthStateListener(listener)
    awaitClose { auth.removeAuthStateListener(listener) }
  }

  suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
    return try {
      val credentialManager = CredentialManager.create(context)
      val serverClientId = context.getString(R.string.default_web_client_id)

      val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId)
        .build()

      val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

      val result = credentialManager.getCredential(
        request = request,
        context = context
      )

      val credential = result.credential
      if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCredential.idToken

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(firebaseCredential).await()
        val user = authResult.user ?: error("Firebase sign-in returned null user")
        Result.success(user)
      } else {
        Result.failure(IllegalStateException("Unexpected credential type returned"))
      }
    } catch (e: GetCredentialCancellationException) {
      Log.d(TAG, "User cancelled Google Sign-In prompt")
      Result.failure(e)
    } catch (e: Exception) {
      Log.e(TAG, "Google Sign-In failed", e)
      Result.failure(e)
    }
  }

  fun signOut() {
    auth.signOut()
  }
}
