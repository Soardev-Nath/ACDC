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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.WhatsAppPrimaryDark

@Composable
fun WhatsAppCallDialog(
  contactName: String,
  isVideo: Boolean,
  onEndCall: () -> Unit
) {
  Dialog(
    onDismissRequest = onEndCall,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = WhatsAppPrimaryDark
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 48.dp)
        ) {
          Text(
            text = "WhatsApp ${if (isVideo) "Video" else "Voice"} Call",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = contactName,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Ringing...",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
          )
        }

        Box(
          modifier = Modifier
            .size(140.dp)
            .background(Color.White.copy(alpha = 0.15f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(80.dp)
          )
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {},
            modifier = Modifier
              .size(56.dp)
              .background(Color.White.copy(alpha = 0.2f), CircleShape)
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = "Speaker", tint = Color.White)
          }

          IconButton(
            onClick = {},
            modifier = Modifier
              .size(56.dp)
              .background(Color.White.copy(alpha = 0.2f), CircleShape)
          ) {
            Icon(Icons.Default.MicOff, contentDescription = "Mute", tint = Color.White)
          }

          IconButton(
            onClick = onEndCall,
            modifier = Modifier
              .size(64.dp)
              .background(Color(0xFFE53935), CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.CallEnd,
              contentDescription = "End Call",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      }
    }
  }
}
