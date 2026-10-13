package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElenaProfileSheet(
  onDismiss: () -> Unit,
  onStartCall: () -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFFFF8F5),
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    modifier = modifier.testTag("elena_profile_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 6.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = WarmOnSurfaceVariant
          )
        }
      }

      // Large Avatar
      Box(contentAlignment = Alignment.BottomEnd) {
        Image(
          painter = painterResource(id = R.drawable.img_elena_avatar),
          contentDescription = "Elena Rostova",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .border(2.dp, TerracottaPrimary, CircleShape)
        )
        Box(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(OnlineGreen)
            .border(3.dp, Color.White, CircleShape)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Elena Rostova",
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
          color = WarmOnSurface
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Verified Master",
          tint = TerracottaPrimary,
          modifier = Modifier.size(20.dp)
        )
      }

      Text(
        text = "Founder & Master Potter • Earth & Fire Studio",
        style = MaterialTheme.typography.bodyMedium,
        color = TerracottaPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Stats row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        ProfileStat(number = "12 yrs", label = "Ceramics")
        ProfileStat(number = "84", label = "Kiln Firings")
        ProfileStat(number = "4.9 ★", label = "Host Rating")
      }

      Spacer(modifier = Modifier.height(20.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0E5DF))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Studio Specializations",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = WarmOnSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "High-fire stoneware, Cone 6 reduction atmospheres, Rutile micro-crystallization, and large sculptural vessel wheel throwing.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = WarmOnSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Button(
          onClick = {
            onDismiss()
            onStartCall()
          },
          modifier = Modifier
            .weight(1f)
            .height(48.dp),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
        ) {
          Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Call Studio", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onDismiss,
          modifier = Modifier
            .weight(1f)
            .height(48.dp),
          shape = RoundedCornerShape(24.dp)
        ) {
          Text("Send Message", fontWeight = FontWeight.SemiBold, color = TerracottaPrimary)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun ProfileStat(number: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = number,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp
      ),
      color = WarmOnSurface
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
      color = WarmOnSurfaceVariant
    )
  }
}
