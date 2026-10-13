package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import com.example.model.AcdaNewsBulletin
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailBottomSheet(
  news: AcdaNewsBulletin,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFF9FCF9),
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    modifier = modifier.testTag("news_detail_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 6.dp)
        .navigationBarsPadding()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFE8F5E9))
            .border(1.dp, Color(0xFFC8E6C9), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = news.category.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp
            ),
            color = AcdaGreenPrimary
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = AcdaOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = news.title,
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          lineHeight = 28.sp
        ),
        color = AcdaOnSurface
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.DateRange,
          contentDescription = null,
          tint = AcdaGreenPrimary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
          text = news.date,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = AcdaOnSurfaceVariant
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "•", color = Color(0xFF7D9480))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = news.authorRole,
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
          ),
          color = AcdaGreenPrimary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Official Summary:",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = AcdaGoldAccent
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = news.summary,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              lineHeight = 20.sp
            ),
            color = AcdaOnSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = news.fullContent,
        style = MaterialTheme.typography.bodyLarge.copy(
          lineHeight = 24.sp,
          fontSize = 14.5.sp
        ),
        color = AcdaOnSurface
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Secretariat sign-off box
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "Signed & Issued By:",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = AcdaOnSurfaceVariant
          )
          Text(
            text = news.author,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = AcdaGreenPrimary
          )
          Text(
            text = "Afo Community Development Association (ACDA) National Secretariat",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = AcdaOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
