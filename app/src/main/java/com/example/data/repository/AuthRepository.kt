package com.example.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.model.UserProfile
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
  private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

  private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
  val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

  private var profileListener: ListenerRegistration? = null

  init {
    auth.addAuthStateListener { firebaseAuth ->
      val user = firebaseAuth.currentUser
      if (user != null) {
        listenToUserProfile(user)
      } else {
        profileListener?.remove()
        profileListener = null
        _currentUserProfile.value = null
      }
    }
  }

  val currentFirebaseUser: FirebaseUser?
    get() = auth.currentUser

  val currentUserId: String?
    get() = auth.currentUser?.uid

  private fun listenToUserProfile(user: FirebaseUser) {
    profileListener?.remove()
    val docRef = firestore.collection("users").document(user.uid)
    profileListener = docRef.addSnapshotListener { snapshot, _ ->
      if (snapshot != null && snapshot.exists()) {
        val profile = snapshot.toObject(UserProfile::class.java)
        _currentUserProfile.value = profile
      } else {
        // Create initial profile in Firestore
        val isAdminUser = user.email.equals("nathanielrop84@gmail.com", ignoreCase = true)
        val initialProfile = UserProfile(
          uid = user.uid,
          name = user.displayName ?: "Community Member",
          email = user.email ?: "",
          role = if (isAdminUser) "admin" else "member",
          avatarUrl = user.photoUrl?.toString() ?: "",
          about = if (isAdminUser) "ACDA Community Administrator" else "Active Member",
          phone = user.phoneNumber ?: "",
          membershipId = "ACDA-${user.uid.take(6).uppercase()}",
          branch = "National Headquarters",
          createdAt = Timestamp.now(),
          updatedAt = Timestamp.now()
        )
        docRef.set(initialProfile)
        _currentUserProfile.value = initialProfile
      }
    }
  }

  suspend fun signInWithGoogle(context: Context, webClientId: String): Result<FirebaseUser> {
    return try {
      val credentialManager = CredentialManager.create(context)
      val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId)
        .build()

      val request = GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()

      val result = credentialManager.getCredential(context = context, request = request)
      val credential = result.credential

      if (credential is androidx.credentials.CustomCredential &&
          credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        val authResult = auth.signInWithCredential(authCredential).await()
        val user = authResult.user ?: throw IllegalStateException("Firebase user was null after sign in")
        Result.success(user)
      } else {
        Result.failure(IllegalArgumentException("Unexpected credential type returned"))
      }
    } catch (e: GetCredentialException) {
      Result.failure(e)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  fun signOut() {
    profileListener?.remove()
    profileListener = null
    _currentUserProfile.value = null
    auth.signOut()
  }

  // Admin User Management
  fun getAllUsers(): Flow<List<UserProfile>> = callbackFlow {
    val listener = firestore.collection("users")
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }
        val users = snapshot?.documents?.mapNotNull { it.toObject(UserProfile::class.java) } ?: emptyList()
        trySend(users)
      }
    awaitClose { listener.remove() }
  }

  suspend fun updateUserRole(targetUserId: String, newRole: String) {
    firestore.collection("users").document(targetUserId)
      .update(mapOf("role" to newRole, "updatedAt" to Timestamp.now()))
      .await()
  }

  suspend fun updateUserBranch(targetUserId: String, newBranch: String) {
    firestore.collection("users").document(targetUserId)
      .update(mapOf("branch" to newBranch, "updatedAt" to Timestamp.now()))
      .await()
  }
}
