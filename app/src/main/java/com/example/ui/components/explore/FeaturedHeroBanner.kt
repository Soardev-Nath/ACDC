package com.example.ui.components.explore

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun FeaturedHeroBanner(
  isJoined: Boolean,
  onJoinClick: () -> Unit,
  onBannerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val pulseAnim = remember { Animatable(1f) }

  LaunchedEffect(Unit) {
    pulseAnim.animateTo(
      targetValue = 0.4f,
      animationSpec = infiniteRepeatable(
        animation = tween(800, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
      )
    )
  }

  val heroImageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBgJZB4cWWKSTU-fWdAyrC-HOWYi93Ahx53nOJ5Pz_8tKcVDPuHq2pXcERs8M3Gbm7Qz94Fb6DcrOW9iYqi7vjoOoTqMb65WNxpg_VWCfDaht3P-C_ITicM2E4dA1cQ9XRIhUa59tvtvZeD78-DiQXaZi4eOB5B-G5wpv8xpxKP8OnWBl7Z45JIMEa9sShK8SloarRGi5hEqXszZ7z1n1TW8mJ2NH9zBct9keJoOozFnPiuLG3w12tY"

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Color(0xFFEADFD9), RoundedCornerShape(20.dp))
      .clickable { onBannerClick() }
      .testTag("featured_hero_banner"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column {
      // Image Container with overlays
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(176.dp)
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(heroImageUrl)
            .crossfade(true)
            .error(R.drawable.img_studio_banner)
            .placeholder(R.drawable.img_studio_banner)
            .build(),
          contentDescription = "Scenic redwood trail runners",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth().height(176.dp)
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color(0x66221E1C),
                  Color(0xDD221E1C)
                )
              )
            )
        )

        // Top Left: Live Status Badge
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.95f))
            .border(1.dp, Color(0xFFEADFD9), CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(OnlineGreen.copy(alpha = pulseAnim.value))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Running tomorrow 8:00 AM",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              ),
              color = Color(0xFF1E1B19)
            )
          }
        }

        // Top Right: Featured Tag
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp)
            .clip(CircleShape)
            .background(TerracottaPrimary)
            .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
          Text(
            text = "Featured",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
        }

        // Bottom text block over image
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        ) {
          Text(
            text = "Weekend Trail Runners",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 21.sp
            ),
            color = Color.White
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Groups,
              contentDescription = null,
              tint = Color(0xFFE9E1DD),
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "1.2k members • Redwood Regional Park",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.5.sp
              ),
              color = Color(0xFFE9E1DD)
            )
          }
        }
      }

      // Hero Footer Action Lockup
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Facepile & mutuals
        Row(verticalAlignment = Alignment.CenterVertically) {
          val facepileUrls = listOf(
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCcsZd1MkxiDQTURSnkNvsVWMuWGYjgaoeTyLqQCQFHsFCT14wN6-sTJy4cuNxcKojO7_0pqmsRZQhIOW9nN_N69TE-BF--RyiynsDyf03wNhTtu4fNWka3Sz6Ee-AM0eD68L14i9GfzGRYEAc9fBRWPFdrSlAk-iFSYNZAEZDDSJylzntIYXWkJzG7hYwtvxOp0bJNGbnWR1S_OPK3BNARXj9jllXAGzL2XJm4zsbkzB9nhFeTQaMt",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAX2dn3nLL1U118kTSVOZBDnzWomwWoSmDZ4PV4SaXrkH9dat55DfqD1KSzGarsHX63poYVzzCuGufdj1NJBA96YQVvW1qKznS5cTB7HPZvg-wQn-AoRynWSRMP00-Y-eOFi6aRQ5XOmPi2jKjBTwUHxXCb7htDM2_2rhsSqSpVxDtIczrMzWjiCrIllLhoxRmozjPOxxiWYM6xFulCV9op7qmOv94kij1VRLRSIJyGRGeIb8u2AcBO",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCO9d-6FJi54_lrjxtSQtOUOTQkzW_70Tc1NdBeoIYc8xnITINSIisb8QcD5M8GBsCjtrkSJQ9N8wAwMh_ZjGJXST1pJa61mOT0ZAO9rUtsJmOvykenFwmQQgEp7huqGiNyNJoCbJgiNM6C8n7ysT5pOlAs-kehWpTpvAW83cB6j7aZbjDgVnVlX5Yy99fWhOWNaI_PIkRZCqUPyk8PruZ-iOz16170V-8r4uCdjb4Zr1JRCSUTcnnv"
          )

          Box {
            facepileUrls.forEachIndexed { i, url ->
              AsyncImage(
                model = ImageRequest.Builder(context)
                  .data(url)
                  .crossfade(true)
                  .error(R.drawable.img_elena_avatar)
                  .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .offset(x = (i * 18).dp)
                  .size(28.dp)
                  .clip(CircleShape)
                  .border(2.dp, Color.White, CircleShape)
              )
            }
          }

          Spacer(modifier = Modifier.width(66.dp))

          Text(
            text = "+38 mutuals",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 11.5.sp
            ),
            color = WarmOnSurfaceVariant
          )
        }

        // Quick Join button
        Button(
          onClick = onJoinClick,
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isJoined) Color(0xFFFAF2EE) else TerracottaPrimary,
            contentColor = if (isJoined) TerracottaPrimary else Color.White
          ),
          modifier = Modifier
            .height(38.dp)
            .testTag("quick_join_button")
        ) {
          Icon(
            imageVector = if (isJoined) Icons.Default.Check else Icons.Default.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isJoined) "Joined ✓" else "Quick Join",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          )
        }
      }
    }
  }
}
