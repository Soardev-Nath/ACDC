package com.example.ui.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOutline
import com.example.ui.theme.WarmOutlineVariant

@Composable
fun ExploreSearchBar(
  query: String,
  onQueryChanged: (String) -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(46.dp)
        .clip(CircleShape)
        .background(Color.White)
        .border(1.dp, Color(0xFFEADFD9), CircleShape)
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        tint = WarmOutline,
        modifier = Modifier.size(20.dp)
      )

      Spacer(modifier = Modifier.width(10.dp))

      Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.CenterStart
      ) {
        if (query.isEmpty()) {
          Text(
            text = "Search clubs, meetups, crafts...",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = WarmOutline
          )
        }

        BasicTextField(
          value = query,
          onValueChange = onQueryChanged,
          textStyle = TextStyle(
            color = WarmOnSurface,
            fontSize = 14.sp
          ),
          cursorBrush = SolidColor(TerracottaPrimary),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("explore_search_input")
        )
      }

      if (query.isNotEmpty()) {
        IconButton(
          onClick = { onQueryChanged("") },
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Clear",
            tint = WarmOutline,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      IconButton(
        onClick = onFilterClick,
        modifier = Modifier
          .size(32.dp)
          .testTag("explore_tune_filter_button")
      ) {
        Icon(
          imageVector = Icons.Default.Tune,
          contentDescription = "Filter Options",
          tint = WarmOutline,
          modifier = Modifier.size(19.dp)
        )
      }
    }
  }
}
