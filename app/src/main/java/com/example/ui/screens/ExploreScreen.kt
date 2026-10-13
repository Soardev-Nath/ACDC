package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExploreFilterCategory
import com.example.model.MeetupCardItem
import com.example.ui.components.explore.CreatePostOrMeetupDialog
import com.example.ui.components.explore.ExploreFilterChips
import com.example.ui.components.explore.ExploreSearchBar
import com.example.ui.components.explore.ExploreTopBar
import com.example.ui.components.explore.FeaturedHeroBanner
import com.example.ui.components.explore.HappeningThisWeekSection
import com.example.ui.components.explore.PopularClubsSection
import com.example.ui.components.explore.TrendingDiscussionsSection
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmSurface
import com.example.viewmodel.ExploreViewModel

@Composable
fun ExploreScreen(
  viewModel: ExploreViewModel,
  onOpenPotteryMeetup: () -> Unit,
  onOpenPotteryChat: () -> Unit,
  onSeeAllMeetups: () -> Unit,
  onExploreAllClubs: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedFilter by viewModel.selectedFilter.collectAsState()
  val selectedLocation by viewModel.selectedLocation.collectAsState()
  val isFeaturedJoined by viewModel.isFeaturedJoined.collectAsState()
  val meetups by viewModel.meetups.collectAsState()
  val discussions by viewModel.discussions.collectAsState()
  val popularClubs by viewModel.popularClubs.collectAsState()
  val showCreateDialog by viewModel.showCreateDialog.collectAsState()

  // Filter items based on selected category & search query
  val filteredMeetups = meetups.filter { meetup ->
    val matchesCategory = when (selectedFilter) {
      ExploreFilterCategory.ALL -> true
      ExploreFilterCategory.CERAMICS -> meetup.clubName.contains("Clay", ignoreCase = true) || meetup.title.contains("Pottery", ignoreCase = true)
      ExploreFilterCategory.TRAIL -> meetup.clubName.contains("Trail", ignoreCase = true) || meetup.title.contains("Run", ignoreCase = true)
      ExploreFilterCategory.PHOTO -> meetup.clubName.contains("Photo", ignoreCase = true)
      ExploreFilterCategory.BOOKS -> true
    }
    val matchesSearch = searchQuery.isEmpty() ||
      meetup.title.contains(searchQuery, ignoreCase = true) ||
      meetup.clubName.contains(searchQuery, ignoreCase = true)

    matchesCategory && matchesSearch
  }

  val filteredDiscussions = discussions.filter { post ->
    val matchesCategory = when (selectedFilter) {
      ExploreFilterCategory.ALL -> true
      ExploreFilterCategory.CERAMICS -> post.clubName.contains("Sourdough", ignoreCase = true) || post.title.contains("loaf", ignoreCase = true)
      ExploreFilterCategory.TRAIL -> false
      ExploreFilterCategory.PHOTO -> post.clubName.contains("Photo", ignoreCase = true)
      ExploreFilterCategory.BOOKS -> false
    }
    val matchesSearch = searchQuery.isEmpty() ||
      post.title.contains(searchQuery, ignoreCase = true) ||
      post.clubName.contains(searchQuery, ignoreCase = true) ||
      post.authorName.contains(searchQuery, ignoreCase = true)

    matchesCategory && matchesSearch
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = WarmSurface,
    topBar = {
      ExploreTopBar(
        currentLocation = selectedLocation,
        onLocationSelected = { loc ->
          viewModel.onLocationSelected(loc)
          Toast.makeText(context, "Location set to $loc", Toast.LENGTH_SHORT).show()
        },
        onNotificationsClick = {
          Toast.makeText(context, "You have 2 upcoming club updates", Toast.LENGTH_SHORT).show()
        },
        onExploreMenuClick = {
          Toast.makeText(context, "Clubhouse: East Bay & San Francisco", Toast.LENGTH_SHORT).show()
        }
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.setShowCreateDialog(true) },
        shape = CircleShape,
        containerColor = TerracottaPrimary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
        modifier = Modifier
          .padding(bottom = 72.dp)
          .testTag("fab_create_post")
      ) {
        androidx.compose.foundation.layout.Row(
          modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "New Post / Host Meetup",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "New Post / Host Meetup",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp
            )
          )
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("explore_main_scroll"),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // 1. Search Bar
      item {
        Spacer(modifier = Modifier.height(4.dp))
        ExploreSearchBar(
          query = searchQuery,
          onQueryChanged = { viewModel.onSearchQueryChanged(it) },
          onFilterClick = {
            Toast.makeText(context, "Refining tags & distance radius", Toast.LENGTH_SHORT).show()
          }
        )
      }

      // 2. Filter Chips Row
      item {
        Spacer(modifier = Modifier.height(10.dp))
        ExploreFilterChips(
          selectedFilter = selectedFilter,
          onFilterSelected = { viewModel.onFilterSelected(it) }
        )
      }

      // 3. Featured Club Hero Banner (shown if All or Trail)
      if (selectedFilter == ExploreFilterCategory.ALL || selectedFilter == ExploreFilterCategory.TRAIL) {
        item {
          Spacer(modifier = Modifier.height(16.dp))
          FeaturedHeroBanner(
            isJoined = isFeaturedJoined,
            onJoinClick = {
              viewModel.toggleFeaturedJoin()
              val msg = if (!isFeaturedJoined) "Joined Weekend Trail Runners!" else "Left club"
              Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            },
            onBannerClick = {
              Toast.makeText(context, "Weekend Trail Runners • 1.2k members", Toast.LENGTH_SHORT).show()
            }
          )
        }
      }

      // 4. Section 1: Happening This Week
      item {
        Spacer(modifier = Modifier.height(24.dp))
        HappeningThisWeekSection(
          meetups = filteredMeetups,
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
          onSeeAllClick = onSeeAllMeetups
        )
      }

      // 5. Section 2: Trending Club Discussions
      item {
        Spacer(modifier = Modifier.height(26.dp))
        TrendingDiscussionsSection(
          discussions = filteredDiscussions,
          onLikeClick = { postId -> viewModel.togglePostLike(postId) },
          onBookmarkClick = { postId ->
            viewModel.togglePostBookmark(postId)
            Toast.makeText(context, "Saved to your bookmarks", Toast.LENGTH_SHORT).show()
          },
          onPostClick = { post ->
            if (post.authorName == "Elena Rostova") {
              onOpenPotteryChat()
            } else {
              Toast.makeText(context, "Opening thread: ${post.title}", Toast.LENGTH_SHORT).show()
            }
          },
          onFilterClick = {
            Toast.makeText(context, "Sorting by hottest discussions", Toast.LENGTH_SHORT).show()
          }
        )
      }

      // 6. Section 3: Popular Clubs Near You
      item {
        Spacer(modifier = Modifier.height(26.dp))
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
          onExploreAllClick = onExploreAllClubs
        )
      }
    }
  }

  // Create Post Dialog
  if (showCreateDialog) {
    CreatePostOrMeetupDialog(
      onDismiss = { viewModel.setShowCreateDialog(false) },
      onSubmitPost = { club, title, content ->
        viewModel.createDiscussionPost(club, title, content)
        Toast.makeText(context, "Posted to $club!", Toast.LENGTH_SHORT).show()
      }
    )
  }
}
