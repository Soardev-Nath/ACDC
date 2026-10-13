package com.example.ui.components.acda

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun AcdaTopBar(
  currentChapter: String,
  onChapterSelected: (String) -> Unit,
  onMembershipClick: () -> Unit,
  onNotificationsClick: () -> Unit,
  onAdminConsoleClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  var chapterMenuExpanded by remember { mutableStateOf(false) }

  val chapters = listOf(
    "National Headquarters (Udege/Nasarawa)",
    "Abuja FCT Chapter",
    "Lafia / Nasarawa State Chapter",
    "Jos / Plateau Chapter",
    "Lagos & South-West Chapter",
    "Diaspora Chapter (UK / North America)"
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Official Logo & Title
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color.White)
          .border(1.5.dp, AcdaGreenPrimary, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_acda_logo),
          contentDescription = "ACDA Official Seal",
          contentScale = ContentScale.Crop,
          modifier = Modifier.size(40.dp).clip(CircleShape)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "ACDA",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 18.sp,
              letterSpacing = (-0.02).sp
            ),
            color = AcdaGreenPrimary
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "COMMUNITY",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 0.5.sp
            ),
            color = Color(0xFFD97706)
          )
        }
        Text(
          text = "Afo Community Dev. Association",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          ),
          color = AcdaOnSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Chapter selector chip
    Box {
      Row(
        modifier = Modifier
          .clip(CircleShape)
          .background(Color(0xFFE8F5E9))
          .border(1.dp, Color(0xFFC8E6C9), CircleShape)
          .clickable { chapterMenuExpanded = true }
          .padding(horizontal = 9.dp, vertical = 6.dp)
          .testTag("chapter_selector_chip"),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = AcdaGreenPrimary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        val shortChapterName = when {
          currentChapter.contains("Abuja") -> "Abuja FCT"
          currentChapter.contains("Lafia") -> "Lafia"
          currentChapter.contains("Jos") -> "Jos"
          currentChapter.contains("Lagos") -> "Lagos"
          currentChapter.contains("Diaspora") -> "Diaspora"
          else -> "HQ Udege"
        }
        Text(
          text = shortChapterName,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          ),
          color = AcdaGreenPrimary
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
          imageVector = Icons.Default.ExpandMore,
          contentDescription = null,
          tint = AcdaGreenPrimary,
          modifier = Modifier.size(15.dp)
        )
      }

      DropdownMenu(
        expanded = chapterMenuExpanded,
        onDismissRequest = { chapterMenuExpanded = false }
      ) {
        chapters.forEach { chap ->
          DropdownMenuItem(
            text = { Text(chap, fontSize = 13.sp) },
            onClick = {
              onChapterSelected(chap)
              chapterMenuExpanded = false
            }
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(6.dp))

    // Digital ID Card shortcut button
    IconButton(
      onClick = onMembershipClick,
      modifier = Modifier
        .size(36.dp)
        .testTag("top_bar_id_card_button")
    ) {
      Icon(
        imageVector = Icons.Default.Badge,
        contentDescription = "Digital Member Card",
        tint = AcdaGreenPrimary,
        modifier = Modifier.size(22.dp)
      )
    }

    // Notification bell
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .clickable { onNotificationsClick() }
        .testTag("acda_notifications_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Notifications,
        contentDescription = "Notifications",
        tint = AcdaOnSurfaceVariant,
        modifier = Modifier.size(20.dp)
      )
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(top = 6.dp, end = 6.dp)
          .size(7.dp)
          .clip(CircleShape)
          .background(Color(0xFFD97706))
      )
    }

    // Admin Console shortcut
    if (onAdminConsoleClick != null) {
      Spacer(modifier = Modifier.width(4.dp))
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Color(0xFFFEF3C7))
          .clickable { onAdminConsoleClick() }
          .testTag("top_bar_admin_console_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AdminPanelSettings,
          contentDescription = "Admin Console",
          tint = Color(0xFFB45309),
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
