package com.example.ui.screens.townhall

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TownHallTopic
import com.example.data.model.UserProfile
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppPrimary
import com.example.viewmodel.AcdaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TownHallScreen(
  viewModel: AcdaViewModel,
  userProfile: UserProfile?,
  modifier: Modifier = Modifier
) {
  val topics by viewModel.topics.collectAsState()
  var showNewTopicDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "ACDA Town Hall & Deliberations",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showNewTopicDialog = true },
        containerColor = WhatsAppPrimary,
        contentColor = Color.White,
        shape = CircleShape,
        modifier = Modifier.testTag("new_topic_fab")
      ) {
        Icon(Icons.Default.Add, contentDescription = "New Motion")
      }
    },
    modifier = modifier.testTag("town_hall_screen")
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = WhatsAppPrimary.copy(alpha = 0.08f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Forum, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.padding(start = 12.dp))
            Column {
              Text(
                text = "Democratic Assembly",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppPrimary
              )
              Text(
                text = "Participate in national community debate, upvote motions, and influence executive resolutions.",
                fontSize = 12.sp,
                color = Color(0xFF475569)
              )
            }
          }
        }
      }

      items(topics, key = { it.id }) { topic ->
        TopicCard(
          topic = topic,
          currentUserId = userProfile?.uid ?: "",
          isAdmin = userProfile?.isAdmin == true,
          onUpvote = {
            if (userProfile != null) {
              viewModel.toggleUpvote(topic.id, userProfile.uid, topic.upvoterIds)
            }
          },
          onConclude = { viewModel.updateTopicStatus(topic.id, "concluded") },
          onDelete = { viewModel.deleteTopic(topic.id) }
        )
      }
    }

    if (showNewTopicDialog && userProfile != null) {
      NewTopicDialog(
        onDismiss = { showNewTopicDialog = false },
        onCreate = { title, desc, speaker, date ->
          showNewTopicDialog = false
          viewModel.createTopic(title, desc, speaker, date, userProfile)
        }
      )
    }
  }
}

@Composable
private fun TopicCard(
  topic: TownHallTopic,
  currentUserId: String,
  isAdmin: Boolean,
  onUpvote: () -> Unit,
  onConclude: () -> Unit,
  onDelete: () -> Unit
) {
  val hasUpvoted = topic.upvoterIds.contains(currentUserId)

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Text(
          text = topic.title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A),
          modifier = Modifier.weight(1f)
        )
        Surface(
          color = if (topic.status == "active") WhatsAppPrimary.copy(alpha = 0.12f) else Color(0xFF64748B).copy(alpha = 0.12f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = topic.status.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (topic.status == "active") WhatsAppPrimary else Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Text(
        text = topic.description,
        fontSize = 13.sp,
        color = Color(0xFF334155),
        modifier = Modifier.padding(vertical = 8.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Lead Speaker: ${topic.speaker}", fontSize = 11.sp, color = WhatsAppPrimary, fontWeight = FontWeight.SemiBold)
          Text("Session: ${topic.scheduledDate}", fontSize = 11.sp, color = Color(0xFF64748B))
        }

        OutlinedButton(
          onClick = onUpvote,
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (hasUpvoted) WhatsAppPrimary else Color(0xFF64748B)
          )
        ) {
          Icon(
            imageVector = Icons.Default.ThumbUp,
            contentDescription = "Upvote",
            tint = if (hasUpvoted) WhatsAppPrimary else Color(0xFF64748B),
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.size(6.dp))
          Text("${topic.upvotesCount} Upvotes", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      if (isAdmin) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
          horizontalArrangement = Arrangement.End
        ) {
          if (topic.status == "active") {
            TextButton(onClick = onConclude) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = WhatsAppPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Conclude Motion", color = WhatsAppPrimary, fontSize = 12.sp)
            }
          }
          TextButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = AcdaRed, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(4.dp))
            Text("Delete", color = AcdaRed, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
private fun NewTopicDialog(
  onDismiss: () -> Unit,
  onCreate: (String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  var speaker by remember { mutableStateOf("ACDA Executive Committee") }
  var date by remember { mutableStateOf("Next Assembly • 10:00 AM") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Submit Town Hall Motion") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Motion Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Motion Substance") }, maxLines = 3, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = speaker, onValueChange = { speaker = it }, label = { Text("Lead Speaker") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Scheduled Date & Time") }, singleLine = true, modifier = Modifier.fillMaxWidth())
      }
    },
    confirmButton = {
      Button(
        onClick = { if (title.isNotBlank()) onCreate(title, desc, speaker, date) },
        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary)
      ) {
        Text("Submit")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
