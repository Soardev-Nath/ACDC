package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.explore.ExploreTopBar
import com.example.ui.components.explore.PopularClubsSection
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant
import com.example.ui.theme.WarmSurface
import com.example.viewmodel.ExploreViewModel

@Composable
fun ClubsScreen(
  viewModel: ExploreViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  val popularClubs by viewModel.popularClubs.collectAsState()
  val selectedLocation by viewModel.selectedLocation.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = WarmSurface,
    topBar = {
      ExploreTopBar(
        currentLocation = selectedLocation,
        onLocationSelected = { viewModel.onLocationSelected(it) },
        onNotificationsClick = {},
        onExploreMenuClick = onNavigateBack
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      item {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
          Text(
            text = "Neighborhood Clubs",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp
            ),
            color = WarmOnSurface
          )
          Text(
            text = "Interest-driven craft, outdoor, culinary & art guilds across $selectedLocation",
            style = MaterialTheme.typography.bodyMedium,
            color = WarmOnSurfaceVariant
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(12.dp))
        PopularClubsSection(
          clubs = popularClubs,
          onClubClick = { club ->
            Toast.makeText(context, "${club.name} • ${club.tagline}", Toast.LENGTH_SHORT).show()
          },
          onJoinClick = { club ->
            viewModel.toggleClubJoin(club.id)
            val msg = if (!club.isJoined) "Joined ${club.name}!" else "Left ${club.name}"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          onExploreAllClick = {}
        )
      }
    }
  }
}
