package com.example.ui.components.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppIncomingBubble
import com.example.ui.theme.WhatsAppOutgoingBubble
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppTimestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WhatsAppBubble(
  message: ChatMessage,
  isOutgoing: Boolean,
  canDelete: Boolean,
  onLongClick: () -> Unit = {}
) {
  val alignment = if (isOutgoing) Alignment.End else Alignment.Start
  val bubbleColor = if (isOutgoing) WhatsAppOutgoingBubble else WhatsAppIncomingBubble
  val bubbleShape = if (isOutgoing) {
    RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
  } else {
    RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
  }

  val formattedTime = message.timestamp?.toDate()?.let {
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(it)
  } ?: SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 3.dp),
    horizontalAlignment = alignment
  ) {
    Card(
      shape = bubbleShape,
      colors = CardDefaults.cardColors(containerColor = bubbleColor),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
      modifier = Modifier
        .widthIn(min = 80.dp, max = 300.dp)
        .combinedClickable(
          onClick = {},
          onLongClick = if (canDelete) onLongClick else null
        )
    ) {
      Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
        // Sender name for incoming group messages
        if (!isOutgoing) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 2.dp)
          ) {
            Text(
              text = message.senderName,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (message.isFromAdmin) AcdaRed else WhatsAppPrimary
            )
            if (message.isFromAdmin) {
              Spacer(modifier = Modifier.width(4.dp))
              Surface(
                color = AcdaRed.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin",
                    tint = AcdaRed,
                    modifier = Modifier.size(10.dp)
                  )
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(
                    text = "EXCO ADMIN",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcdaRed
                  )
                }
              }
            }
          }
        }

        // Announcement tag if applicable
        if (message.type == "announcement") {
          Surface(
            color = WhatsAppPrimary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(bottom = 4.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = WhatsAppPrimary,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "OFFICIAL BROADCAST",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppPrimary
              )
            }
          }
        }

        // Message text
        Text(
          text = message.text,
          fontSize = 15.sp,
          color = Color(0xFF111B21),
          lineHeight = 20.sp
        )

        // Timestamp and double check
        Row(
          modifier = Modifier
            .align(Alignment.End)
            .padding(top = 2.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.End
        ) {
          Text(
            text = formattedTime,
            fontSize = 11.sp,
            color = WhatsAppTimestamp
          )
          if (isOutgoing) {
            Spacer(modifier = Modifier.width(4.dp))
            WhatsAppBlueChecks()
          }
        }
      }
    }
  }
}
