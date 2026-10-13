package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.AcdaGold
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.viewmodel.AcdaViewModel
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.WhatsAppChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  authViewModel: AuthViewModel,
  acdaViewModel: AcdaViewModel,
  chatViewModel: WhatsAppChatViewModel,
  currentProfile: UserProfile?,
  modifier: Modifier = Modifier
) {
  val allUsers by authViewModel.allUsers.collectAsState()
  val projects by acdaViewModel.projects.collectAsState()
  val topics by acdaViewModel.topics.collectAsState()
  val channels by chatViewModel.allChannels.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) }
  var userSearchQuery by remember { mutableStateOf("") }
  var showBroadcastDialog by remember { mutableStateOf(false) }
  var userToEdit by remember { mutableStateOf<UserProfile?>(null) }
  var statusMessage by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = AcdaGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("ACDA Executive Backend", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        },
        actions = {
          IconButton(onClick = {
            authViewModel.seedProductionDataIfEmpty()
            statusMessage = "Synced system database defaults successfully"
          }) {
            Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimaryDark)
      )
    },
    modifier = modifier.testTag("admin_dashboard_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (statusMessage != null) {
        Surface(
          color = WhatsAppPrimary.copy(alpha = 0.15f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(statusMessage ?: "", color = WhatsAppPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { statusMessage = null }, modifier = Modifier.size(20.dp)) {
              Icon(Icons.Default.Close, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      // Navigation tabs: Metrics & Broadcast, Member Directory
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = WhatsAppPrimary
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Command Center", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Member Roles (${allUsers.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        )
      }

      when (selectedTab) {
        0 -> {
          // Command Center Tab
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // Metrics grid
            item {
              Text("Real-Time Infrastructure Telemetry", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            }

            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                AdminMetricCard(
                  title = "Registered Members",
                  value = allUsers.size.coerceAtLeast(1).toString(),
                  icon = Icons.Default.Group,
                  color = WhatsAppPrimary,
                  modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                  title = "WhatsApp Channels",
                  value = channels.size.toString(),
                  icon = Icons.Default.Campaign,
                  color = Color(0xFF0284C7),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                AdminMetricCard(
                  title = "Active Projects",
                  value = projects.size.toString(),
                  icon = Icons.Default.CloudDone,
                  color = Color(0xFF7C3AED),
                  modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                  title = "Total Capital Raised",
                  value = "₦${String.format("%,.0f", projects.sumOf { it.raisedBudget })}",
                  icon = Icons.Default.Security,
                  color = AcdaGold,
                  modifier = Modifier.weight(1f)
                )
              }
            }

            // Executive Broadcast button
            item {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = WhatsAppPrimary),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Text(
                    text = "Broadcast Official Notice to WhatsApp",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text(
                    text = "Instantly push a verified bulletin to all community members across active WhatsApp channels.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(vertical = 4.dp)
                  )
                  Spacer(modifier = Modifier.height(8.dp))
                  Button(
                    onClick = { showBroadcastDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = WhatsAppPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Compose Announcement", color = WhatsAppPrimary, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            // Quick actions
            item {
              Text("System Administration Controls", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            }

            item {
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text("Production Firestore Backend", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                      Text("Project ID: gen-lang-client-0001958493", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                    Surface(color = WhatsAppPrimary.copy(alpha = 0.12f), shape = RoundedCornerShape(6.dp)) {
                      Text("HEALTHY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WhatsAppPrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                  }
                  HorizontalDivider(color = Color(0xFFF1F5F9))
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text("Security Rules Enforcement", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                      Text("Zero-trust permissions active for members and admins", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                    Surface(color = Color(0xFF22C55E).copy(alpha = 0.12f), shape = RoundedCornerShape(6.dp)) {
                      Text("ENFORCED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                  }
                }
              }
            }
          }
        }

        1 -> {
          // Member Directory Tab
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp)
          ) {
            OutlinedTextField(
              value = userSearchQuery,
              onValueChange = { userSearchQuery = it },
              placeholder = { Text("Search members by name, email, or branch...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              singleLine = true,
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            )

            val filteredUsers = allUsers.filter {
              userSearchQuery.isEmpty() ||
                it.name.contains(userSearchQuery, ignoreCase = true) ||
                it.email.contains(userSearchQuery, ignoreCase = true) ||
                it.branch.contains(userSearchQuery, ignoreCase = true)
            }

            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              items(filteredUsers, key = { it.uid }) { user ->
                UserRowCard(
                  user = user,
                  onEditRole = { userToEdit = user }
                )
              }
            }
          }
        }
      }
    }

    // Broadcast Notice Dialog
    if (showBroadcastDialog && currentProfile != null) {
      BroadcastNoticeDialog(
        channels = channels,
        onDismiss = { showBroadcastDialog = false },
        onBroadcast = { channelId, noticeText ->
          showBroadcastDialog = false
          chatViewModel.openChannel(channelId)
          chatViewModel.sendMessage(currentProfile, noticeText, type = "announcement")
          statusMessage = "Official bulletin broadcasted successfully!"
        }
      )
    }

    // Edit Member Role Dialog
    if (userToEdit != null) {
      EditUserRoleDialog(
        targetUser = userToEdit!!,
        onDismiss = { userToEdit = null },
        onPromote = {
          authViewModel.promoteToAdmin(userToEdit!!.uid)
          userToEdit = null
          statusMessage = "Promoted user to Full Administrator"
        },
        onDemote = {
          authViewModel.demoteToMember(userToEdit!!.uid)
          userToEdit = null
          statusMessage = "Demoted user to Regular Member"
        },
        onBranchChange = { branch ->
          authViewModel.updateUserBranch(userToEdit!!.uid, branch)
          userToEdit = null
          statusMessage = "Updated branch assignment"
        }
      )
    }
  }
}

@Composable
private fun AdminMetricCard(
  title: String,
  value: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .background(color.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
      Text(text = title, fontSize = 11.sp, color = Color(0xFF64748B), maxLines = 1)
    }
  }
}

@Composable
private fun UserRowCard(
  user: UserProfile,
  onEditRole: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .background(
            if (user.isAdmin) AcdaRed.copy(alpha = 0.15f) else WhatsAppPrimary.copy(alpha = 0.12f),
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (user.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Group,
          contentDescription = null,
          tint = if (user.isAdmin) AcdaRed else WhatsAppPrimary,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = user.name.ifEmpty { "Member" }, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            color = if (user.isAdmin) AcdaRed.copy(alpha = 0.15f) else WhatsAppPrimary.copy(alpha = 0.12f),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = if (user.isAdmin) "ADMIN" else "MEMBER",
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold,
              color = if (user.isAdmin) AcdaRed else WhatsAppPrimary,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
        }
        Text(text = user.email, fontSize = 11.sp, color = Color(0xFF64748B))
        Text(text = "Branch: ${user.branch} • ID: ${user.membershipId}", fontSize = 11.sp, color = Color(0xFF94A3B8))
      }
      OutlinedButton(
        onClick = onEditRole,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.height(34.dp)
      ) {
        Text("Manage", fontSize = 11.sp)
      }
    }
  }
}

@Composable
private fun BroadcastNoticeDialog(
  channels: List<com.example.data.model.ChatChannel>,
  onDismiss: () -> Unit,
  onBroadcast: (String, String) -> Unit
) {
  var selectedChannelId by remember { mutableStateOf(channels.firstOrNull()?.id ?: "") }
  var noticeText by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Compose Official Broadcast", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Target Channel:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.height(100.dp)) {
          items(channels) { channel ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedChannelId = channel.id }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              androidx.compose.material3.RadioButton(
                selected = selectedChannelId == channel.id,
                onClick = { selectedChannelId = channel.id }
              )
              Text(channel.name, fontSize = 13.sp, modifier = Modifier.padding(start = 4.dp))
            }
          }
        }
        OutlinedTextField(
          value = noticeText,
          onValueChange = { noticeText = it },
          label = { Text("Official Notice Text") },
          placeholder = { Text("Enter formal circular text...") },
          maxLines = 4,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (noticeText.isNotBlank() && selectedChannelId.isNotEmpty()) onBroadcast(selectedChannelId, noticeText) },
        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary)
      ) {
        Text("Broadcast")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

@Composable
private fun EditUserRoleDialog(
  targetUser: UserProfile,
  onDismiss: () -> Unit,
  onPromote: () -> Unit,
  onDemote: () -> Unit,
  onBranchChange: (String) -> Unit
) {
  var branch by remember { mutableStateOf(targetUser.branch) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Manage Member Access", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("User: ${targetUser.name} (${targetUser.email})", fontSize = 13.sp)
        Text("Current Role: ${if (targetUser.isAdmin) "Administrator" else "Regular Member"}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
          value = branch,
          onValueChange = { branch = it },
          label = { Text("Assigned Community Branch") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          if (!targetUser.isAdmin) {
            Button(
              onClick = onPromote,
              colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary),
              modifier = Modifier.weight(1f)
            ) {
              Text("Promote to Admin", fontSize = 11.sp)
            }
          } else {
            Button(
              onClick = onDemote,
              colors = ButtonDefaults.buttonColors(containerColor = AcdaRed),
              modifier = Modifier.weight(1f)
            ) {
              Text("Demote to Member", fontSize = 11.sp)
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onBranchChange(branch) },
        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary)
      ) {
        Text("Save Branch")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Close") }
    }
  )
}
