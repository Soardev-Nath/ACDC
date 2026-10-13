package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppLightGreen
import com.example.ui.theme.WhatsAppPrimary

enum class NavigationTab(val label: String, val icon: ImageVector) {
  OVERVIEW("Overview", Icons.Default.Home),
  CHATS("Chats", Icons.Default.Chat),
  PROJECTS("Projects", Icons.Default.Build),
  TOWN_HALL("Town Hall", Icons.Default.Forum),
  ADMIN("Admin", Icons.Default.AdminPanelSettings),
  PROFILE("ID Card", Icons.Default.Badge)
}

@Composable
fun BottomNavBar(
  selectedTab: NavigationTab,
  onTabSelected: (NavigationTab) -> Unit,
  unreadChatCount: Int = 0,
  isAdmin: Boolean = false,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 8.dp,
    modifier = modifier.testTag("bottom_nav_bar")
  ) {
    NavigationTab.values().forEach { tab ->
      // Show Admin tab to administrators or everyone as an entry to the management panel
      val isSelected = selectedTab == tab
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Box {
            Icon(
              imageVector = tab.icon,
              contentDescription = tab.label,
              tint = if (isSelected) WhatsAppPrimary else Color(0xFF64748B)
            )
            if (tab == NavigationTab.CHATS && unreadChatCount > 0) {
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .offset(x = 6.dp, y = (-4).dp)
                  .size(16.dp)
                  .background(WhatsAppLightGreen, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = if (unreadChatCount > 9) "9+" else unreadChatCount.toString(),
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        },
        label = {
          Text(
            text = tab.label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) WhatsAppPrimary else Color(0xFF64748B),
            maxLines = 1
          )
        },
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = WhatsAppPrimary.copy(alpha = 0.12f)
        ),
        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
      )
    }
  }
}
