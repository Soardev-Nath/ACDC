package com.example.ui.screens.acda

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.acda.AcdaForumCard
import com.example.ui.components.acda.AcdaTopBar
import com.example.ui.dialogs.CreateForumPostDialog
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant
import com.example.ui.theme.AcdaSurface
import com.example.viewmodel.AcdaViewModel

@Composable
fun AcdaTownHallScreen(
  viewModel: AcdaViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  val forumPosts by viewModel.forumPosts.collectAsState()
  val selectedChapter by viewModel.selectedChapter.collectAsState()
  val showCreatePostDialog by viewModel.showCreatePostDialog.collectAsState()

  var selectedTagFilter by remember { mutableStateOf("All") }
  val tags = listOf("All", "President's Desk", "Project Updates", "Cultural Heritage", "Youth Wing")

  val filteredPosts = if (selectedTagFilter == "All") {
    forumPosts
  } else {
    forumPosts.filter { it.tag.contains(selectedTagFilter, ignoreCase = true) }
  }

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
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.setShowCreatePostDialog(true) },
        shape = CircleShape,
        containerColor = AcdaGreenPrimary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
        modifier = Modifier.padding(bottom = 72.dp)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Start Topic", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
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
            text = "Afo Community Town Hall",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp
            ),
            color = AcdaOnSurface
          )
          Text(
            text = "Open dialogue, consultative proposals & community feedback",
            style = MaterialTheme.typography.bodyMedium,
            color = AcdaOnSurfaceVariant
          )
        }
      }

      // Filter tags row
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          tags.forEach { tag ->
            val isSelected = tag == selectedTagFilter
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFE8F5E9) else Color.White)
                .border(1.dp, if (isSelected) AcdaGreenPrimary else Color(0xFFDBE8DB), CircleShape)
                .clickable { selectedTagFilter = tag }
                .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
              Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 12.sp
                ),
                color = if (isSelected) AcdaGreenPrimary else AcdaOnSurfaceVariant
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
      }

      items(filteredPosts, key = { it.id }) { post ->
        Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
          AcdaForumCard(
            post = post,
            onLikeClick = { viewModel.toggleLikePost(post.id) },
            onClick = {
              Toast.makeText(context, "Viewing: ${post.title}", Toast.LENGTH_SHORT).show()
            }
          )
        }
      }
    }
  }

  if (showCreatePostDialog) {
    CreateForumPostDialog(
      onDismiss = { viewModel.setShowCreatePostDialog(false) },
      onSubmit = { title, content, tag ->
        viewModel.submitForumPost(title, content, tag)
        Toast.makeText(context, "Posted to Town Hall!", Toast.LENGTH_SHORT).show()
      }
    )
  }
}
