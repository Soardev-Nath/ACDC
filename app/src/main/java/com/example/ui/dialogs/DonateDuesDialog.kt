package com.example.ui.dialogs

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Paid
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AcdaProject
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun DonateDuesDialog(
  projects: List<AcdaProject>,
  selectedProject: AcdaProject?,
  onDismiss: () -> Unit,
  onConfirmDonation: (projectId: String, amount: Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var activeProject by remember { mutableStateOf(selectedProject ?: projects.firstOrNull()) }
  var selectedAmountNaira by remember { mutableStateOf(10000L) }
  var customAmountText by remember { mutableStateOf("") }

  val presets = listOf(2000L, 5000L, 10000L, 25000L, 50000L)

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
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Community Contribution",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = AcdaGreenPrimary
            )
            Text(
              text = "ACDA Development & Welfare Fund",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
              color = AcdaOnSurfaceVariant
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

        Spacer(modifier = Modifier.height(14.dp))

        // Target Project selector
        Text(
          text = "Select Cause or Project:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = AcdaOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        projects.take(3).forEach { proj ->
          val isSelected = proj.id == activeProject?.id
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
              .border(1.dp, if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB), RoundedCornerShape(12.dp))
              .clickable { activeProject = proj }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) AcdaGreenPrimary else Color(0xFFC7DAC9))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = proj.title,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 12.sp
                ),
                color = if (isSelected) AcdaGreenPrimary else AcdaOnSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Amount Presets
        Text(
          text = "Select Amount (₦ Naira):",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = AcdaOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          presets.take(3).forEach { amount ->
            val isSelected = selectedAmountNaira == amount && customAmountText.isEmpty()
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
                .border(1.dp, if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB), RoundedCornerShape(10.dp))
                .clickable {
                  selectedAmountNaira = amount
                  customAmountText = ""
                }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "₦${amount / 1000}k",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                ),
                color = if (isSelected) AcdaGreenPrimary else AcdaOnSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          presets.drop(3).forEach { amount ->
            val isSelected = selectedAmountNaira == amount && customAmountText.isEmpty()
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
                .border(1.dp, if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB), RoundedCornerShape(10.dp))
                .clickable {
                  selectedAmountNaira = amount
                  customAmountText = ""
                }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "₦${amount / 1000}k",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                ),
                color = if (isSelected) AcdaGreenPrimary else AcdaOnSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = customAmountText,
          onValueChange = {
            customAmountText = it
            it.toLongOrNull()?.let { amt -> selectedAmountNaira = amt }
          },
          label = { Text("Or enter custom amount (₦)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("custom_donation_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Security / Transparency note
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Receipt generated directly to your ACDA profile ID.",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = AcdaOnSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            val projId = activeProject?.id ?: "proj_water_1"
            onConfirmDonation(projId, selectedAmountNaira)
            Toast.makeText(context, "Thank you! ₦$selectedAmountNaira recorded for ${activeProject?.title}", Toast.LENGTH_LONG).show()
          },
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("confirm_donation_button")
        ) {
          Icon(
            imageVector = Icons.Default.Paid,
            contentDescription = null,
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Pay ₦$selectedAmountNaira",
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
