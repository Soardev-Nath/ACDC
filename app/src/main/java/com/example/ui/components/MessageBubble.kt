package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.model.ReservationInfo
import com.example.model.Sender
import com.example.ui.theme.ChatBubbleBorder
import com.example.ui.theme.ChatBubbleInbound
import com.example.ui.theme.ChatBubbleOutbound
import com.example.ui.theme.ReservedBadgeBg
import com.example.ui.theme.ReservedBadgeText
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun MessageBubble(
  message: ChatMessage,
  isAudioPlaying: Boolean,
  audioPlayProgress: Float,
  onToggleAudio: () -> Unit,
  onInspectGlaze: () -> Unit,
  onViewStudioPass: (ReservationInfo) -> Unit,
  modifier: Modifier = Modifier
) {
  val isOutbound = message.sender == Sender.SOFIA

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp),
    horizontalArrangement = if (isOutbound) Arrangement.End else Arrangement.Start,
    verticalAlignment = Alignment.Bottom
  ) {
    // Elena's Avatar for inbound messages
    if (!isOutbound) {
      Image(
        painter = painterResource(id = R.drawable.img_elena_avatar),
        contentDescription = "Elena",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .border(1.dp, Color(0xFFEADBCE), CircleShape)
          .padding(bottom = 2.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
    }

    Column(
      horizontalAlignment = if (isOutbound) Alignment.End else Alignment.Start,
      modifier = Modifier.widthIn(max = 310.dp)
    ) {
      when (message.type) {
        MessageType.TEXT -> {
          TextMessageContent(message = message, isOutbound = isOutbound)
        }
        MessageType.MEDIA -> {
          MediaGlazeContent(message = message, onInspectGlaze = onInspectGlaze)
        }
        MessageType.AUDIO -> {
          AudioVoiceContent(
            message = message,
            isPlaying = isAudioPlaying,
            playProgress = audioPlayProgress,
            onToggleAudio = onToggleAudio
          )
        }
        MessageType.RESERVATION -> {
          message.reservation?.let { res ->
            ReservationContent(reservation = res, onViewStudioPass = { onViewStudioPass(res) })
          }
        }
      }

      // Timestamp & read status
      Row(
        modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = message.timestamp,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = WarmOnSurfaceVariant
        )
        if (isOutbound) {
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.DoneAll,
            contentDescription = "Delivered and Read",
            tint = TerracottaPrimary,
            modifier = Modifier.size(15.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun TextMessageContent(
  message: ChatMessage,
  isOutbound: Boolean
) {
  val shape = if (isOutbound) {
    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
  } else {
    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
  }

  val backgroundColor = if (isOutbound) ChatBubbleOutbound else ChatBubbleInbound
  val textColor = if (isOutbound) Color.White else WarmOnSurface

  Box(
    modifier = Modifier
      .clip(shape)
      .background(backgroundColor)
      .then(
        if (!isOutbound) Modifier.border(1.dp, ChatBubbleBorder, shape) else Modifier
      )
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag(if (isOutbound) "outbound_bubble" else "inbound_bubble")
  ) {
    Text(
      text = message.text,
      style = MaterialTheme.typography.bodyLarge.copy(
        lineHeight = 22.sp,
        fontSize = 14.5.sp
      ),
      color = textColor
    )
  }
}

@Composable
private fun MediaGlazeContent(
  message: ChatMessage,
  onInspectGlaze: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, ChatBubbleBorder, RoundedCornerShape(20.dp))
      .testTag("glaze_media_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column {
      // Photo container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .clickable { onInspectGlaze() }
      ) {
        val imageId = message.mediaResId ?: R.drawable.img_glaze_tile
        Image(
          painter = painterResource(id = imageId),
          contentDescription = "Glaze Test Tile",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        )

        // Pill badge on the image bottom-left
        Box(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(10.dp)
            .clip(CircleShape)
            .background(Color(0xCC1A1615))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "🏺",
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = message.mediaBadge ?: "Cone 6 Reduction • Rutile Amber Test",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              ),
              color = Color.White
            )
          }
        }

        // Zoom inspect button on top right
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .size(34.dp)
            .clip(CircleShape)
            .background(Color(0xBBFFFFFF))
            .clickable { onInspectGlaze() }
            .testTag("inspect_glaze_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Inspect recipe and test results",
            tint = WarmOnSurface,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // Caption below image
      message.mediaCaption?.let { caption ->
        Text(
          text = caption,
          style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 20.sp,
            fontSize = 14.sp
          ),
          color = WarmOnSurface,
          modifier = Modifier.padding(14.dp)
        )
      }
    }
  }
}

@Composable
private fun AudioVoiceContent(
  message: ChatMessage,
  isPlaying: Boolean,
  playProgress: Float,
  onToggleAudio: () -> Unit
) {
  Card(
    modifier = Modifier
      .widthIn(min = 250.dp, max = 290.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, ChatBubbleBorder, RoundedCornerShape(20.dp))
      .testTag("voice_note_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Play button
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(TerracottaPrimary)
            .clickable { onToggleAudio() }
            .testTag("audio_play_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Stop" else "Play",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Sound waveform graphic
        AudioWaveform(
          isPlaying = isPlaying,
          progress = playProgress,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Time indicators: current position and total duration
      Row(
        modifier = Modifier.fillMaxWidth().padding(start = 54.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val currentSec = if (isPlaying) (playProgress * message.audioDuration).toInt() else message.audioPosition
        Text(
          text = String.format("0:%02d", currentSec),
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = WarmOnSurfaceVariant
        )
        Text(
          text = String.format("0:%02d", message.audioDuration),
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = WarmOnSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun AudioWaveform(
  isPlaying: Boolean,
  progress: Float,
  modifier: Modifier = Modifier
) {
  val barHeights = listOf(10, 16, 24, 18, 12, 22, 28, 16, 20, 26, 14, 18, 22, 10, 14, 20, 12, 16)
  val totalBars = barHeights.size

  Row(
    modifier = modifier.height(32.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(3.dp)
  ) {
    barHeights.forEachIndexed { index, baseHeight ->
      val barFraction = index.toFloat() / totalBars.toFloat()
      val isPast = barFraction <= progress

      val activeColor = TerracottaPrimary
      val inactiveColor = Color(0xFFDCCDC6)

      Box(
        modifier = Modifier
          .width(3.dp)
          .height(baseHeight.dp)
          .clip(CircleShape)
          .background(if (isPast) activeColor else inactiveColor)
      )
    }
  }
}

@Composable
private fun ReservationContent(
  reservation: ReservationInfo,
  onViewStudioPass: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, ChatBubbleBorder, RoundedCornerShape(20.dp))
      .testTag("wheel_reservation_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp)
    ) {
      // Top status row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(ReservedBadgeBg)
            .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = ReservedBadgeText,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = reservation.status,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
              ),
              color = ReservedBadgeText
            )
          }
        }

        Text(
          text = reservation.studio,
          style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          ),
          color = WarmOnSurface
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Middle tool + title row
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFBF1EC)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Build,
            contentDescription = "Pottery Wheel",
            tint = TerracottaPrimary,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = reservation.wheelName,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            ),
            color = WarmOnSurface
          )
          Text(
            text = reservation.timeSlot,
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.5.sp
            ),
            color = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Footer row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Ready upon check-in",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp
          ),
          color = WarmOnSurfaceVariant
        )

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFF4ECE8))
            .clickable { onViewStudioPass() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .testTag("view_studio_pass_button"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "View Studio Pass",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = WarmOnSurface
          )
        }
      }
    }
  }
}
