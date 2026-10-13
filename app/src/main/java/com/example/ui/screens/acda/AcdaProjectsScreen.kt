package com.example.ui.screens.acda

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.acda.AcdaProjectCard
import com.example.ui.components.acda.AcdaTopBar
import com.example.ui.dialogs.DonateDuesDialog
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant
import com.example.ui.theme.AcdaSurface
import com.example.viewmodel.AcdaViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AcdaProjectsScreen(
  viewModel: AcdaViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val projects by viewModel.projects.collectAsState()
  val selectedChapter by viewModel.selectedChapter.collectAsState()
  val showDonateDialog by viewModel.showDonateDialog.collectAsState()
  val selectedProjectForDonation by viewModel.selectedProjectForDonation.collectAsState()

  val totalTarget = projects.sumOf { it.targetAmountNaira }
  val totalRaised = projects.sumOf { it.raisedAmountNaira }
  val formatter = NumberFormat.getNumberInstance(Locale.US)

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
      // Summary Card
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = AcdaGreenPrimary)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "COMMUNITY DEVELOPMENT FUND STATS",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
              ),
              color = Color(0xFFA2D3A7)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₦${formatter.format(totalRaised)} Raised",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
              ),
              color = Color.White
            )
            Text(
              text = "Total cumulative budget: ₦${formatter.format(totalTarget)} across 3 active projects",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = Color(0xFFE8F5E9)
            )
          }
        }
      }

      item {
        Text(
          text = "Active Infrastructure Projects",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = AcdaOnSurface,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
      }

      items(projects, key = { it.id }) { proj ->
        Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
          AcdaProjectCard(
            project = proj,
            onContributeClick = { viewModel.openDonateDialog(proj) }
          )
        }
      }
    }
  }

  if (showDonateDialog) {
    DonateDuesDialog(
      projects = projects,
      selectedProject = selectedProjectForDonation,
      onDismiss = { viewModel.closeDonateDialog() },
      onConfirmDonation = { projId, amt -> viewModel.contributeToProject(projId, amt) }
    )
  }
}
