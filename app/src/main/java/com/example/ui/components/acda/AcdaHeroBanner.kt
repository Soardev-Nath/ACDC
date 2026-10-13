package com.example.ui.components.acda

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary

@Composable
fun AcdaHeroBanner(
  onContributeClick: () -> Unit,
  onViewMemberCardClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .clip(RoundedCornerShape(22.dp))
      .border(1.dp, Color(0xFFC7DAC9), RoundedCornerShape(22.dp))
      .testTag("acda_hero_banner"),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      // Scenic Image of Afo hills with text overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_afo_hills),
          contentDescription = "Afo Homeland Hills & River Valley",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth().height(180.dp)
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0x330A240E),
                  Color(0x880A240E),
                  Color(0xE60A240E)
                )
              )
            )
        )

        // Top Tag
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp)
            .clip(CircleShape)
            .background(Color(0xE6FFFFFF))
            .border(1.dp, AcdaGreenPrimary.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Handshake,
              contentDescription = null,
              tint = AcdaGreenPrimary,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "UNITY • PROGRESS • PEACE",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
              ),
              color = AcdaGreenPrimary
            )
          }
        }

        // Bottom text block
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        ) {
          Text(
            text = "Together We Build the Afo Nation",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            ),
            color = Color.White
          )
          Text(
            text = "Official Platform for Community Infrastructure, Welfare & Cultural Heritage",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp
            ),
            color = Color(0xFFE8F5E9),
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      // Quick CTA buttons row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF9FCF9))
          .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = onContributeClick,
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("hero_contribute_button")
        ) {
          Icon(
            imageVector = Icons.Default.Paid,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Pay Dues / Donate",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color.White
            )
          )
        }

        OutlinedButton(
          onClick = onViewMemberCardClick,
          shape = CircleShape,
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("hero_member_pass_button"),
          border = androidx.compose.foundation.BorderStroke(1.dp, AcdaGreenPrimary)
        ) {
          Icon(
            imageVector = Icons.Default.Badge,
            contentDescription = null,
            tint = AcdaGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Digital ID Card",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = AcdaGreenPrimary
            )
          )
        }
      }
    }
  }
}
