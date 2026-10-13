package com.example.ui.dialogs

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
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun CreateForumPostDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, content: String, tag: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedTag by remember { mutableStateOf("Development") }

  val tags = listOf("Development", "Youth Wing", "Cultural Heritage", "Agriculture", "General Welfare")

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(24.dp)),
      color = Color(0xFFF9FCF9),
      tonalElevation = 6.dp
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Forum,
              contentDescription = null,
              tint = AcdaGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Town Hall Dialogue",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
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

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Select Topic Category:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = AcdaOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          tags.take(3).forEach { tag ->
            val isSelected = tag == selectedTag
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
                .border(1.dp, if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB), CircleShape)
                .clickable { selectedTag = tag }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (isSelected) AcdaGreenPrimary else AcdaOnSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Topic Title / Proposition") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("forum_post_title_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Write your thoughts, proposal, or community suggestion...") },
          minLines = 4,
          maxLines = 6,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("forum_post_content_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
              onSubmit(title, content, selectedTag)
            }
          },
          enabled = title.isNotBlank() && content.isNotBlank(),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("submit_forum_post_button")
        ) {
          Text(
            text = "Post to Community Town Hall",
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
