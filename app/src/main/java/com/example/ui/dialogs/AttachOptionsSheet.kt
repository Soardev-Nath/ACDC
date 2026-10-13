package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
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
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachOptionsSheet(
  onAttachBisquePhoto: () -> Unit,
  onAttachWheelRequest: () -> Unit,
  onAttachGlazeFormula: () -> Unit,
  onAttachLocation: () -> Unit,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFFFF8F5),
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    modifier = modifier.testTag("attach_options_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .navigationBarsPadding()
    ) {
      Text(
        text = "Share with Elena",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        ),
        color = WarmOnSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      AttachOptionItem(
        icon = Icons.Default.CameraAlt,
        iconBg = Color(0xFFFFECEB),
        iconTint = TerracottaPrimary,
        title = "Take / Attach Bisqueware Photo",
        subtitle = "Share pieces from last week's session for glaze tips",
        onClick = {
          onDismiss()
          onAttachBisquePhoto()
        }
      )

      AttachOptionItem(
        icon = Icons.Default.Build,
        iconBg = Color(0xFFE7F5EA),
        iconTint = Color(0xFF2E6B3E),
        title = "Request Extra Wheel Time",
        subtitle = "Check availability for Shimpo wheels before kiln load",
        onClick = {
          onDismiss()
          onAttachWheelRequest()
        }
      )

      AttachOptionItem(
        icon = Icons.Default.Palette,
        iconBg = Color(0xFFFEF3C7),
        iconTint = Color(0xFFB45309),
        title = "Share Glaze Formula / Test Tile",
        subtitle = "Send specific gravity and pyrometric cone recipes",
        onClick = {
          onDismiss()
          onAttachGlazeFormula()
        }
      )

      AttachOptionItem(
        icon = Icons.Default.LocationOn,
        iconBg = Color(0xFFF4ECE8),
        iconTint = TerracottaPrimary,
        title = "Studio Check-in Location",
        subtitle = "Earth & Fire Studio • 428 Kiln Way, Arts District",
        onClick = {
          onDismiss()
          onAttachLocation()
        }
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun AttachOptionItem(
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(vertical = 10.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(iconBg),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(22.dp)
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = WarmOnSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = WarmOnSurfaceVariant
      )
    }
  }
}
