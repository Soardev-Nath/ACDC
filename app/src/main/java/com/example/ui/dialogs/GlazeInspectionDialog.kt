package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.GlazeRecipe
import com.example.ui.theme.TerracottaPrimary
import com.example.ui.theme.WarmOnSurface
import com.example.ui.theme.WarmOnSurfaceVariant

@Composable
fun GlazeInspectionDialog(
  recipe: GlazeRecipe = GlazeRecipe(),
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isBookmarked by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 24.dp),
      shape = RoundedCornerShape(24.dp),
      color = Color(0xFFFFF8F5),
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Top bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Color(0xFFFEF3C7))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "KILN RUN #84 • UNLOADED TODAY",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              ),
              color = Color(0xFFB45309)
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = WarmOnSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large Image of Glaze Tile
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_glaze_tile),
            contentDescription = "Rutile Amber Test Tile",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = recipe.name,
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          ),
          color = WarmOnSurface
        )

        Text(
          text = recipe.firing,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold
          ),
          color = TerracottaPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = recipe.finish,
          style = MaterialTheme.typography.bodyMedium,
          color = WarmOnSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Formula section
        Text(
          text = "Base Recipe (100% Batch)",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = WarmOnSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0E5DF))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            recipe.baseFormula.forEach { (material, percent) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = material,
                  style = MaterialTheme.typography.bodyMedium,
                  color = WarmOnSurface
                )
                Text(
                  text = percent,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = TerracottaPrimary
                )
              }
            }

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .height(1.dp)
                .background(Color(0xFFF0E5DF))
            )

            Text(
              text = "Additions & Colorants:",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = WarmOnSurfaceVariant
            )

            recipe.colorantsAndAdditions.forEach { (material, percent) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = material,
                  style = MaterialTheme.typography.bodyMedium,
                  color = WarmOnSurface
                )
                Text(
                  text = percent,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFFD97706)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Notes from Elena:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = WarmOnSurfaceVariant
        )
        Text(
          text = recipe.notes,
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
          color = WarmOnSurface
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = { isBookmarked = !isBookmarked },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("bookmark_recipe_button"),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
        ) {
          Icon(
            imageVector = Icons.Default.Bookmark,
            contentDescription = null,
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isBookmarked) "Saved to Studio Notebook" else "Bookmark Recipe for Tomorrow",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }
      }
    }
  }
}
