package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.UserProfile
import com.example.data.repository.AuthRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.ProjectRepository
import com.example.data.repository.TownHallRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
  private val authRepository: AuthRepository = AuthRepository(),
  private val chatRepository: ChatRepository = ChatRepository(),
  private val projectRepository: ProjectRepository = ProjectRepository(),
  private val townHallRepository: TownHallRepository = TownHallRepository()
) : ViewModel() {

  val currentUserProfile: StateFlow<UserProfile?> = authRepository.currentUserProfile

  val allUsers: StateFlow<List<UserProfile>> = authRepository.getAllUsers()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

  fun clearError() {
    _errorMessage.value = null
  }

  fun signInWithGoogle(context: Context, webClientId: String) {
    viewModelScope.launch {
      _isLoading.value = true
      _errorMessage.value = null
      val result = authRepository.signInWithGoogle(context, webClientId)
      result.fold(
        onSuccess = { user ->
          _isLoading.value = false
          // Trigger seeding of production default data if empty
          seedProductionDataIfEmpty()
        },
        onFailure = { error ->
          _isLoading.value = false
          _errorMessage.value = error.localizedMessage ?: "Google Sign-In failed"
        }
      )
    }
  }

  fun signOut() {
    authRepository.signOut()
  }

  fun seedProductionDataIfEmpty() {
    viewModelScope.launch {
      val profile = currentUserProfile.value ?: return@launch
      try {
        chatRepository.seedDefaultChannelsIfEmpty(profile)
        projectRepository.seedDefaultProjectsIfEmpty(profile)
        townHallRepository.seedDefaultTopicsIfEmpty(profile)
      } catch (_: Exception) {
        // Ignored or rules prevented non-admin seeding
      }
    }
  }

  fun promoteToAdmin(targetUserId: String) {
    viewModelScope.launch {
      try {
        authRepository.updateUserRole(targetUserId, "admin")
      } catch (e: Exception) {
        _errorMessage.value = "Failed to promote user: ${e.message}"
      }
    }
  }

  fun demoteToMember(targetUserId: String) {
    viewModelScope.launch {
      try {
        authRepository.updateUserRole(targetUserId, "member")
      } catch (e: Exception) {
        _errorMessage.value = "Failed to update role: ${e.message}"
      }
    }
  }

  fun updateUserBranch(targetUserId: String, branch: String) {
    viewModelScope.launch {
      try {
        authRepository.updateUserBranch(targetUserId, branch)
      } catch (e: Exception) {
        _errorMessage.value = "Failed to update branch: ${e.message}"
      }
    }
  }
}
