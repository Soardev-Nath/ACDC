package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ChatBubbleBorder
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurfaceVariant
import kotlinx.coroutines.delay

@Composable
fun TypingIndicator(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Elena's Avatar
    Image(
      painter = painterResource(id = R.drawable.img_elena_avatar),
      contentDescription = "Elena",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
        .border(1.dp, Color(0xFFEADBCE), CircleShape)
    )

    Spacer(modifier = Modifier.width(8.dp))

    // Rounded pill with animated dots
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .border(1.dp, ChatBubbleBorder, RoundedCornerShape(16.dp))
        .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        BouncingDot(delayMillis = 0)
        BouncingDot(delayMillis = 150)
        BouncingDot(delayMillis = 300)
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      text = "Elena is typing...",
      style = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp,
        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
      ),
      color = WarmOnSurfaceVariant
    )
  }
}

@Composable
private fun BouncingDot(
  delayMillis: Int
) {
  val offset = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    delay(delayMillis.toLong())
    offset.animateTo(
      targetValue = -4f,
      animationSpec = infiniteRepeatable(
        animation = tween(400, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
      )
    )
  }

  Box(
    modifier = Modifier
      .offset(y = offset.value.dp)
      .size(6.dp)
      .clip(CircleShape)
      .background(TerracottaPrimary.copy(alpha = 0.8f))
  )
}
