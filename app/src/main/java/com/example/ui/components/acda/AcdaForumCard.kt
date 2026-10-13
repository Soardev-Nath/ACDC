package com.example.ui.components.acda

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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcdaForumPost
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant

@Composable
fun AcdaForumCard(
  post: AcdaForumPost,
  onLikeClick: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, Color(0xFFDBE8DB), RoundedCornerShape(18.dp))
      .clickable { onClick() }
      .testTag("forum_card_${post.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header: Author & Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = post.authorName,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.5.sp
            ),
            color = AcdaOnSurface
          )
          Text(
            text = "${post.authorRoleOrChapter} • ${post.timeAgo}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = AcdaOnSurfaceVariant
          )
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFE8F5E9))
            .padding(horizontal = 9.dp, vertical = 3.dp)
        ) {
          Text(
            text = post.tag,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp
            ),
            color = AcdaGreenPrimary
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = post.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          lineHeight = 21.sp
        ),
        color = AcdaOnSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = post.content,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = 13.sp,
          lineHeight = 19.sp
        ),
        color = AcdaOnSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Footer: Likes, Replies, Share
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onLikeClick() }
          ) {
            Icon(
              imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (post.isLiked) AcdaGreenPrimary else Color(0xFF7D9480),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.likesCount} agrees",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.5.sp,
                fontWeight = if (post.isLiked) FontWeight.Bold else FontWeight.Normal
              ),
              color = if (post.isLiked) AcdaGreenPrimary else AcdaOnSurfaceVariant
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClick() }
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Replies",
              tint = Color(0xFF7D9480),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.repliesCount} replies",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
              color = AcdaOnSurfaceVariant
            )
          }
        }

        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = "Share",
          tint = Color(0xFF7D9480),
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}
