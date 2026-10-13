package com.example.ui.dialogs

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.MeetupInfo
import com.example.ui.theme.DateBadgeBg
import com.example.ui.theme.DateBadgeRed
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetupDetailsBottomSheet(
  meetup: MeetupInfo,
  onDismiss: () -> Unit,
  onViewStudioPass: () -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  var isCalendarAdded by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFFFF8F5),
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    modifier = modifier.testTag("meetup_details_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .navigationBarsPadding()
    ) {
      // Header with close button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Color(0xFFE7F5EA))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "✓ RSVP: GOING",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              ),
              color = Color(0xFF2E6B3E)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "14 Ceramists Attending",
            style = MaterialTheme.typography.bodySmall,
            color = WarmOnSurfaceVariant
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Studio Banner Image
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_studio_banner),
          contentDescription = "Pottery Studio",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth()
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Meetup Title
      Text(
        text = meetup.title,
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        ),
        color = WarmOnSurface
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Date & Time Row
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DateBadgeBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = DateBadgeRed,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = meetup.fullDateTime,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = WarmOnSurface
          )
          Text(
            text = "Studio doors open at 5:00 PM for setup",
            style = MaterialTheme.typography.bodySmall,
            color = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Location Row
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF4ECE8)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = TerracottaPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Earth & Fire Ceramic Studio",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            ),
            color = WarmOnSurface
          )
          Text(
            text = "428 Kiln Way, Arts District (Studio B)",
            style = MaterialTheme.typography.bodySmall,
            color = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Host Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0E5DF))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_elena_avatar),
            contentDescription = "Elena Rostova",
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape),
            contentScale = ContentScale.Crop
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = meetup.hostName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarmOnSurface
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = TerracottaPrimary,
                modifier = Modifier.size(15.dp)
              )
            }
            Text(
              text = meetup.hostRole,
              style = MaterialTheme.typography.bodySmall,
              color = WarmOnSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Description
      Text(
        text = "About this Meetup",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = WarmOnSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = meetup.description,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
        color = WarmOnSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      // What to Bring list
      Text(
        text = "What to Bring",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = WarmOnSurface
      )
      Spacer(modifier = Modifier.height(8.dp))
      meetup.whatToBring.forEach { item ->
        Row(
          modifier = Modifier.padding(vertical = 3.dp),
          verticalAlignment = Alignment.Top
        ) {
          Text(text = "• ", fontWeight = FontWeight.Bold, color = TerracottaPrimary)
          Text(
            text = item,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmOnSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons
      Button(
        onClick = {
          isCalendarAdded = true
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("add_calendar_button"),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
      ) {
        Icon(
          imageVector = if (isCalendarAdded) Icons.Default.CheckCircle else Icons.Default.DateRange,
          contentDescription = null,
          tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isCalendarAdded) "Added to Google Calendar!" else "Add to Google Calendar",
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = {
          onDismiss()
          onViewStudioPass()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("dialog_view_pass_button"),
        shape = RoundedCornerShape(24.dp)
      ) {
        Text(
          text = "View Reserved Shimpo Wheel #4 Pass",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = TerracottaPrimary
          )
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
