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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Yard
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClubItem
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.SageSecondary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun PopularClubsSection(
  clubs: List<ClubItem>,
  onClubClick: (ClubItem) -> Unit,
  onJoinClick: (ClubItem) -> Unit,
  onExploreAllClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Popular Clubs Near You",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = WarmOnSurface
        )
        Text(
          text = "Active local communities welcoming new members",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
          color = WarmOnSurfaceVariant
        )
      }

      Text(
        text = "Explore all",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        ),
        color = TerracottaPrimary,
        modifier = Modifier
          .clickable { onExploreAllClick() }
          .padding(vertical = 4.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // List of clubs
    Column(
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      clubs.forEach { club ->
        ClubItemRow(
          club = club,
          onClubClick = { onClubClick(club) },
          onJoinClick = { onJoinClick(club) }
        )
      }
    }
  }
}

@Composable
private fun ClubItemRow(
  club: ClubItem,
  onClubClick: () -> Unit,
  onJoinClick: () -> Unit
) {
  val iconBg = when (club.badgeColorType) {
    "secondary" -> Color(0xFFE7F5EA)
    "tertiary" -> Color(0xFFFEF3C7)
    else -> Color(0xFFFFECEB)
  }

  val iconTint = when (club.badgeColorType) {
    "secondary" -> SageSecondary
    "tertiary" -> AmberTertiary
    else -> TerracottaPrimary
  }

  val icon = when (club.iconName) {
    "potted_plant" -> Icons.Default.Yard
    "local_cafe" -> Icons.Default.Coffee
    else -> Icons.Default.Build
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, Color(0xFFEADFD9), RoundedCornerShape(18.dp))
      .clickable { onClubClick() }
      .testTag("club_row_${club.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        // Club Icon
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(iconBg)
            .border(1.dp, iconTint.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = club.name,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.5.sp
              ),
              color = WarmOnSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(iconBg)
                .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
              Text(
                text = club.categoryBadge,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.5.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = iconTint
              )
            }
          }

          Text(
            text = club.tagline,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = WarmOnSurfaceVariant,
            modifier = Modifier.padding(top = 1.dp)
          )

          Text(
            text = club.memberCountText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            ),
            color = Color(0xFF8C716A),
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = onJoinClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (club.isJoined) Color(0xFFFAF2EE) else TerracottaPrimary,
          contentColor = if (club.isJoined) TerracottaPrimary else Color.White
        ),
        modifier = Modifier.height(34.dp)
      ) {
        if (club.isJoined) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
          text = if (club.isJoined) "Joined" else "Join Club",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp
          )
        )
      }
    }
  }
}
