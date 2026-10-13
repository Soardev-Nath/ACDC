package com.example.ui.components.explore

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun CreatePostOrMeetupDialog(
  onDismiss: () -> Unit,
  onSubmitPost: (club: String, title: String, content: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0 = New Post, 1 = Host Meetup
  var selectedClub by remember { mutableStateOf("Earth & Clay Club") }
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }

  val clubs = listOf("Earth & Clay Club", "Film & Photo Collective", "Oakland Sourdough Bakers", "Weekend Trail Runners")

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(24.dp)),
      color = Color(0xFFFFF8F5),
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        // Top row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (selectedTab == 0) "Create Club Discussion" else "Host Community Meetup",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            ),
            color = WarmOnSurface
          )

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = WarmOnSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFAF2EE))
            .padding(4.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (selectedTab == 0) Color.White else Color.Transparent)
              .clickable { selectedTab = 0 }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Forum,
                contentDescription = null,
                tint = if (selectedTab == 0) TerracottaPrimary else WarmOnSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "New Post",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold
                ),
                color = if (selectedTab == 0) TerracottaPrimary else WarmOnSurfaceVariant
              )
            }
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (selectedTab == 1) Color.White else Color.Transparent)
              .clickable { selectedTab = 1 }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.EventAvailable,
                contentDescription = null,
                tint = if (selectedTab == 1) TerracottaPrimary else WarmOnSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Host Meetup",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold
                ),
                color = if (selectedTab == 1) TerracottaPrimary else WarmOnSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Club selector chips
        Text(
          text = "Select Community Board:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = WarmOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          clubs.take(2).forEach { club ->
            val isSelected = club == selectedClub
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFFFDBD1) else Color.White)
                .border(1.dp, if (isSelected) TerracottaPrimary else Color(0xFFEADFD9), CircleShape)
                .clickable { selectedClub = club }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = club,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (isSelected) TerracottaPrimary else WarmOnSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Form Fields
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(if (selectedTab == 0) "Discussion Title / Question" else "Meetup Title") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("create_post_title_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text(if (selectedTab == 0) "What's on your mind? Share tips, ask gear questions..." else "Meetup description, location, and equipment requirements") },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("create_post_content_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            if (title.isNotBlank()) {
              onSubmitPost(selectedClub, title, content)
            }
          },
          enabled = title.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("submit_create_post_button"),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
        ) {
          Text(
            text = if (selectedTab == 0) "Publish Discussion" else "Create Meetup Listing",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }
      }
    }
  }
}
