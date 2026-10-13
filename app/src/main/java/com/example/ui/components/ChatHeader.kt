package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun ChatHeader(
  onBackClick: () -> Unit,
  onCalendarClick: () -> Unit,
  onCallClick: () -> Unit,
  onViewStudioLocation: () -> Unit,
  onViewElenaProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = onBackClick,
      modifier = Modifier
        .size(44.dp)
        .testTag("chat_back_button")
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back to chats",
        tint = WarmOnSurface
      )
    }

    // Avatar with online status indicator
    Box(
      modifier = Modifier
        .size(44.dp)
        .clickable { onViewElenaProfile() }
        .testTag("elena_avatar_header"),
      contentAlignment = Alignment.BottomEnd
    ) {
      Image(
        painter = painterResource(id = R.drawable.img_elena_avatar),
        contentDescription = "Elena Rostova profile picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
      )
      // Online green dot
      Box(
        modifier = Modifier
          .size(11.dp)
          .clip(CircleShape)
          .background(OnlineGreen)
          .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Name and status subtitle
    Column(
      modifier = Modifier
        .weight(1f)
        .clickable { onViewElenaProfile() }
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Elena Rostova",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = WarmOnSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Verified Master Ceramist",
          tint = TerracottaPrimary,
          modifier = Modifier.size(16.dp)
        )
      }

      Text(
        text = "Master... • Eart... • 12m ago",
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 12.sp
        ),
        color = WarmOnSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    // Action Icons
    IconButton(
      onClick = onCalendarClick,
      modifier = Modifier
        .size(40.dp)
        .testTag("header_calendar_button")
    ) {
      Icon(
        imageVector = Icons.Default.DateRange,
        contentDescription = "Upcoming Meetup Details",
        tint = TerracottaPrimary
      )
    }

    IconButton(
      onClick = onCallClick,
      modifier = Modifier
        .size(40.dp)
        .testTag("header_call_button")
    ) {
      Icon(
        imageVector = Icons.Default.Call,
        contentDescription = "Call Elena",
        tint = WarmOnSurfaceVariant
      )
    }

    Box {
      IconButton(
        onClick = { menuExpanded = true },
        modifier = Modifier
          .size(40.dp)
          .testTag("header_more_button")
      ) {
        Icon(
          imageVector = Icons.Default.MoreVert,
          contentDescription = "More options",
          tint = WarmOnSurfaceVariant
        )
      }

      DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
      ) {
        DropdownMenuItem(
          text = { Text("View Elena's Portfolio") },
          onClick = {
            menuExpanded = false
            onViewElenaProfile()
          }
        )
        DropdownMenuItem(
          text = { Text("Studio Directions & Hours") },
          onClick = {
            menuExpanded = false
            onViewStudioLocation()
          }
        )
        DropdownMenuItem(
          text = { Text("Meetup Details") },
          onClick = {
            menuExpanded = false
            onCalendarClick()
          }
        )
      }
    }
  }
}
