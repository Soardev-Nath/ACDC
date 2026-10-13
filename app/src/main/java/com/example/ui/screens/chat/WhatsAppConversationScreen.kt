package com.example.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.ui.components.chat.WhatsAppBubble
import com.example.ui.theme.WhatsAppBackground
import com.example.ui.theme.WhatsAppLightGreen
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.viewmodel.WhatsAppChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppConversationScreen(
  viewModel: WhatsAppChatViewModel,
  userProfile: UserProfile?,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val channel by viewModel.activeChannel.collectAsState()
  val messages by viewModel.messages.collectAsState()
  val listState = rememberLazyListState()

  var inputText by remember { mutableStateOf("") }
  var messageToDelete by remember { mutableStateOf<ChatMessage?>(null) }

  // Auto scroll to bottom when new messages arrive
  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { /* Group Info */ }
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(WhatsAppPrimaryDark, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (channel?.isAnnouncement == true) Icons.Default.Campaign else if (channel?.isGroup == true) Icons.Default.Group else Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = channel?.name ?: "Chat",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = if (channel?.isAnnouncement == true) "Official Announcement Broadcast" else if (channel?.isGroup == true) "ACDA Group • ${messages.size} messages" else "online",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                maxLines = 1
              )
            }
          }
        },
        actions = {
          IconButton(onClick = { viewModel.startCall(channel?.name ?: "Contact", isVideo = true) }) {
            Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White)
          }
          IconButton(onClick = { viewModel.startCall(channel?.name ?: "Contact", isVideo = false) }) {
            Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = Color.White)
          }
          IconButton(onClick = { /* Menu */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
      )
    },
    modifier = modifier.testTag("whatsapp_conversation_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(WhatsAppBackground)
        .imePadding()
    ) {
      // Messages list
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp)
      ) {
        items(messages, key = { it.id }) { message ->
          val isOutgoing = message.senderId == userProfile?.uid
          val canDelete = isOutgoing || userProfile?.isAdmin == true

          WhatsAppBubble(
            message = message,
            isOutgoing = isOutgoing,
            canDelete = canDelete,
            onLongClick = { messageToDelete = message }
          )
        }
      }

      // WhatsApp Input Bar
      val canSendInAnnouncement = !(channel?.isAnnouncement == true && userProfile?.isAdmin != true)

      if (!canSendInAnnouncement) {
        Surface(
          color = Color(0xFFF1F5F9),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Only Administrators can send messages in this broadcast channel.",
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(12.dp)
          )
        }
      } else {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Rounded input pill
          Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.weight(1f)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp)
            ) {
              IconButton(onClick = { /* Emoji picker */ }, modifier = Modifier.size(36.dp)) {
                Icon(
                  imageVector = Icons.Default.Mood,
                  contentDescription = "Emoji",
                  tint = Color(0xFF8696A0),
                  modifier = Modifier.size(24.dp)
                )
              }

              OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Message", color = Color(0xFF8696A0), fontSize = 15.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color.Transparent,
                  unfocusedBorderColor = Color.Transparent
                ),
                maxLines = 4,
                modifier = Modifier
                  .weight(1f)
                  .testTag("chat_input_field")
              )

              IconButton(onClick = { /* Attachment */ }, modifier = Modifier.size(36.dp)) {
                Icon(
                  imageVector = Icons.Default.AttachFile,
                  contentDescription = "Attach",
                  tint = Color(0xFF8696A0),
                  modifier = Modifier.size(22.dp)
                )
              }

              if (inputText.isBlank()) {
                IconButton(onClick = { /* Camera */ }, modifier = Modifier.size(36.dp)) {
                  Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color(0xFF8696A0),
                    modifier = Modifier.size(22.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Green Circle Action button (Send or Mic)
          val isSend = inputText.isNotBlank()
          Box(
            modifier = Modifier
              .size(48.dp)
              .background(WhatsAppPrimary, CircleShape)
              .clickable {
                if (isSend && userProfile != null) {
                  viewModel.sendMessage(userProfile, inputText)
                  inputText = ""
                }
              }
              .testTag("chat_send_button"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isSend) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
              contentDescription = if (isSend) "Send" else "Voice Note",
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }
    }

    // Message delete / moderation dialog
    if (messageToDelete != null) {
      AlertDialog(
        onDismissRequest = { messageToDelete = null },
        title = { Text("Delete / Moderate Message") },
        text = {
          Text(
            if (userProfile?.isAdmin == true && messageToDelete?.senderId != userProfile.uid) {
              "As an Administrator, you can remove this message from the channel for all community members."
            } else {
              "Are you sure you want to delete this message?"
            }
          )
        },
        confirmButton = {
          TextButton(
            onClick = {
              messageToDelete?.id?.let { viewModel.deleteMessage(it) }
              messageToDelete = null
            }
          ) {
            Text("Delete", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { messageToDelete = null }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}
