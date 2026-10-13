package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NavigationTab
import com.example.ui.dialogs.WhatsAppCallDialog
import com.example.ui.dialogs.WhatsAppStoryViewerDialog
import com.example.ui.screens.acda.AcdaHomeScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.SignInScreen
import com.example.ui.screens.chat.WhatsAppChatListScreen
import com.example.ui.screens.chat.WhatsAppConversationScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.townhall.TownHallScreen
import com.example.ui.theme.AcdaTheme
import com.example.viewmodel.AcdaViewModel
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.WhatsAppChatViewModel

@Composable
fun MainApp(
  authViewModel: AuthViewModel = viewModel(),
  chatViewModel: WhatsAppChatViewModel = viewModel(),
  acdaViewModel: AcdaViewModel = viewModel()
) {
  AcdaTheme {
    val currentUserProfile by authViewModel.currentUserProfile.collectAsState()
    val activeChannelId by chatViewModel.activeChannelId.collectAsState()
    val totalUnread by chatViewModel.totalUnreadCount.collectAsState()
    val activeCall by chatViewModel.activeCall.collectAsState()
    val activeStory by chatViewModel.activeStory.collectAsState()

    var currentTab by remember { mutableStateOf(NavigationTab.OVERVIEW) }

    // Auth gate: if not signed in, show SignInScreen
    if (currentUserProfile == null) {
      SignInScreen(viewModel = authViewModel)
    } else {
      // Back navigation handling
      BackHandler(enabled = activeChannelId != null || currentTab != NavigationTab.OVERVIEW) {
        if (activeChannelId != null) {
          chatViewModel.closeChannel()
        } else if (currentTab != NavigationTab.OVERVIEW) {
          currentTab = NavigationTab.OVERVIEW
        }
      }

      val isInsideConversation = activeChannelId != null

      Scaffold(
        bottomBar = {
          if (!isInsideConversation) {
            BottomNavBar(
              selectedTab = currentTab,
              onTabSelected = { currentTab = it },
              unreadChatCount = totalUnread,
              isAdmin = currentUserProfile?.isAdmin == true
            )
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(if (isInsideConversation) androidx.compose.foundation.layout.PaddingValues() else innerPadding)
        ) {
          if (isInsideConversation) {
            WhatsAppConversationScreen(
              viewModel = chatViewModel,
              userProfile = currentUserProfile,
              onBack = { chatViewModel.closeChannel() }
            )
          } else {
            when (currentTab) {
              NavigationTab.OVERVIEW -> {
                AcdaHomeScreen(
                  userProfile = currentUserProfile,
                  acdaViewModel = acdaViewModel,
                  chatViewModel = chatViewModel,
                  onNavigateToChat = { currentTab = NavigationTab.CHATS },
                  onNavigateToProjects = { currentTab = NavigationTab.PROJECTS },
                  onNavigateToTownHall = { currentTab = NavigationTab.TOWN_HALL },
                  onNavigateToAdmin = { currentTab = NavigationTab.ADMIN },
                  onNavigateToProfile = { currentTab = NavigationTab.PROFILE }
                )
              }

              NavigationTab.CHATS -> {
                WhatsAppChatListScreen(
                  viewModel = chatViewModel,
                  userProfile = currentUserProfile,
                  onChannelClick = { channelId ->
                    chatViewModel.openChannel(channelId)
                  }
                )
              }

              NavigationTab.PROJECTS -> {
                ProjectsScreen(
                  viewModel = acdaViewModel,
                  userProfile = currentUserProfile
                )
              }

              NavigationTab.TOWN_HALL -> {
                TownHallScreen(
                  viewModel = acdaViewModel,
                  userProfile = currentUserProfile
                )
              }

              NavigationTab.ADMIN -> {
                AdminDashboardScreen(
                  authViewModel = authViewModel,
                  acdaViewModel = acdaViewModel,
                  chatViewModel = chatViewModel,
                  currentProfile = currentUserProfile
                )
              }

              NavigationTab.PROFILE -> {
                ProfileScreen(
                  userProfile = currentUserProfile,
                  authViewModel = authViewModel
                )
              }
            }
          }
        }
      }

      // Dialogs overlays
      activeCall?.let { call ->
        WhatsAppCallDialog(
          contactName = call.contactName,
          isVideo = call.isVideo,
          onEndCall = { chatViewModel.endCall() }
        )
      }

      activeStory?.let { story ->
        WhatsAppStoryViewerDialog(
          authorName = story.authorName,
          title = story.title,
          timeAgo = story.timeAgo,
          onClose = { chatViewModel.closeStory() }
        )
      }
    }
  }
}
