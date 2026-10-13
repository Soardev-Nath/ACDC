package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.model.MeetupInfo
import com.example.ui.theme.DateBadgeBg
import com.example.ui.theme.DateBadgeRed
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun PinnedMeetupBanner(
  meetup: MeetupInfo,
  onDetailsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 4.dp)
      .clickable { onDetailsClick() }
      .testTag("pinned_meetup_banner"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFFAF2EE)
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0E5DF)),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Date badge square
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(DateBadgeBg)
          .border(1.dp, Color(0xFFFFD4D0), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = meetup.dateBadgeMonth,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            ),
            color = DateBadgeRed
          )
          Text(
            text = meetup.dateBadgeDay,
            style = MaterialTheme.typography.titleMedium.copy(
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              lineHeight = 18.sp
            ),
            color = DateBadgeRed
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Middle Info
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "SHARED MEETUP",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.4.sp
            ),
            color = TerracottaPrimary
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "•",
            style = MaterialTheme.typography.bodySmall,
            color = WarmOnSurfaceVariant
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = meetup.timeSummary,
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            ),
            color = WarmOnSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Text(
          text = meetup.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          ),
          color = WarmOnSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Pill Button: Details ->
      Box(
        modifier = Modifier
          .clip(CircleShape)
          .background(Color.White)
          .border(1.dp, Color(0xFFE2D6D0), CircleShape)
          .clickable { onDetailsClick() }
          .padding(horizontal = 12.dp, vertical = 7.dp)
          .testTag("meetup_details_button"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Details",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            ),
            color = TerracottaPrimary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TerracottaPrimary,
            modifier = Modifier.size(13.dp)
          )
        }
      }
    }
  }
}
