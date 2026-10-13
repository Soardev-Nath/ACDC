package com.example.ui.screens.chat

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
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatChannel
import com.example.data.model.UserProfile
import com.example.ui.components.chat.WhatsAppBlueChecks
import com.example.ui.theme.WhatsAppLightGreen
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.ui.theme.WhatsAppTimestamp
import com.example.viewmodel.ChatFilter
import com.example.viewmodel.WhatsAppChatViewModel
import com.example.viewmodel.WhatsAppTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppChatListScreen(
  viewModel: WhatsAppChatViewModel,
  userProfile: UserProfile?,
  onChannelClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val activeTab by viewModel.activeTab.collectAsState()
  val activeFilter by viewModel.activeFilter.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val channels by viewModel.filteredChannels.collectAsState()
  val totalUnread by viewModel.totalUnreadCount.collectAsState()

  var isSearching by remember { mutableStateOf(false) }
  var showMenu by remember { mutableStateOf(false) }
  var showNewChannelDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      Column {
        TopAppBar(
          title = {
            if (isSearching) {
              OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search chats...", color = Color.White.copy(alpha = 0.7f)) },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("whatsapp_search_input")
              )
            } else {
              Text(
                text = "ACDA WhatsApp",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          },
          actions = {
            if (isSearching) {
              IconButton(onClick = {
                isSearching = false
                viewModel.setSearchQuery("")
              }) {
                Icon(Icons.Default.Close, contentDescription = "Close Search", tint = Color.White)
              }
            } else {
              IconButton(onClick = { /* Simulated Camera */ }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White)
              }
              IconButton(onClick = { isSearching = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
              }
              IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
              }
              DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
              ) {
                DropdownMenuItem(
                  text = { Text("New Community Group") },
                  onClick = {
                    showMenu = false
                    showNewChannelDialog = true
                  }
                )
                if (userProfile?.isAdmin == true) {
                  DropdownMenuItem(
                    text = { Text("Broadcast Official Circular") },
                    onClick = {
                      showMenu = false
                      showNewChannelDialog = true
                    }
                  )
                }
              }
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
        )

        // WhatsApp Tabs: CHATS, STATUS, CALLS
        TabRow(
          selectedTabIndex = activeTab.ordinal,
          containerColor = WhatsAppPrimary,
          contentColor = Color.White,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab.ordinal]),
              color = Color.White,
              height = 3.dp
            )
          }
        ) {
          WhatsAppTab.values().forEach { tab ->
            Tab(
              selected = activeTab == tab,
              onClick = { viewModel.setTab(tab) },
              text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = tab.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (activeTab == tab) Color.White else Color.White.copy(alpha = 0.7f)
                  )
                  if (tab == WhatsAppTab.CHATS && totalUnread > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                      modifier = Modifier
                        .size(18.dp)
                        .background(Color.White, CircleShape),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = totalUnread.toString(),
                        color = WhatsAppPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }
              }
            )
          }
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showNewChannelDialog = true },
        containerColor = WhatsAppLightGreen,
        contentColor = Color.White,
        shape = CircleShape,
        modifier = Modifier.testTag("whatsapp_new_chat_fab")
      ) {
        Icon(
          imageVector = when (activeTab) {
            WhatsAppTab.CHATS -> Icons.Default.AddComment
            WhatsAppTab.STATUS -> Icons.Default.CameraAlt
            WhatsAppTab.CALLS -> Icons.Default.Call
          },
          contentDescription = "Action"
        )
      }
    },
    modifier = modifier.testTag("whatsapp_chat_list_screen")
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (activeTab) {
        WhatsAppTab.CHATS -> {
          Column(modifier = Modifier.fillMaxSize()) {
            // Filter Chips
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ChatFilter.values().forEach { filter ->
                val isSelected = activeFilter == filter
                FilterChip(
                  selected = isSelected,
                  onClick = { viewModel.setFilter(filter) },
                  label = {
                    Text(
                      text = filter.name.lowercase().replaceFirstChar { it.uppercase() },
                      fontSize = 12.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WhatsAppPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = WhatsAppPrimary
                  )
                )
              }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            if (channels.isEmpty()) {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(32.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "No channels found.\nTap '+' to start a new ACDA group or conversation.",
                  color = Color(0xFF64748B),
                  fontSize = 14.sp,
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            } else {
              LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(channels, key = { it.id }) { channel ->
                  ChannelItemRow(
                    channel = channel,
                    onClick = { onChannelClick(channel.id) }
                  )
                  HorizontalDivider(
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.padding(start = 72.dp)
                  )
                }
              }
            }
          }
        }

        WhatsAppTab.STATUS -> {
          StatusTabContent(
            userProfile = userProfile,
            onViewStory = { author, title -> viewModel.viewStory(author, title) }
          )
        }

        WhatsAppTab.CALLS -> {
          CallsTabContent(
            onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) }
          )
        }
      }
    }

    if (showNewChannelDialog) {
      NewChannelDialog(
        userProfile = userProfile,
        onDismiss = { showNewChannelDialog = false },
        onCreate = { name, desc, isGroup, isAnnounce ->
          showNewChannelDialog = false
          if (userProfile != null) {
            viewModel.createNewChannel(name, desc, isGroup, isAnnounce, userProfile)
          }
        }
      )
    }
  }
}

@Composable
private fun ChannelItemRow(
  channel: ChatChannel,
  onClick: () -> Unit
) {
  val formattedTime = channel.lastMessageTime?.toDate()?.let {
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(it)
  } ?: "Just now"

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Channel Avatar
    Box(
      modifier = Modifier
        .size(48.dp)
        .background(
          if (channel.isAnnouncement) WhatsAppPrimary else WhatsAppPrimaryDark,
          CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (channel.isAnnouncement) Icons.Default.Campaign else if (channel.isGroup) Icons.Default.Group else Icons.Default.Person,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = channel.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
        Text(
          text = formattedTime,
          fontSize = 11.sp,
          color = if (channel.unreadCount > 0) WhatsAppLightGreen else WhatsAppTimestamp,
          fontWeight = if (channel.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
        )
      }

      Spacer(modifier = Modifier.height(2.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          if (channel.unreadCount == 0 && channel.lastMessage.isNotEmpty()) {
            WhatsAppBlueChecks()
            Spacer(modifier = Modifier.width(4.dp))
          }
          Text(
            text = channel.lastMessage.ifEmpty { "No messages yet" },
            fontSize = 13.sp,
            color = if (channel.unreadCount > 0) Color(0xFF1E293B) else Color(0xFF64748B),
            fontWeight = if (channel.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        if (channel.unreadCount > 0) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(20.dp)
              .background(WhatsAppLightGreen, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = channel.unreadCount.toString(),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatusTabContent(
  userProfile: UserProfile?,
  onViewStory: (String, String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // My Status
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .background(Color(0xFFE2E8F0), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(30.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "My Status", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          Text(text = "Tap to add status update", fontSize = 13.sp, color = Color(0xFF64748B))
        }
      }
    }

    item {
      Text(
        text = "Recent Updates",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B)
      )
    }

    val sampleStatuses = listOf(
      Triple("National President", "All arrangements concluded for the Q4 Central Assembly.", "Today, 9:30 AM"),
      Triple("Udege Youth Leader", "Solar borehole civil works 80% completed. Water flow tests next week!", "Today, 8:15 AM"),
      Triple("ACDA Women Wing", "Annual Empowerment & Cooperative Grants distribution starting Friday.", "Yesterday, 6:40 PM")
    )

    items(sampleStatuses) { (author, text, time) ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onViewStory(author, text) },
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .background(WhatsAppLightGreen, CircleShape)
            .padding(2.dp)
            .background(WhatsAppPrimaryDark, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Group, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(text = author, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          Text(text = time, fontSize = 12.sp, color = Color(0xFF64748B))
        }
      }
    }
  }
}

@Composable
private fun CallsTabContent(
  onStartCall: (String, Boolean) -> Unit
) {
  val sampleCalls = listOf(
    Triple("National President", "Today, 11:20 AM", true),
    Triple("Secretariat Helpline", "Yesterday, 4:15 PM", false),
    Triple("Youth Wing Coordinator", "10 Oct, 7:00 PM", true)
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(sampleCalls) { (name, time, isVideo) ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .background(WhatsAppPrimaryDark, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(text = name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CallReceived, contentDescription = null, tint = WhatsAppLightGreen, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = time, fontSize = 12.sp, color = Color(0xFF64748B))
          }
        }
        IconButton(onClick = { onStartCall(name, isVideo) }) {
          Icon(
            imageVector = if (isVideo) Icons.Default.Videocam else Icons.Default.Call,
            contentDescription = "Call",
            tint = WhatsAppPrimary
          )
        }
      }
    }
  }
}

@Composable
private fun NewChannelDialog(
  userProfile: UserProfile?,
  onDismiss: () -> Unit,
  onCreate: (String, String, Boolean, Boolean) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  var isGroup by remember { mutableStateOf(true) }
  var isAnnounce by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Create New Channel / Group", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Channel / Group Name") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Purpose / Description") },
          modifier = Modifier.fillMaxWidth()
        )
        if (userProfile?.isAdmin == true) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { isAnnounce = !isAnnounce }
          ) {
            androidx.compose.material3.Checkbox(
              checked = isAnnounce,
              onCheckedChange = { isAnnounce = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Official Announcement Channel (Admin Only Posts)", fontSize = 12.sp)
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { if (name.isNotBlank()) onCreate(name, desc, isGroup, isAnnounce) },
        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary)
      ) {
        Text("Create")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
