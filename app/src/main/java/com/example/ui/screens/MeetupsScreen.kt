package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.explore.HappeningThisWeekSection
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant
import com.example.ui.theme.WarmSurface
import com.example.viewmodel.ExploreViewModel

@Composable
fun MeetupsScreen(
  viewModel: ExploreViewModel,
  onOpenPotteryMeetup: () -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  val meetups by viewModel.meetups.collectAsState()
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
            text = "Upcoming Club Meetups",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp
            ),
            color = WarmOnSurface
          )
          Text(
            text = "Hands-on studio jams, morning shakeouts, and photo walks",
            style = MaterialTheme.typography.bodyMedium,
            color = WarmOnSurfaceVariant
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(12.dp))
        HappeningThisWeekSection(
          meetups = meetups,
          onMeetupClick = { item ->
            if (item.id == "meetup_pottery") {
              onOpenPotteryMeetup()
            } else {
              Toast.makeText(context, "${item.title} • ${item.timeAndDuration}", Toast.LENGTH_SHORT).show()
            }
          },
          onRsvpClick = { item ->
            viewModel.toggleMeetupRsvp(item.id)
            val msg = if (!item.isRsvped) "RSVP confirmed for ${item.title}!" else "Cancelled RSVP"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          onSeeAllClick = {}
        )
      }
    }
  }
}
