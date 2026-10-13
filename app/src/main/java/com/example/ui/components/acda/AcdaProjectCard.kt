package com.example.ui.components.acda

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.AcdaProject
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AcdaProjectCard(
  project: AcdaProject,
  onContributeClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val formatter = NumberFormat.getNumberInstance(Locale.US)
  val raisedFormatted = "₦${formatter.format(project.raisedAmountNaira)}"
  val targetFormatted = "₦${formatter.format(project.targetAmountNaira)}"

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Color(0xFFDBE8DB), RoundedCornerShape(20.dp))
      .testTag("project_card_${project.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column {
      // Photo container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
      ) {
        Image(
          painter = painterResource(id = project.imageResId),
          contentDescription = project.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth().height(160.dp)
        )

        // Status Badge overlay
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.95f))
            .border(1.dp, AcdaGreenPrimary.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = AcdaGreenPrimary,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = project.statusText,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              ),
              color = AcdaGreenPrimary
            )
          }
        }

        // Category Tag overlay
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .clip(CircleShape)
            .background(AcdaGreenPrimary)
            .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
          Text(
            text = project.category,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp
            ),
            color = Color.White
          )
        }
      }

      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = project.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.5.sp
          ),
          color = AcdaOnSurface
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(top = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = AcdaGreenPrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = project.location,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = AcdaOnSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = project.description,
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.5.sp,
            lineHeight = 18.sp
          ),
          color = AcdaOnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Progress funding bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Raised: $raisedFormatted",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            ),
            color = AcdaGreenPrimary
          )
          Text(
            text = "Target: $targetFormatted",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp
            ),
            color = AcdaOnSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
          progress = { (project.progressPercent / 100f).coerceIn(0f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(CircleShape),
          color = AcdaGreenPrimary,
          trackColor = Color(0xFFE8F5E9)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "PROJECT LEAD",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold
              ),
              color = AcdaOnSurfaceVariant
            )
            Text(
              text = project.projectCommitteeLead,
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp
              ),
              color = AcdaOnSurface
            )
          }

          Button(
            onClick = onContributeClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
            modifier = Modifier.height(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Paid,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Contribute",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            )
          }
        }
      }
    }
  }
}
