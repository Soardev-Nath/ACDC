package com.example.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WhatsAppInputBar(
  text: String,
  onTextChanged: (String) -> Unit,
  onSendClick: () -> Unit,
  onAttachClick: () -> Unit,
  onCameraClick: () -> Unit,
  onMicClick: () -> Unit,
  onEmojiClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(Color(0xFFF0F2F5))
      .padding(horizontal = 6.dp, vertical = 6.dp)
      .navigationBarsPadding(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Rounded White Input Container
    Row(
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(Color.White)
        .padding(horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Emoji face icon
      IconButton(
        onClick = onEmojiClick,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.SentimentSatisfied,
          contentDescription = "Emoji",
          tint = Color(0xFF8696A0),
          modifier = Modifier.size(24.dp)
        )
      }

      // Text Input
      Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.CenterStart
      ) {
        if (text.isEmpty()) {
          Text(
            text = "Message",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
            color = Color(0xFF8696A0)
          )
        }

        BasicTextField(
          value = text,
          onValueChange = onTextChanged,
          textStyle = TextStyle(
            color = Color(0xFF111B21),
            fontSize = 15.sp
          ),
          cursorBrush = SolidColor(Color(0xFF00A884)),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = {
            if (text.isNotBlank()) onSendClick()
          }),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("whatsapp_message_input")
        )
      }

      // Paperclip Attachment icon
      IconButton(
        onClick = onAttachClick,
        modifier = Modifier
          .size(36.dp)
          .testTag("whatsapp_attach_button")
      ) {
        Icon(
          imageVector = Icons.Default.AttachFile,
          contentDescription = "Attach",
          tint = Color(0xFF8696A0),
          modifier = Modifier.size(22.dp)
        )
      }

      // Camera icon (if text is empty)
      if (text.isEmpty()) {
        IconButton(
          onClick = onCameraClick,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Camera",
            tint = Color(0xFF8696A0),
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(6.dp))

    // WhatsApp Floating Round Send or Mic Button
    val hasText = text.isNotBlank()
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(Color(0xFF00A884))
        .clickable {
          if (hasText) onSendClick() else onMicClick()
        }
        .testTag("whatsapp_send_mic_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (hasText) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
        contentDescription = if (hasText) "Send" else "Record Voice",
        tint = Color.White,
        modifier = Modifier.size(22.dp)
      )
    }
  }
}
