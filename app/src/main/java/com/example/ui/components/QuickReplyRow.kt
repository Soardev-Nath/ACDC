package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WarmOnSurface

data class QuickReply(
  val icon: String,
  val text: String
)

@Composable
fun QuickReplyRow(
  onReplySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val replies = listOf(
    QuickReply("✨", "Can't wait!"),
    QuickReply("📍", "Sent studio location"),
    QuickReply("🏺", "Bring porcelain tools"),
    QuickReply("🍯", "Rutile Amber recipe"),
    QuickReply("🕒", "Reserve extra 30 min wheel")
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 14.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    replies.forEachIndexed { index, reply ->
      Box(
        modifier = Modifier
          .clip(CircleShape)
          .background(Color.White)
          .border(1.dp, Color(0xFFEADBCE), CircleShape)
          .clickable { onReplySelected(reply.text) }
          .padding(horizontal = 12.dp, vertical = 7.dp)
          .testTag("quick_reply_chip_$index"),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = reply.icon, fontSize = 13.sp)
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = "\"${reply.text}\"",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 12.5.sp
            ),
            color = WarmOnSurface
          )
        }
      }
    }
  }
}
