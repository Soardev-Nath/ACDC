package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.AcdaGold
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  userProfile: UserProfile?,
  authViewModel: AuthViewModel,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Official Membership Card",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
      )
    },
    modifier = modifier.testTag("profile_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      // 1. Digital Membership ID Card (Realistic Badge)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  WhatsAppPrimaryDark,
                  Color(0xFF0F172A),
                  Color(0xFF1E3A8A)
                )
              )
            )
            .border(2.dp, AcdaGold, RoundedCornerShape(16.dp))
            .padding(20.dp)
        ) {
          Column {
            // Card Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Diversity3, contentDescription = null, tint = AcdaGold, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "ACDA COMMUNITY",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                  )
                  Text(
                    text = "National Identity & Secretariat",
                    fontSize = 10.sp,
                    color = AcdaGold
                  )
                }
              }

              Surface(
                color = if (userProfile?.isAdmin == true) AcdaRed else WhatsAppPrimary,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = if (userProfile?.isAdmin == true) "EXECUTIVE ADMIN" else "VERIFIED MEMBER",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Photo & Credentials
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(72.dp)
                  .background(Color.White.copy(alpha = 0.1f), CircleShape)
                  .border(2.dp, AcdaGold, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (userProfile?.isAdmin == true) Icons.Default.AdminPanelSettings else Icons.Default.Badge,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(40.dp)
                )
              }

              Spacer(modifier = Modifier.width(16.dp))

              Column {
                Text(
                  text = userProfile?.name?.ifEmpty { "Community Member" } ?: "Community Member",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Text(
                  text = userProfile?.email ?: "",
                  fontSize = 12.sp,
                  color = Color.White.copy(alpha = 0.75f)
                )
                Text(
                  text = "Branch: ${userProfile?.branch ?: "National Headquarters"}",
                  fontSize = 12.sp,
                  color = AcdaGold,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Card Footer with Membership ID and QR simulation
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "MEMBERSHIP IDENTIFIER",
                  fontSize = 9.sp,
                  color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                  text = userProfile?.membershipId ?: "ACDA-000000",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontFamily = FontFamily.Monospace
                )
              }

              Icon(
                imageVector = Icons.Default.QrCode2,
                contentDescription = "QR Code",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
              )
            }
          }
        }
      }

      // 2. Member Information Cards
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          ProfileInfoRow(icon = Icons.Default.Email, label = "Email Address", value = userProfile?.email ?: "Not available")
          HorizontalDivider(color = Color(0xFFF1F5F9))
          ProfileInfoRow(icon = Icons.Default.LocationOn, label = "Registered Branch", value = userProfile?.branch ?: "National Secretariat")
          HorizontalDivider(color = Color(0xFFF1F5F9))
          ProfileInfoRow(
            icon = Icons.Default.Security,
            label = "Access Privileges",
            value = if (userProfile?.isAdmin == true) "Full System Administrator (CRUD on all components)" else "Standard Member"
          )
        }
      }

      // 3. Sign Out Button
      Button(
        onClick = { authViewModel.signOut() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFEF4444)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("sign_out_button")
      ) {
        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign Out of ACDA", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }
    }
  }
}

@Composable
private fun ProfileInfoRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(icon, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(20.dp))
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(label, fontSize = 11.sp, color = Color(0xFF64748B))
      Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
  }
}
