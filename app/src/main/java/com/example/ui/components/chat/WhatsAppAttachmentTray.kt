package com.example.ui.components.chat

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WhatsAppAttachmentTray(
  onDocumentClick: () -> Unit,
  onCameraClick: () -> Unit,
  onGalleryClick: () -> Unit,
  onAudioClick: () -> Unit,
  onLocationClick: () -> Unit,
  onContactClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 8.dp)
      .testTag("whatsapp_attachment_tray"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(
      modifier = Modifier.padding(vertical = 18.dp, horizontal = 14.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        AttachmentOption(
          label = "Document",
          icon = Icons.Default.Description,
          bgColor = Color(0xFF7F66FF),
          onClick = onDocumentClick
        )
        AttachmentOption(
          label = "Camera",
          icon = Icons.Default.CameraAlt,
          bgColor = Color(0xFFD3396D),
          onClick = onCameraClick
        )
        AttachmentOption(
          label = "Gallery",
          icon = Icons.Default.Image,
          bgColor = Color(0xFFAC44CF),
          onClick = onGalleryClick
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        AttachmentOption(
          label = "Audio",
          icon = Icons.Default.Headphones,
          bgColor = Color(0xFFE06D14),
          onClick = onAudioClick
        )
        AttachmentOption(
          label = "Location",
          icon = Icons.Default.LocationOn,
          bgColor = Color(0xFF1F7E41),
          onClick = onLocationClick
        )
        AttachmentOption(
          label = "Contact",
          icon = Icons.Default.Person,
          bgColor = Color(0xFF009DE2),
          onClick = onContactClick
        )
      }
    }
  }
}

@Composable
private fun AttachmentOption(
  label: String,
  icon: ImageVector,
  bgColor: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable { onClick() }
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(CircleShape)
        .background(bgColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
      color = Color(0xFF54656F)
    )
  }
}
