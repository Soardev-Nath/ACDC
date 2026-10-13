package com.example.ui.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun ExploreTopBar(
  currentLocation: String,
  onLocationSelected: (String) -> Unit,
  onNotificationsClick: () -> Unit,
  onExploreMenuClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var locationMenuExpanded by remember { mutableStateOf(false) }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Explore icon & Title
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable { onExploreMenuClick() }
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Explore,
          contentDescription = "Explore Menu",
          tint = TerracottaPrimary,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "Clubhouse",
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          letterSpacing = (-0.015).sp
        ),
        color = TerracottaPrimary
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    // Right: Location pill button with dropdown
    Box {
      Row(
        modifier = Modifier
          .clip(CircleShape)
          .background(Color(0xFFFAF2EE))
          .border(1.dp, Color(0xFFF0E5DF), CircleShape)
          .clickable { locationMenuExpanded = true }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("location_selector_chip"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = TerracottaPrimary,
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = currentLocation,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.5.sp
          ),
          color = WarmOnSurfaceVariant
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
          imageVector = Icons.Default.ExpandMore,
          contentDescription = null,
          tint = WarmOnSurfaceVariant,
          modifier = Modifier.size(16.dp)
        )
      }

      DropdownMenu(
        expanded = locationMenuExpanded,
        onDismissRequest = { locationMenuExpanded = false }
      ) {
        listOf("Oakland & Bay Area", "San Francisco (Mission)", "Berkeley & North Bay", "San Jose & South Bay").forEach { loc ->
          DropdownMenuItem(
            text = { Text(loc) },
            onClick = {
              onLocationSelected(loc)
              locationMenuExpanded = false
            }
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Notification bell icon with unread badge
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .clickable { onNotificationsClick() }
        .testTag("notifications_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Notifications,
        contentDescription = "Notifications",
        tint = WarmOnSurfaceVariant,
        modifier = Modifier.size(22.dp)
      )
      // Unread terracotta dot
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(top = 8.dp, end = 8.dp)
          .size(8.dp)
          .clip(CircleShape)
          .background(TerracottaPrimary)
          .border(1.5.dp, Color(0xFFFFF8F5), CircleShape)
      )
    }
  }
}
