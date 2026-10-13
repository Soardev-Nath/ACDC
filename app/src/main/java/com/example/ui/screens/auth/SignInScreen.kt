package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AcdaGold
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
  viewModel: AuthViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isLoading by viewModel.isLoading.collectAsState()
  val errorMessage by viewModel.errorMessage.collectAsState()
  val webClientId = stringResource(R.string.default_web_client_id)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            WhatsAppPrimaryDark,
            WhatsAppPrimary,
            Color(0xFF0F172A)
          )
        )
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // ACDA Emblem
      Image(
        painter = painterResource(R.drawable.img_acda_logo),
        contentDescription = "ACDA Community Logo",
        modifier = Modifier
          .size(108.dp)
          .clip(CircleShape)
          .border(2.5.dp, AcdaGold, CircleShape)
      )

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "ACDA COMMUNITY",
        fontSize = 26.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        letterSpacing = 1.sp
      )

      Text(
        text = "Official Community & Administration Portal",
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.85f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 6.dp)
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Production features card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          FeatureRow(
            icon = Icons.Default.Chat,
            title = "WhatsApp Real-Time Chat",
            desc = "Official channels, group messages & broadcast announcements"
          )
          Spacer(modifier = Modifier.height(14.dp))
          FeatureRow(
            icon = Icons.Default.AdminPanelSettings,
            title = "Executive Administration Backend",
            desc = "Full administrative access to manage projects, topics & members"
          )
          Spacer(modifier = Modifier.height(14.dp))
          FeatureRow(
            icon = Icons.Default.Security,
            title = "Secure Cloud Database",
            desc = "Production Google Identity authentication with live Firestore backend"
          )
        }
      }

      Spacer(modifier = Modifier.height(36.dp))

      // Google Sign-In Button
      Button(
        onClick = { viewModel.signInWithGoogle(context, webClientId) },
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color(0xFF1E293B)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("google_sign_in_button")
      ) {
        if (isLoading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = WhatsAppPrimary,
            strokeWidth = 2.dp
          )
        } else {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = WhatsAppPrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "Sign in with Google",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
          }
        }
      }

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
          color = Color(0xFFEF4444).copy(alpha = 0.2f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = errorMessage ?: "",
            color = Color(0xFFFCA5A5),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun FeatureRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  desc: String
) {
  Row(verticalAlignment = Alignment.Top) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .background(WhatsAppPrimary.copy(alpha = 0.12f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(20.dp))
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
      Text(text = desc, fontSize = 12.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
    }
  }
}
