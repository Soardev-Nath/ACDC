package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant
import kotlinx.coroutines.delay

@Composable
fun StudioCallDialog(
  onEndCall: () -> Unit,
  modifier: Modifier = Modifier
) {
  var callDuration by remember { mutableIntStateOf(0) }
  var isMuted by remember { mutableStateOf(false) }
  var isSpeaker by remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(1000)
      callDuration++
    }
  }

  Dialog(onDismissRequest = onEndCall) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.9f)
        .clip(RoundedCornerShape(28.dp)),
      color = Color(0xFF221E1C),
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "STUDIO AUDIO CALL",
          style = MaterialTheme.typography.labelSmall.copy(
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold
          ),
          color = Color(0xFFD4A373)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
          painter = painterResource(id = R.drawable.img_elena_avatar),
          contentDescription = "Elena",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .border(3.dp, TerracottaPrimary, CircleShape)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Elena Rostova",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
          ),
          color = Color.White
        )

        val minutes = callDuration / 60
        val seconds = callDuration % 60
        Text(
          text = String.format("%02d:%02d", minutes, seconds),
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFFBEABA2)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Call Control Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Mute button
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (isMuted) Color.White else Color(0xFF38322F)),
            contentAlignment = Alignment.Center
          ) {
            IconButton(onClick = { isMuted = !isMuted }) {
              Icon(
                imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Mute",
                tint = if (isMuted) Color.Black else Color.White
              )
            }
          }

          // End call
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(Color(0xFFE53935)),
            contentAlignment = Alignment.Center
          ) {
            IconButton(onClick = onEndCall) {
              Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "End Call",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
              )
            }
          }

          // Speaker
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (isSpeaker) TerracottaPrimary else Color(0xFF38322F)),
            contentAlignment = Alignment.Center
          ) {
            IconButton(onClick = { isSpeaker = !isSpeaker }) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Speaker",
                tint = Color.White
              )
            }
          }
        }
      }
    }
  }
}
