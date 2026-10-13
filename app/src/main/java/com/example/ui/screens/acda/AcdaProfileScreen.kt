package com.example.ui.screens.acda

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AcdaExecutive
import com.example.ui.components.acda.AcdaTopBar
import com.example.ui.dialogs.DigitalMembershipCardDialog
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant
import com.example.ui.theme.AcdaSurface
import com.example.viewmodel.AcdaViewModel

@Composable
fun AcdaProfileScreen(
  viewModel: AcdaViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  val memberProfile by viewModel.memberProfile.collectAsState()
  val executives by viewModel.executives.collectAsState()
  val selectedChapter by viewModel.selectedChapter.collectAsState()
  val showMembershipCard by viewModel.showMembershipCard.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = AcdaSurface,
    topBar = {
      AcdaTopBar(
        currentChapter = selectedChapter,
        onChapterSelected = { viewModel.setChapter(it) },
        onMembershipClick = { viewModel.setShowMembershipCard(true) },
        onNotificationsClick = {}
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // Member Header Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                painter = painterResource(id = R.drawable.img_elena_avatar),
                contentDescription = memberProfile.memberName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
                  .border(2.dp, AcdaGreenPrimary, CircleShape)
              )

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = memberProfile.memberName,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                  ),
                  color = AcdaOnSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = AcdaGreenPrimary,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = memberProfile.membershipType,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 12.sp
                    ),
                    color = AcdaGreenPrimary
                  )
                }
                Text(
                  text = "ID: ${memberProfile.memberId}",
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                  ),
                  color = AcdaOnSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "STATUS: ${memberProfile.status}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp
                  ),
                  color = AcdaGreenPrimary
                )
                Text(
                  text = memberProfile.duesStatus,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp
                  ),
                  color = Color(0xFF2E7D32)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = { viewModel.setShowMembershipCard(true) },
              shape = CircleShape,
              colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
            ) {
              Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = Color.White)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Open Digital Certificate Pass", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // About ACDA & History
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "About the Afo (Eloyi) Nation",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              ),
              color = AcdaOnSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "The Afo (Eloyi) community is an ancient people endowed with fertile river basins, rich agricultural heritage, and rolling hills across Nasarawa and Benue. The Afo Community Development Association (ACDA) stands as the supreme socio-cultural umbrella body uniting all sons and daughters at home and in the diaspora towards collective prosperity, education, peace, and sustainable infrastructure.",
              style = MaterialTheme.typography.bodySmall.copy(
                lineHeight = 19.sp,
                fontSize = 12.5.sp
              ),
              color = AcdaOnSurfaceVariant
            )
          }
        }
      }

      // Executive Council Directory
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Central Executive Committee Directory",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          color = AcdaOnSurface,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
      }

      items(executives) { exec ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = exec.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = AcdaOnSurface
              )
              Text(
                text = "${exec.role} • ${exec.chapter}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                color = AcdaGreenPrimary
              )
            }

            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable {
                  Toast.makeText(context, "Contact: ${exec.contact}", Toast.LENGTH_SHORT).show()
                },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Contact",
                tint = AcdaGreenPrimary,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }

  if (showMembershipCard) {
    DigitalMembershipCardDialog(
      profile = memberProfile,
      onDismiss = { viewModel.setShowMembershipCard(false) }
    )
  }
}
