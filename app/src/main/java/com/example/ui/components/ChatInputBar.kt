package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
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
import com.example.ui.theme.ChatBubbleBorder
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun ChatInputBar(
  text: String,
  onTextChanged: (String) -> Unit,
  onSendClick: () -> Unit,
  onAttachClick: () -> Unit,
  onMicClick: () -> Unit,
  onEmojiClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(Color(0xFFFFF8F5))
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .navigationBarsPadding(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Plus button (+)
    Box(
      modifier = Modifier
        .size(42.dp)
        .clip(CircleShape)
        .background(Color.White)
        .border(1.dp, ChatBubbleBorder, CircleShape)
        .clickable { onAttachClick() }
        .testTag("attach_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Attach photo or wheel reservation",
        tint = WarmOnSurfaceVariant,
        modifier = Modifier.size(22.dp)
      )
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Text Input Container
    Box(
      modifier = Modifier
        .weight(1f)
        .height(48.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(Color.White)
        .border(1.dp, ChatBubbleBorder, RoundedCornerShape(24.dp))
        .padding(horizontal = 14.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier.weight(1f),
          contentAlignment = Alignment.CenterStart
        ) {
          if (text.isEmpty()) {
            Text(
              text = "Message Elena...",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp
              ),
              color = Color(0xFFA69B95)
            )
          }

          BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            textStyle = TextStyle(
              color = WarmOnSurface,
              fontSize = 15.sp
            ),
            cursorBrush = SolidColor(TerracottaPrimary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
              if (text.isNotBlank()) onSendClick()
            }),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("chat_text_input")
          )
        }

        // Emoji smiley button
        IconButton(
          onClick = onEmojiClick,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.SentimentSatisfied,
            contentDescription = "Insert Emoji",
            tint = WarmOnSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }

        // Mic button
        IconButton(
          onClick = onMicClick,
          modifier = Modifier
            .size(32.dp)
            .testTag("mic_button")
        ) {
          Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Record Voice Note",
            tint = WarmOnSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Send Button
    val canSend = text.isNotBlank()
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape)
        .background(if (canSend) TerracottaPrimary else TerracottaPrimary.copy(alpha = 0.85f))
        .clickable(enabled = canSend) { onSendClick() }
        .testTag("send_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.Send,
        contentDescription = "Send message",
        tint = Color.White,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
