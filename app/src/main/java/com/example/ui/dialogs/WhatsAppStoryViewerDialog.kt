package com.example.ui.dialogs

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.WhatsAppPrimaryDark

@Composable
fun WhatsAppStoryViewerDialog(
  authorName: String,
  title: String,
  timeAgo: String,
  onClose: () -> Unit
) {
  Dialog(
    onDismissRequest = onClose,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = Color.Black
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          // Progress bar
          LinearProgressIndicator(
            progress = { 0.75f },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(WhatsAppPrimaryDark, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
            }

            Column(
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
            ) {
              Text(
                text = authorName,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = timeAgo,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
              )
            }

            IconButton(onClick = onClose) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
          }
        }

        // Story card body
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(vertical = 32.dp),
          contentAlignment = Alignment.Center
        ) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = WhatsAppPrimaryDark,
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Column(
              modifier = Modifier.padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "ACDA Community Status",
                color = Color(0xFF25D366),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
              )
            }
          }
        }

        Text(
          text = "Tap to reply to $authorName",
          color = Color.White.copy(alpha = 0.6f),
          fontSize = 13.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        )
      }
    }
  }
}
