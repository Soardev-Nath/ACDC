package com.example.ui.components.acda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcdaNewsBulletin
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun AcdaNewsCard(
  news: AcdaNewsBulletin,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, Color(0xFFDBE8DB), RoundedCornerShape(18.dp))
      .clickable { onClick() }
      .testTag("news_card_${news.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(if (news.isImportant) Color(0xFFFEF3C7) else Color(0xFFE8F5E9))
              .border(
                1.dp,
                if (news.isImportant) AcdaGoldAccent.copy(alpha = 0.4f) else AcdaGreenPrimary.copy(alpha = 0.3f),
                CircleShape
              )
              .padding(horizontal = 9.dp, vertical = 3.dp)
          ) {
            Text(
              text = news.category,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp
              ),
              color = if (news.isImportant) AcdaGoldAccent else AcdaGreenPrimary
            )
          }

          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "•", color = Color(0xFF7D9480), fontSize = 11.sp)
          Spacer(modifier = Modifier.width(6.dp))

          Text(
            text = news.date,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = AcdaOnSurfaceVariant
          )
        }

        if (news.isImportant) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Color(0xFFDC2626).copy(alpha = 0.1f))
              .padding(horizontal = 7.dp, vertical = 2.dp)
          ) {
            Text(
              text = "PRIORITY",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold
              ),
              color = Color(0xFFDC2626)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = news.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 15.5.sp,
          lineHeight = 22.sp
        ),
        color = AcdaOnSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = news.summary,
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 13.sp,
          lineHeight = 19.sp
        ),
        color = AcdaOnSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = news.authorRole,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
          ),
          color = AcdaGreenPrimary
        )

        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Read Full Bulletin",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            ),
            color = AcdaGreenPrimary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = AcdaGreenPrimary,
            modifier = Modifier.size(13.dp)
          )
        }
      }
    }
  }
}
