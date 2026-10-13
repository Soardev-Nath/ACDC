package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ReservationInfo
import com.example.ui.components.ChatHeader
import com.example.ui.components.ChatInputBar
import com.example.ui.components.MessageBubble
import com.example.ui.components.PinnedMeetupBanner
import com.example.ui.components.QuickReplyRow
import com.example.ui.components.TypingIndicator
import com.example.ui.dialogs.AttachOptionsSheet
import com.example.ui.dialogs.ElenaProfileSheet
import com.example.ui.dialogs.GlazeInspectionDialog
import com.example.ui.dialogs.MeetupDetailsBottomSheet
import com.example.ui.dialogs.StudioCallDialog
import com.example.ui.dialogs.StudioPassBottomSheet
import com.example.ui.theme.WarmOnSurfaceVariant
import com.example.ui.theme.WarmSurface
import com.example.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  viewModel: ChatViewModel = viewModel(),
  onNavigateBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val messages by viewModel.messages.collectAsState()
  val isTyping by viewModel.isElenaTyping.collectAsState()
  val inputText by viewModel.inputText.collectAsState()
  val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()
  val audioProgress by viewModel.audioPlayProgress.collectAsState()
  val meetupInfo by viewModel.meetupInfo.collectAsState()
  val selectedReservation by viewModel.selectedReservation.collectAsState()
  val glazeRecipe by viewModel.glazeRecipe.collectAsState()

  val listState = rememberLazyListState()

  // Modals state
  var showMeetupDetails by remember { mutableStateOf(false) }
  var showGlazeDialog by remember { mutableStateOf(false) }
  var showAttachSheet by remember { mutableStateOf(false) }
  var showProfileSheet by remember { mutableStateOf(false) }
  var showCallDialog by remember { mutableStateOf(false) }

  val meetupSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val passSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val attachSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  LaunchedEffect(messages.size, isTyping) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size)
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .statusBarsPadding()
      .imePadding(),
    containerColor = WarmSurface
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // 1. Top App Header
      ChatHeader(
        onBackClick = onNavigateBack,
        onCalendarClick = { showMeetupDetails = true },
        onCallClick = { showCallDialog = true },
        onViewStudioLocation = {
          Toast.makeText(context, "Earth & Fire Studio: 428 Kiln Way, Arts District", Toast.LENGTH_LONG).show()
        },
        onViewElenaProfile = { showProfileSheet = true }
      )

      // 2. Pinned Shared Meetup Banner
      PinnedMeetupBanner(
        meetup = meetupInfo,
        onDetailsClick = { showMeetupDetails = true }
      )

      Spacer(modifier = Modifier.height(4.dp))

      // 3. Chat Messages Stream
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .testTag("chat_messages_list")
      ) {
        // Date separator pill: "Today, Nov 20"
        item(key = "date_separator") {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFEFE8E3))
                .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Text(
                text = "Today, Nov 20",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 12.sp
                ),
                color = WarmOnSurfaceVariant
              )
            }
          }
        }

        // List of chat bubbles
        items(
          items = messages,
          key = { it.id }
        ) { message ->
          MessageBubble(
            message = message,
            isAudioPlaying = isPlayingAudio,
            audioPlayProgress = audioProgress,
            onToggleAudio = { viewModel.toggleAudio() },
            onInspectGlaze = { showGlazeDialog = true },
            onViewStudioPass = { res -> viewModel.setSelectedReservation(res) }
          )
        }

        // Animated typing indicator
        if (isTyping) {
          item(key = "typing_indicator") {
            TypingIndicator()
          }
        }

        item {
          Spacer(modifier = Modifier.height(6.dp))
        }
      }

      // 4. Quick Reply Action Chips
      QuickReplyRow(
        onReplySelected = { text ->
          viewModel.sendQuickReply(text)
        }
      )

      // 5. Bottom Input Bar
      ChatInputBar(
        text = inputText,
        onTextChanged = { viewModel.onInputTextChanged(it) },
        onSendClick = { viewModel.sendMessage() },
        onAttachClick = { showAttachSheet = true },
        onMicClick = {
          viewModel.toggleAudio()
          Toast.makeText(context, "Voice note playback / record", Toast.LENGTH_SHORT).show()
        },
        onEmojiClick = {
          viewModel.onInputTextChanged(inputText + "🏺 ")
        }
      )
    }
  }

  // --- Modal Sheets & Dialogs ---

  // Meetup Details Bottom Sheet
  if (showMeetupDetails) {
    MeetupDetailsBottomSheet(
      meetup = meetupInfo,
      onDismiss = { showMeetupDetails = false },
      onViewStudioPass = {
        showMeetupDetails = false
        viewModel.setSelectedReservation(
          ReservationInfo(
            wheelName = "Shimpo Wheel #4",
            studio = "Studio B",
            timeSlot = "5:30 PM – 6:30 PM (Pre-jam session)"
          )
        )
      },
      sheetState = meetupSheetState
    )
  }

  // Studio Wheel Pass Bottom Sheet
  selectedReservation?.let { reservation ->
    StudioPassBottomSheet(
      reservation = reservation,
      onDismiss = { viewModel.clearSelectedReservation() },
      sheetState = passSheetState
    )
  }

  // Glaze Inspection Dialog
  if (showGlazeDialog) {
    GlazeInspectionDialog(
      recipe = glazeRecipe,
      onDismiss = { showGlazeDialog = false }
    )
  }

  // Attach Options Sheet
  if (showAttachSheet) {
    AttachOptionsSheet(
      onAttachBisquePhoto = { viewModel.attachBisquePhoto() },
      onAttachWheelRequest = { viewModel.requestWheelBooking() },
      onAttachGlazeFormula = { viewModel.shareGlazeFormula() },
      onAttachLocation = { viewModel.sendStudioLocation() },
      onDismiss = { showAttachSheet = false },
      sheetState = attachSheetState
    )
  }

  // Elena's Profile Sheet
  if (showProfileSheet) {
    ElenaProfileSheet(
      onDismiss = { showProfileSheet = false },
      onStartCall = { showCallDialog = true },
      sheetState = profileSheetState
    )
  }

  // Simulated Studio Call Dialog
  if (showCallDialog) {
    StudioCallDialog(
      onEndCall = { showCallDialog = false }
    )
  }
}
