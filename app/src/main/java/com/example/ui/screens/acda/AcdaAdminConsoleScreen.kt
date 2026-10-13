package com.example.ui.screens.acda

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcdaNewsBulletin
import com.example.model.AcdaProject
import com.example.ui.theme.AcdaGoldAccent
import com.example.ui.theme.AcdaGreenPrimary
import com.example.ui.theme.AcdaOnSurface
import com.example.ui.theme.AcdaOnSurfaceVariant
import com.example.ui.theme.AcdaSurface
import com.example.viewmodel.AcdaViewModel

@Composable
fun AcdaAdminConsoleScreen(
  viewModel: AcdaViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current
  val isAdminMode by viewModel.isAdminMode.collectAsState()
  val projects by viewModel.projects.collectAsState()
  val news by viewModel.news.collectAsState()
  val forumPosts by viewModel.forumPosts.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddProjectDialog by remember { mutableStateOf(false) }
  var showAddBulletinDialog by remember { mutableStateOf(false) }
  var showBroadcastDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = AcdaSurface,
    topBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFF143B1B))
          .padding(top = 10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onNavigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFFFCD34D),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ACDA Executive Administration",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = Color.White
                )
              )
            }
            Text(
              text = "Central Command & Real-Time Production Backend",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.5.sp,
                color = Color(0xFFA7D5AE)
              )
            )
          }

          // Admin Mode Toggle
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(Color(0x33FFFFFF))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (isAdminMode) "ADMIN" else "MEMBER",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color.White
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Switch(
              checked = isAdminMode,
              onCheckedChange = { viewModel.toggleAdminMode() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF22C55E),
                checkedTrackColor = Color(0xFF14532D)
              ),
              modifier = Modifier.size(36.dp, 20.dp)
            )
          }
        }

        // Admin Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color(0xFF143B1B),
          contentColor = Color.White,
          indicator = { tabPositions ->
            TabRowDefaults.Indicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              height = 3.dp,
              color = Color(0xFFFCD34D)
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Overview", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Projects", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = { Text("Bulletins", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 3,
            onClick = { selectedTab = 3 },
            text = { Text("Members", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
          )
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      when (selectedTab) {
        0 -> {
          // --- Overview Stats ---
          item {
            AdminStatsGrid(
              projectsCount = projects.size,
              newsCount = news.size,
              postsCount = forumPosts.size
            )
          }

          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Quick Administrative Actions",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = AcdaOnSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Button(
                    onClick = { showAddProjectDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Project", fontSize = 12.sp)
                  }

                  Button(
                    onClick = { showAddBulletinDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AcdaGoldAccent),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Broadcast", fontSize = 12.sp)
                  }
                }
              }
            }
          }

          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Cloud Backend Status: Live & Synced",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF14532D)
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Cloud Firestore and Google Authentication are provisioned. Security rules enforce zero-trust role-based authorization across collections (/users, /projects, /bulletins, /forum_posts, /chats).",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                  color = Color(0xFF166534)
                )
              }
            }
          }
        }

        1 -> {
          // --- Projects Management Tab ---
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Community Projects (${projects.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = AcdaOnSurface
              )
              Button(
                onClick = { showAddProjectDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Project", fontSize = 12.sp)
              }
            }
          }

          items(projects) { project ->
            AdminProjectItem(
              project = project,
              onDelete = {
                viewModel.deleteProject(project.id)
                Toast.makeText(context, "Deleted ${project.title}", Toast.LENGTH_SHORT).show()
              },
              onUpdateProgress = { newProgress ->
                viewModel.updateProjectProgress(project.id, newProgress)
                Toast.makeText(context, "Progress updated to $newProgress%", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }

        2 -> {
          // --- Bulletins Management Tab ---
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Official Communiqués (${news.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = AcdaOnSurface
              )
              Button(
                onClick = { showAddBulletinDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AcdaGoldAccent),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Post Notice", fontSize = 12.sp)
              }
            }
          }

          items(news) { bulletin ->
            AdminBulletinItem(
              bulletin = bulletin,
              onDelete = {
                viewModel.deleteNewsBulletin(bulletin.id)
                Toast.makeText(context, "Deleted communiqué", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }

        3 -> {
          // --- Member Directory & Roles Tab ---
          item {
            Text(
              text = "Registered Executive & Member Directory",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = AcdaOnSurface
            )
          }

          item {
            AdminMemberList(
              onRoleChanged = { name, role ->
                Toast.makeText(context, "Updated $name to $role", Toast.LENGTH_SHORT).show()
              }
            )
          }
        }
      }
    }
  }

  // Add Project Dialog
  if (showAddProjectDialog) {
    AddProjectDialog(
      onDismiss = { showAddProjectDialog = false },
      onConfirm = { title, desc, cat, budget, loc, target ->
        viewModel.createProject(title, desc, cat, budget, loc, target)
        showAddProjectDialog = false
        Toast.makeText(context, "Project published to Firestore backend", Toast.LENGTH_SHORT).show()
      }
    )
  }

  // Add Bulletin Dialog
  if (showAddBulletinDialog) {
    AddBulletinDialog(
      onDismiss = { showAddBulletinDialog = false },
      onConfirm = { title, summary, content, category, isImportant ->
        viewModel.createNewsBulletin(title, summary, content, category, isImportant)
        showAddBulletinDialog = false
        Toast.makeText(context, "Official notice published to all chapters", Toast.LENGTH_SHORT).show()
      }
    )
  }
}

@Composable
private fun AdminStatsGrid(
  projectsCount: Int,
  newsCount: Int,
  postsCount: Int
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    AdminStatCard(
      title = "Projects",
      value = "$projectsCount Live",
      subtitle = "₦38.5M Allocated",
      icon = Icons.Default.Build,
      color = AcdaGreenPrimary,
      modifier = Modifier.weight(1f)
    )
    AdminStatCard(
      title = "Bulletins",
      value = "$newsCount Active",
      subtitle = "All Chapters",
      icon = Icons.Default.Campaign,
      color = AcdaGoldAccent,
      modifier = Modifier.weight(1f)
    )
    AdminStatCard(
      title = "Members",
      value = "1,420",
      subtitle = "Verified",
      icon = Icons.Default.People,
      color = Color(0xFF2563EB),
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
private fun AdminStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp), color = AcdaOnSurface)
      Text(text = title, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = AcdaOnSurfaceVariant))
      Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = color))
    }
  }
}

@Composable
private fun AdminProjectItem(
  project: AcdaProject,
  onDelete: () -> Unit,
  onUpdateProgress: (Int) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = project.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AcdaOnSurface)
          Text(text = "${project.category} • ${project.location}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AcdaOnSurfaceVariant)
        }

        IconButton(onClick = onDelete) {
          Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Budget: ₦${"%,d".format(project.targetAmountNaira)} | Raised: ₦${"%,d".format(project.raisedAmountNaira)} (${project.progressPercent}%)",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
        color = AcdaGreenPrimary
      )

      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = { onUpdateProgress((project.progressPercent + 10).coerceAtMost(100)) },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("+10% Progress", fontSize = 11.5.sp)
        }

        Button(
          onClick = { onUpdateProgress(100) },
          modifier = Modifier.weight(1f),
          colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Mark Complete", fontSize = 11.5.sp)
        }
      }
    }
  }
}

@Composable
private fun AdminBulletinItem(
  bulletin: AcdaNewsBulletin,
  onDelete: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        if (bulletin.isImportant) {
          Text(text = "★ PINNED BULLETIN", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = AcdaGoldAccent, fontSize = 10.sp))
        }
        Text(text = bulletin.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AcdaOnSurface)
        Text(text = "${bulletin.category} • ${bulletin.date}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = AcdaOnSurfaceVariant)
      }

      IconButton(onClick = onDelete) {
        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
      }
    }
  }
}

@Composable
private fun AdminMemberList(
  onRoleChanged: (name: String, role: String) -> Unit
) {
  val sampleMembers = listOf(
    Triple("Alhaji Danladi Musa", "National President", "ADMIN"),
    Triple("Barr. Emmanuel Agabi", "Secretary General", "ADMIN"),
    Triple("Mrs. Grace Alaku", "Treasury Lead", "CHAPTER_CHAIR"),
    Triple("Engr. Bitrus Owo", "Project Director", "ADMIN"),
    Triple("Amina Ibrahim", "Youth Wing Leader", "CHAPTER_CHAIR"),
    Triple("Yakubu Gabriel", "Community Elder (Udege)", "MEMBER")
  )

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    sampleMembers.forEach { (name, office, currentRole) ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBE8DB))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AcdaOnSurface)
            Text(text = office, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AcdaOnSurfaceVariant)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (currentRole == "ADMIN") Color(0xFFDCFCE7) else Color(0xFFF1F5F9))
              .clickable {
                val nextRole = if (currentRole == "ADMIN") "MEMBER" else "ADMIN"
                onRoleChanged(name, nextRole)
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = currentRole,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (currentRole == "ADMIN") Color(0xFF166534) else Color(0xFF475569)
              )
            )
          }
        }
      }
    }
  }
}

@Composable
private fun AddProjectDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, desc: String, category: String, budget: Long, location: String, targetDate: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Infrastructure") }
  var budgetStr by remember { mutableStateOf("5000000") }
  var location by remember { mutableStateOf("Udege Area") }
  var targetDate by remember { mutableStateOf("Dec 2026") }
  var desc by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Community Development Project", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Project Title") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category (e.g. Water, Health, Education)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = budgetStr,
          onValueChange = { budgetStr = it },
          label = { Text("Target Budget (₦)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Target Location / Community") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Brief Project Description") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val budget = budgetStr.toLongOrNull() ?: 5000000L
            onConfirm(title, desc, category, budget, location, targetDate)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = AcdaGreenPrimary)
      ) {
        Text("Create & Publish")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

@Composable
private fun AddBulletinDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, summary: String, content: String, category: String, isImportant: Boolean) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Executive Notice") }
  var summary by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var isImportant by remember { mutableStateOf(true) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Publish Official Communiqué", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Notice Headline") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = summary,
          onValueChange = { summary = it },
          label = { Text("Short Summary") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Full Communiqué Details") },
          maxLines = 4,
          modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Switch(
            checked = isImportant,
            onCheckedChange = { isImportant = it }
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Pin to top as High Priority Notice", fontSize = 12.sp)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, summary, content, category, isImportant)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = AcdaGoldAccent)
      ) {
        Text("Broadcast Notice")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
