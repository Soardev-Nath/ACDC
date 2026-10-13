package com.example.ui.components.explore

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.DiscussionPostItem
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun TrendingDiscussionsSection(
  discussions: List<DiscussionPostItem>,
  onLikeClick: (String) -> Unit,
  onBookmarkClick: (String) -> Unit,
  onPostClick: (DiscussionPostItem) -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Trending Club Discussions",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = WarmOnSurface
        )
        Text(
          text = "Live conversations from community boards",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
          color = WarmOnSurfaceVariant
        )
      }

      IconButton(
        onClick = onFilterClick,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.FilterList,
          contentDescription = "Filter discussions",
          tint = WarmOnSurfaceVariant,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // List of discussion cards
    Column(
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      discussions.forEach { post ->
        DiscussionPostCard(
          post = post,
          onLikeClick = { onLikeClick(post.id) },
          onBookmarkClick = { onBookmarkClick(post.id) },
          onPostClick = { onPostClick(post) }
        )
      }
    }
  }
}

@Composable
private fun DiscussionPostCard(
  post: DiscussionPostItem,
  onLikeClick: () -> Unit,
  onBookmarkClick: () -> Unit,
  onPostClick: () -> Unit
) {
  val context = LocalContext.current

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, Color(0xFFEADFD9), RoundedCornerShape(20.dp))
      .clickable { onPostClick() }
      .testTag("discussion_card_${post.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      // Top row: Club Pill + Time + Bookmark
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Color(0xFFFFDBD1).copy(alpha = 0.5f))
              .border(1.dp, TerracottaPrimary.copy(alpha = 0.3f), CircleShape)
              .padding(horizontal = 10.dp, vertical = 3.dp)
          ) {
            Text(
              text = post.clubName,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              ),
              color = TerracottaPrimary
            )
          }

          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "•", color = Color(0xFF8C716A), fontSize = 11.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = post.timeAgo,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = WarmOnSurfaceVariant
          )
        }

        IconButton(
          onClick = onBookmarkClick,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = "Bookmark",
            tint = if (post.isBookmarked) TerracottaPrimary else Color(0xFF8C716A),
            modifier = Modifier.size(19.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Author & Title block
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(post.authorAvatarUrl)
            .crossfade(true)
            .error(R.drawable.img_elena_avatar)
            .build(),
          contentDescription = post.authorName,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFEADFD9), CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = post.authorName,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
              ),
              color = WarmOnSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = post.authorHandle,
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
              color = WarmOnSurfaceVariant
            )
          }

          Text(
            text = post.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              lineHeight = 21.sp
            ),
            color = WarmOnSurface,
            modifier = Modifier.padding(top = 2.dp)
          )

          post.bodySnippet?.let { snippet ->
            Text(
              text = snippet,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                lineHeight = 19.sp
              ),
              color = WarmOnSurfaceVariant,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }

      // Optional Media Preview (e.g. Sourdough loaf photo)
      post.mediaUrl?.let { mediaUrl ->
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFEADFD9), RoundedCornerShape(14.dp))
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(mediaUrl)
              .crossfade(true)
              .error(R.drawable.img_glaze_tile)
              .build(),
            contentDescription = "Post media preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(170.dp)
          )

          post.mediaBadge?.let { badge ->
            Box(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.92f))
                .border(1.dp, Color(0xFFEADFD9), CircleShape)
                .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Science,
                  contentDescription = null,
                  tint = AmberTertiary,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = badge,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                  ),
                  color = WarmOnSurface
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Thread Engagement Footer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .border(0.5.dp, Color.Transparent, RoundedCornerShape(0.dp))
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Replies
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onPostClick() }
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = null,
              tint = Color(0xFF8C716A),
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.replyCount} replies",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
              color = WarmOnSurfaceVariant
            )
          }

          // Likes
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onLikeClick() }
          ) {
            Icon(
              imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (post.isLiked) TerracottaPrimary else Color(0xFF8C716A),
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.likeCount} likes",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                fontWeight = if (post.isLiked) FontWeight.Bold else FontWeight.Normal
              ),
              color = if (post.isLiked) TerracottaPrimary else WarmOnSurfaceVariant
            )
          }

          // Share
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = Color(0xFF8C716A),
            modifier = Modifier.size(17.dp)
          )
        }

        // Active thread badge
        post.statusBadge?.let { badge ->
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Color(0xFFE7F5EA))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = badge,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium
              ),
              color = Color(0xFF2E6B3E)
            )
          }
        }
      }
    }
  }
}
