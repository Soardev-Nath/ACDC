package com.example.ui.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
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
import com.example.model.ExploreFilterCategory
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.SageSecondary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun ExploreFilterChips(
  selectedFilter: ExploreFilterCategory,
  onFilterSelected: (ExploreFilterCategory) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    ExploreFilterCategory.values().forEach { category ->
      val isSelected = category == selectedFilter

      val icon: ImageVector = when (category) {
        ExploreFilterCategory.ALL -> Icons.Default.AutoAwesome
        ExploreFilterCategory.CERAMICS -> Icons.Default.Palette
        ExploreFilterCategory.TRAIL -> Icons.Default.DirectionsRun
        ExploreFilterCategory.PHOTO -> Icons.Default.CameraAlt
        ExploreFilterCategory.BOOKS -> Icons.Default.MenuBook
      }

      val iconColor = when {
        isSelected -> TerracottaPrimary
        category == ExploreFilterCategory.CERAMICS -> AmberTertiary
        category == ExploreFilterCategory.TRAIL -> SageSecondary
        category == ExploreFilterCategory.PHOTO -> TerracottaPrimary
        category == ExploreFilterCategory.BOOKS -> SageSecondary
        else -> WarmOnSurfaceVariant
      }

      Box(
        modifier = Modifier
          .height(34.dp)
          .clip(CircleShape)
          .background(
            if (isSelected) Color(0xFFFFDBD1).copy(alpha = 0.6f) else Color(0xFFFAF2EE)
          )
          .border(
            width = 1.dp,
            color = if (isSelected) TerracottaPrimary else Color(0xFFEADBCE),
            shape = CircleShape
          )
          .clickable { onFilterSelected(category) }
          .padding(horizontal = 14.dp)
          .testTag("filter_chip_${category.name}"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = category.label,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
              fontSize = 12.5.sp
            ),
            color = if (isSelected) TerracottaPrimary else WarmOnSurfaceVariant
          )
        }
      }
    }
  }
}
