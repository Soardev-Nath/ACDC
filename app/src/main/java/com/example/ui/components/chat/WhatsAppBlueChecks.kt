package com.example.ui.components.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.theme.WhatsAppCheckBlue

@Composable
fun WhatsAppBlueChecks(modifier: Modifier = Modifier) {
  Row(modifier = modifier) {
    Icon(
      imageVector = Icons.Default.DoneAll,
      contentDescription = "Read",
      tint = WhatsAppCheckBlue,
      modifier = Modifier.size(16.dp)
    )
  }
}
