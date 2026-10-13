package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.AcdaMemberProfile
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun DigitalMembershipCardDialog(
  profile: AcdaMemberProfile,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 20.dp),
      shape = RoundedCornerShape(24.dp),
      color = Color(0xFFF9FCF9),
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Official ACDA Digital ID",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = AcdaGreenPrimary
          )

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = AcdaOnSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Official Card Graphic
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, AcdaGreenPrimary, RoundedCornerShape(20.dp))
            .testTag("acda_digital_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            // Card Header with Logo
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Image(
                painter = painterResource(id = R.drawable.img_acda_logo),
                contentDescription = "ACDA Seal",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .border(1.5.dp, AcdaGreenPrimary, CircleShape)
              )

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "AFO COMMUNITY DEVELOPMENT ASSOCIATION",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                  ),
                  color = AcdaGreenPrimary
                )
                Text(
                  text = "ACDA • NATIONAL MEMBERSHIP IDENTITY",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                  ),
                  color = AcdaGoldAccent
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFDBE8DB))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Member Info Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Avatar
              Image(
                painter = painterResource(id = R.drawable.img_elena_avatar),
                contentDescription = profile.memberName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(56.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.5.dp, AcdaGreenPrimary, RoundedCornerShape(12.dp))
              )

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = profile.memberName,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                  ),
                  color = AcdaOnSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = AcdaGreenPrimary,
                    modifier = Modifier.size(13.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = profile.membershipType,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.SemiBold
                    ),
                    color = AcdaGreenPrimary
                  )
                }
                Text(
                  text = profile.chapter,
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = AcdaOnSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = "MEMBERSHIP ID",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  color = AcdaOnSurfaceVariant
                )
                Text(
                  text = profile.memberId,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                  ),
                  color = AcdaGreenPrimary
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "ANNUAL DUES",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  color = AcdaOnSurfaceVariant
                )
                Text(
                  text = profile.duesStatus,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  ),
                  color = Color(0xFF2E7D32)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barcode representation
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFF1F7F1))
                .padding(horizontal = 14.dp, vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Canvas(modifier = Modifier.fillMaxWidth().height(32.dp)) {
                val barWidths = listOf(3f, 1f, 4f, 2f, 1f, 5f, 2f, 3f, 1f, 4f, 2f, 2f, 4f, 1f, 3f, 5f, 2f, 1f, 3f, 4f, 2f, 1f, 4f, 2f, 3f, 1f, 4f, 2f)
                var currentX = 10f
                val step = (size.width - 20f) / barWidths.sum()
                barWidths.forEachIndexed { i, widthMultiplier ->
                  val w = widthMultiplier * step
                  if (i % 2 == 0) {
                    drawRect(
                      color = Color(0xFF142116),
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
              text = "OFFICIALLY REGISTERED • ACDA NATIONAL SECRETARIAT",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              ),
              color = AcdaOnSurfaceVariant,
              modifier = Modifier.align(Alignment.CenterHorizontally)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            Toast.makeText(context, "ACDA Digital ID Card saved to device wallet!", Toast.LENGTH_SHORT).show()
            onDismiss()
          },
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Save ID Card to Device",
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
