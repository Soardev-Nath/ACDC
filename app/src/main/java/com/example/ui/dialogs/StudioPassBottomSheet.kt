package com.example.ui.dialogs

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReservationInfo
import com.example.ui.theme.ReservedBadgeBg
import com.example.ui.theme.ReservedBadgeText
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioPassBottomSheet(
  reservation: ReservationInfo,
  onDismiss: () -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  var isWalletSaved by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFFFFF8F5),
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    modifier = modifier.testTag("studio_pass_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Digital Studio Pass",
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

      // Realistic Ticket / Pass Container
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .border(1.dp, Color(0xFFEADBCE), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp)
        ) {
          // Top Brand & Studio Badge
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "EARTH & FIRE",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.sp
                ),
                color = TerracottaPrimary
              )
              Text(
                text = " • CERAMICS",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Medium,
                  letterSpacing = 0.5.sp
                ),
                color = WarmOnSurfaceVariant
              )
            }

            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(ReservedBadgeBg)
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = "CONFIRMED",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                color = ReservedBadgeText
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Big Wheel Name & Station
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFBF1EC)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = TerracottaPrimary,
                modifier = Modifier.size(28.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = reservation.wheelName,
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp
                ),
                color = WarmOnSurface
              )
              Text(
                text = "${reservation.studio} • Pre-jam session",
                style = MaterialTheme.typography.bodyMedium,
                color = TerracottaPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Divider with ticket notch effect
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(Color(0xFFEADFD8))
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Key-Value Grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "RESERVED FOR",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                ),
                color = WarmOnSurfaceVariant
              )
              Text(
                text = reservation.holderName,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = WarmOnSurface
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "TIME WINDOW",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                ),
                color = WarmOnSurfaceVariant
              )
              Text(
                text = "5:30 – 6:30 PM",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = WarmOnSurface
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "EQUIPMENT",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = WarmOnSurfaceVariant
              )
              Text(
                text = reservation.wheelModel,
                style = MaterialTheme.typography.bodySmall,
                color = WarmOnSurface
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "PASS CODE",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = WarmOnSurfaceVariant
              )
              Text(
                text = reservation.studioPassCode,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                ),
                color = TerracottaPrimary
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Barcode Representation
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFFAF6F3))
              .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
              val barWidths = listOf(3f, 1f, 4f, 2f, 1f, 5f, 2f, 3f, 1f, 4f, 2f, 2f, 4f, 1f, 3f, 5f, 2f, 1f, 3f, 4f, 2f, 1f, 4f, 2f, 3f, 1f, 4f, 2f, 3f, 1f)
              var currentX = 10f
              val step = (size.width - 20f) / barWidths.sum()
              barWidths.forEachIndexed { i, widthMultiplier ->
                val w = widthMultiplier * step
                if (i % 2 == 0) {
                  drawRect(
                    color = Color(0xFF1E1B19),
                    topLeft = Offset(currentX, 0f),
                    size = Size(w * 0.8f, size.height)
                  )
                }
                currentX += w
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "SCAN AT STUDIO B DESK UPON ARRIVAL",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            ),
            color = WarmOnSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Button to save to wallet
      Button(
        onClick = { isWalletSaved = true },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("save_wallet_button"),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
      ) {
        Icon(
          imageVector = if (isWalletSaved) Icons.Default.CheckCircle else Icons.Default.QrCode2,
          contentDescription = null,
          tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isWalletSaved) "Saved to Google Wallet!" else "Save Pass to Google Wallet",
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
