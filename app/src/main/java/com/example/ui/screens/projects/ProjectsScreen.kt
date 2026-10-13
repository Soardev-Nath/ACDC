package com.example.ui.screens.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.AcdaProject
import com.example.data.model.UserProfile
import com.example.ui.theme.AcdaRed
import com.example.ui.theme.WhatsAppPrimary
import com.example.viewmodel.AcdaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
  viewModel: AcdaViewModel,
  userProfile: UserProfile?,
  modifier: Modifier = Modifier
) {
  val projects by viewModel.filteredProjects.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()

  var showCreateDialog by remember { mutableStateOf(false) }
  var projectToEdit by remember { mutableStateOf<AcdaProject?>(null) }
  var projectToDelete by remember { mutableStateOf<AcdaProject?>(null) }

  val categories = listOf("All", "Infrastructure", "Water", "Education", "Healthcare", "Agriculture")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Community Projects",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        },
        actions = {
          if (userProfile?.isAdmin == true) {
            Button(
              onClick = { showCreateDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = Color.White),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.padding(end = 8.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = WhatsAppPrimary)
              Text("New Project", color = WhatsAppPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WhatsAppPrimary)
      )
    },
    floatingActionButton = {
      if (userProfile?.isAdmin == true) {
        FloatingActionButton(
          onClick = { showCreateDialog = true },
          containerColor = WhatsAppPrimary,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_project_fab")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Project")
        }
      }
    },
    modifier = modifier.testTag("projects_screen")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Category filter chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { category ->
          val isSelected = if (category == "All") selectedCategory == null else selectedCategory == category
          FilterChip(
            selected = isSelected,
            onClick = {
              viewModel.setCategory(if (category == "All") null else category)
            },
            label = { Text(category, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = WhatsAppPrimary.copy(alpha = 0.15f),
              selectedLabelColor = WhatsAppPrimary
            )
          )
        }
      }

      if (projects.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text("No projects under this category.", color = Color(0xFF64748B))
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(projects, key = { it.id }) { project ->
            ProjectCard(
              project = project,
              isAdmin = userProfile?.isAdmin == true,
              onEdit = { projectToEdit = project },
              onDelete = { projectToDelete = project }
            )
          }
        }
      }
    }

    if (showCreateDialog && userProfile != null) {
      ProjectFormDialog(
        initialProject = null,
        onDismiss = { showCreateDialog = false },
        onSave = { title, desc, cat, target, raised, loc, cont ->
          showCreateDialog = false
          viewModel.createProject(title, desc, cat, target, raised, loc, cont, userProfile)
        }
      )
    }

    if (projectToEdit != null) {
      ProjectFormDialog(
        initialProject = projectToEdit,
        onDismiss = { projectToEdit = null },
        onSave = { title, desc, cat, target, raised, loc, cont ->
          projectToEdit?.id?.let {
            viewModel.updateProject(it, title, desc, "In Progress", raised, target, loc, cont)
          }
          projectToEdit = null
        }
      )
    }

    if (projectToDelete != null) {
      AlertDialog(
        onDismissRequest = { projectToDelete = null },
        title = { Text("Delete Project") },
        text = { Text("Are you sure you want to permanently delete '${projectToDelete?.title}'?") },
        confirmButton = {
          TextButton(
            onClick = {
              projectToDelete?.id?.let { viewModel.deleteProject(it) }
              projectToDelete = null
            }
          ) {
            Text("Delete", color = AcdaRed, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { projectToDelete = null }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@Composable
private fun ProjectCard(
  project: AcdaProject,
  isAdmin: Boolean,
  onEdit: () -> Unit,
  onDelete: () -> Unit
) {
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
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = project.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Text(
            text = "${project.category} • Location: ${project.location}",
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 2.dp)
          )
        }
        if (isAdmin) {
          Row {
            IconButton(onClick = onEdit) {
              Icon(Icons.Default.Edit, contentDescription = "Edit", tint = WhatsAppPrimary)
            }
            IconButton(onClick = onDelete) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AcdaRed)
            }
          }
        }
      }

      Text(
        text = project.description,
        fontSize = 13.sp,
        color = Color(0xFF334155),
        modifier = Modifier.padding(vertical = 8.dp)
      )

      LinearProgressIndicator(
        progress = { project.progressPercent },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp),
        color = WhatsAppPrimary,
        trackColor = Color(0xFFE2E8F0)
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("Raised", fontSize = 11.sp, color = Color(0xFF64748B))
          Text("₦${String.format("%,.0f", project.raisedBudget)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WhatsAppPrimary)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Progress", fontSize = 11.sp, color = Color(0xFF64748B))
          Text("${(project.progressPercent * 100).toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("Target Budget", fontSize = 11.sp, color = Color(0xFF64748B))
          Text("₦${String.format("%,.0f", project.targetBudget)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        }
      }

      if (project.contractor.isNotEmpty()) {
        Surface(
          color = Color(0xFFF8FAFC),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        ) {
          Text(
            text = "Contractor: ${project.contractor}",
            fontSize = 11.sp,
            color = Color(0xFF475569),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ProjectFormDialog(
  initialProject: AcdaProject?,
  onDismiss: () -> Unit,
  onSave: (String, String, String, Double, Double, String, String) -> Unit
) {
  var title by remember { mutableStateOf(initialProject?.title ?: "") }
  var desc by remember { mutableStateOf(initialProject?.description ?: "") }
  var category by remember { mutableStateOf(initialProject?.category ?: "Infrastructure") }
  var targetBudget by remember { mutableStateOf(initialProject?.targetBudget?.toInt()?.toString() ?: "10000000") }
  var raisedBudget by remember { mutableStateOf(initialProject?.raisedBudget?.toInt()?.toString() ?: "2000000") }
  var location by remember { mutableStateOf(initialProject?.location ?: "Central Area") }
  var contractor by remember { mutableStateOf(initialProject?.contractor ?: "") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(if (initialProject == null) "Add Community Project" else "Edit Project") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Project Title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, maxLines = 3, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = targetBudget, onValueChange = { targetBudget = it }, label = { Text("Target ₦") }, singleLine = true, modifier = Modifier.weight(1f))
          OutlinedTextField(value = raisedBudget, onValueChange = { raisedBudget = it }, label = { Text("Raised ₦") }, singleLine = true, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contractor, onValueChange = { contractor = it }, label = { Text("Contractor / Agency") }, singleLine = true, modifier = Modifier.fillMaxWidth())
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val t = targetBudget.toDoubleOrNull() ?: 0.0
          val r = raisedBudget.toDoubleOrNull() ?: 0.0
          if (title.isNotBlank()) onSave(title, desc, category, t, r, location, contractor)
        },
        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppPrimary)
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
