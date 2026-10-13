package com.example.ui.screens.acda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.R
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.AcdaGold
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppLightGreen
import com.example.ui.theme.WhatsAppPrimary
import com.example.ui.theme.WhatsAppPrimaryDark
import com.example.viewmodel.AcdaViewModel
import com.example.viewmodel.WhatsAppChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcdaHomeScreen(
  userProfile: UserProfile?,
  acdaViewModel: AcdaViewModel,
  chatViewModel: WhatsAppChatViewModel,
  onNavigateToChat: () -> Unit,
  onNavigateToProjects: () -> Unit,
  onNavigateToTownHall: () -> Unit,
  onNavigateToAdmin: () -> Unit,
  onNavigateToProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val projects by acdaViewModel.projects.collectAsState()
  val topics by acdaViewModel.topics.collectAsState()
  val channels by chatViewModel.allChannels.collectAsState()
  val unreadCount by chatViewModel.totalUnreadCount.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(R.drawable.img_acda_logo),
              contentDescription = "ACDA Logo",
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .border(1.5.dp, AcdaGold, CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "ACDA Community",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = userProfile?.branch ?: "National Secretariat",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp
              )
            }
          }
        },
        actions = {
          if (userProfile?.isAdmin == true) {
            Surface(
              color = AcdaGold,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.padding(end = 8.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = "Admin",
                  tint = Color.Black,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "ADMIN",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.Black
                )
              }
            }
          }
          IconButton(onClick = onNavigateToChat) {
            Icon(Icons.Default.Chat, contentDescription = "Chats", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
      )
    },
    modifier = modifier.testTag("acda_home_screen")
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Executive Welcome & Identity Banner
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .background(WhatsAppPrimary.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (userProfile?.isAdmin == true) Icons.Default.AdminPanelSettings else Icons.Default.Badge,
                contentDescription = null,
                tint = if (userProfile?.isAdmin == true) AcdaRed else WhatsAppPrimary,
                modifier = Modifier.size(30.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Welcome, ${userProfile?.name ?: "Member"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
              Text(
                text = "Membership ID: ${userProfile?.membershipId ?: "ACDA-000000"}",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
              Text(
                text = if (userProfile?.isAdmin == true) "Role: Full System Administrator" else "Role: Verified Community Member",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (userProfile?.isAdmin == true) AcdaRed else WhatsAppPrimary
              )
            }
          }
        }
      }

      // 2. WhatsApp Community Live Portal Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = WhatsAppPrimaryDark
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToChat() }
            .testTag("whatsapp_portal_card")
        ) {
          Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(WhatsAppLightGreen, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "ACDA WhatsApp Community",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                  color = WhatsAppLightGreen,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "LIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "${channels.size} Active Channels • NEC, Youth Wing & Bulletins",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 2.dp)
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open Chat",
              tint = Color.White
            )
          }
        }
      }

      // 3. Quick Portals Grid
      item {
        Text(
          text = "Community Portals",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A),
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          PortalTile(
            title = "Projects",
            subtitle = "${projects.size} Tracked",
            icon = Icons.Default.Build,
            color = WhatsAppPrimary,
            onClick = onNavigateToProjects,
            modifier = Modifier.weight(1f)
          )
          PortalTile(
            title = "Town Hall",
            subtitle = "${topics.size} Motions",
            icon = Icons.Default.Forum,
            color = Color(0xFF0284C7),
            onClick = onNavigateToTownHall,
            modifier = Modifier.weight(1f)
          )
          PortalTile(
            title = if (userProfile?.isAdmin == true) "Admin Panel" else "ID Card",
            subtitle = if (userProfile?.isAdmin == true) "Full Access" else "Verified",
            icon = if (userProfile?.isAdmin == true) Icons.Default.AdminPanelSettings else Icons.Default.Badge,
            color = if (userProfile?.isAdmin == true) AcdaRed else AcdaGold,
            onClick = if (userProfile?.isAdmin == true) onNavigateToAdmin else onNavigateToProfile,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // 4. Featured Development Projects
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Active Community Projects",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Text(
            text = "View All",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = WhatsAppPrimary,
            modifier = Modifier.clickable { onNavigateToProjects() }
          )
        }
      }

      items(projects.take(2)) { project ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToProjects() }
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = project.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                modifier = Modifier.weight(1f)
              )
              Surface(
                color = WhatsAppPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = project.status,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = WhatsAppPrimary,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = project.description,
              fontSize = 12.sp,
              color = Color(0xFF64748B),
              maxLines = 2,
              modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { project.progressPercent },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
              color = WhatsAppPrimary,
              trackColor = Color(0xFFE2E8F0)
            )
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Progress: ${(project.progressPercent * 100).toInt()}%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569)
              )
              Text(
                text = "Target: ₦${String.format("%,.0f", project.targetBudget)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569)
              )
            }
          }
        }
      }

      // 5. Featured Town Hall Deliberation
      if (topics.isNotEmpty()) {
        item {
          Text(
            text = "Upcoming Town Hall Motion",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        }
        item {
          val topic = topics.first()
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onNavigateToTownHall() }
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = topic.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
              Text(
                text = topic.description,
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                maxLines = 2,
                modifier = Modifier.padding(vertical = 4.dp)
              )
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Speaker: ${topic.speaker}",
                  fontSize = 11.sp,
                  color = WhatsAppPrimary,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "${topic.upvotesCount} community upvotes",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0284C7)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PortalTile(
  title: String,
  subtitle: String,
  icon: ImageVector,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .background(color.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A),
        maxLines = 1
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = Color(0xFF64748B),
        maxLines = 1
      )
    }
  }
}
