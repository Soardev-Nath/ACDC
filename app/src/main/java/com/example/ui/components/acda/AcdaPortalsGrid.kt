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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Forum
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcdaCategory
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun AcdaPortalsGrid(
  activeCategory: AcdaCategory,
  onCategoryClick: (AcdaCategory) -> Unit,
  onMembershipCardClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    PortalItem(
      title = "Projects",
      icon = Icons.Default.Build,
      iconBg = Color(0xFFE8F5E9),
      iconTint = AcdaGreenPrimary,
      isSelected = activeCategory == AcdaCategory.PROJECTS,
      onClick = { onCategoryClick(AcdaCategory.PROJECTS) },
      modifier = Modifier.weight(1f)
    )

    PortalItem(
      title = "Bulletins",
      icon = Icons.Default.Campaign,
      iconBg = Color(0xFFFEF3C7),
      iconTint = AcdaGoldAccent,
      isSelected = activeCategory == AcdaCategory.NEWS,
      onClick = { onCategoryClick(AcdaCategory.NEWS) },
      modifier = Modifier.weight(1f)
    )

    PortalItem(
      title = "Town Hall",
      icon = Icons.Default.Forum,
      iconBg = Color(0xFFE0F2FE),
      iconTint = Color(0xFF0369A1),
      isSelected = activeCategory == AcdaCategory.TOWN_HALL,
      onClick = { onCategoryClick(AcdaCategory.TOWN_HALL) },
      modifier = Modifier.weight(1f)
    )

    PortalItem(
      title = "Member ID",
      icon = Icons.Default.Badge,
      iconBg = Color(0xFFF3E8FF),
      iconTint = Color(0xFF7E22CE),
      isSelected = false,
      onClick = onMembershipCardClick,
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
private fun PortalItem(
  title: String,
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB),
        shape = RoundedCornerShape(16.dp)
      )
      .clickable { onClick() }
      .testTag("portal_button_$title"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) Color(0xFFE8F5E9) else Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 11.5.sp
        ),
        color = if (isSelected) AcdaGreenPrimary else AcdaOnSurface
      )
    }
  }
}
