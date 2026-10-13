package com.example.ui.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.MeetupCardItem
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.SageSecondary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun HappeningThisWeekSection(
  meetups: List<MeetupCardItem>,
  onMeetupClick: (MeetupCardItem) -> Unit,
  onRsvpClick: (MeetupCardItem) -> Unit,
  onSeeAllClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Column(
    modifier = modifier.fillMaxWidth()
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Happening This Week",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = WarmOnSurface
        )
        Text(
          text = "RSVP for upcoming neighborhood club meetups",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
          color = WarmOnSurfaceVariant
        )
      }

      Row(
        modifier = Modifier
          .clickable { onSeeAllClick() }
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "See all",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = TerracottaPrimary
        )
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = null,
          tint = TerracottaPrimary,
          modifier = Modifier.size(16.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Horizontal list of cards
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      meetups.forEach { meetup ->
        MeetupHorizontalCard(
          meetup = meetup,
          onCardClick = { onMeetupClick(meetup) },
          onRsvpClick = { onRsvpClick(meetup) }
        )
      }
    }
  }
}

@Composable
private fun MeetupHorizontalCard(
  meetup: MeetupCardItem,
  onCardClick: () -> Unit,
  onRsvpClick: () -> Unit
) {
  val context = LocalContext.current

  val dateBadgeBg = when (meetup.tagType) {
    "secondary" -> Color(0xFFFFECEB)
    "tertiary" -> Color(0xFFFEF3C7)
    else -> Color(0xFFE7F5EA)
  }

  val dateBadgeColor = when (meetup.tagType) {
    "secondary" -> TerracottaPrimary
    "tertiary" -> AmberTertiary
    else -> SageSecondary
  }

  Card(
    modifier = Modifier
      .width(270.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Color(0xFFEADFD9), RoundedCornerShape(20.dp))
      .clickable { onCardClick() }
      .testTag("meetup_card_${meetup.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .padding(14.dp)
    ) {
      // Top Date + Tag row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // Date block
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(dateBadgeBg)
            .border(1.dp, dateBadgeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = meetup.dayOfWeek,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              ),
              color = dateBadgeColor
            )
            Text(
              text = meetup.dayOfMonth,
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 18.sp
              ),
              color = dateBadgeColor
            )
          }
        }

        // Tag pill
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFF4ECE8))
            .padding(horizontal = 9.dp, vertical = 4.dp)
        ) {
          Text(
            text = meetup.tagText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.5.sp,
              fontWeight = FontWeight.Medium
            ),
            color = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title
      Text(
        text = meetup.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        ),
        color = WarmOnSurface,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      // Club name
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
      ) {
        val icon = when (meetup.clubIcon) {
          "nest_eco_leaf" -> Icons.Default.Palette
          "sprint" -> Icons.Default.DirectionsRun
          else -> Icons.Default.CameraAlt
        }
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = TerracottaPrimary,
          modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = meetup.clubName,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
          color = WarmOnSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Time & Cost details
      Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = Color(0xFF8C716A),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = meetup.timeAndDuration,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = WarmOnSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (meetup.costOrAttendees.contains("$")) Icons.Default.Paid else Icons.Default.Group,
            contentDescription = null,
            tint = Color(0xFF8C716A),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = meetup.costOrAttendees,
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp,
              fontWeight = if (meetup.costOrAttendees.contains("$")) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = if (meetup.costOrAttendees.contains("$")) WarmOnSurface else WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom Row: Facepile & RSVP button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .border(0.5.dp, Color.Transparent, RoundedCornerShape(0.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (meetup.avatars.isNotEmpty()) {
          Box {
            meetup.avatars.forEachIndexed { i, url ->
              AsyncImage(
                model = ImageRequest.Builder(context)
                  .data(url)
                  .crossfade(true)
                  .error(R.drawable.img_elena_avatar)
                  .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .offset(x = (i * 14).dp)
                  .size(24.dp)
                  .clip(CircleShape)
                  .border(1.5.dp, Color.White, CircleShape)
              )
            }
          }
        } else {
          Text(
            text = meetup.hostInfo ?: "",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = WarmOnSurfaceVariant
          )
        }

        Button(
          onClick = onRsvpClick,
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (meetup.isRsvped) Color(0xFFE7F5EA) else TerracottaPrimary,
            contentColor = if (meetup.isRsvped) Color(0xFF2E6B3E) else Color.White
          ),
          modifier = Modifier.height(34.dp)
        ) {
          Text(
            text = if (meetup.isRsvped) "RSVP'd ✓" else "RSVP Spot",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.5.sp
            )
          )
        }
      }
    }
  }
}
