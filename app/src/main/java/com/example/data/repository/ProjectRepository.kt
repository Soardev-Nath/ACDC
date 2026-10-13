package com.example.data.repository

import com.example.data.model.AcdaProject
import com.example.data.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProjectRepository(
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

  fun getProjects(): Flow<List<AcdaProject>> = callbackFlow {
    val listener = firestore.collection("projects")
      .orderBy("createdAt", Query.Direction.DESCENDING)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }
        val projects = snapshot?.documents?.mapNotNull { it.toObject(AcdaProject::class.java) } ?: emptyList()
        trySend(projects)
      }
    awaitClose { listener.remove() }
  }

  suspend fun createProject(
    title: String,
    description: String,
    category: String,
    targetBudget: Double,
    raisedBudget: Double,
    location: String,
    contractor: String,
    creator: UserProfile
  ): String {
    val docRef = firestore.collection("projects").document()
    val now = Timestamp.now()
    val project = AcdaProject(
      id = docRef.id,
      title = title,
      description = description,
      category = category,
      status = "In Progress",
      targetBudget = targetBudget,
      raisedBudget = raisedBudget,
      location = location,
      contractor = contractor,
      createdBy = creator.uid,
      createdAt = now,
      updatedAt = now
    )
    docRef.set(project).await()
    return docRef.id
  }

  suspend fun updateProject(
    projectId: String,
    title: String,
    description: String,
    status: String,
    raisedBudget: Double,
    targetBudget: Double,
    location: String,
    contractor: String
  ) {
    firestore.collection("projects").document(projectId).update(
      mapOf(
        "title" to title,
        "description" to description,
        "status" to status,
        "raisedBudget" to raisedBudget,
        "targetBudget" to targetBudget,
        "location" to location,
        "contractor" to contractor,
        "updatedAt" to Timestamp.now()
      )
    ).await()
  }

  suspend fun deleteProject(projectId: String) {
    firestore.collection("projects").document(projectId).delete().await()
  }

  suspend fun seedDefaultProjectsIfEmpty(adminUser: UserProfile) {
    val snapshot = firestore.collection("projects").limit(1).get().await()
    if (snapshot.isEmpty) {
      val now = Timestamp.now()
      val defaultProjects = listOf(
        AcdaProject(
          title = "Udege Solar Mini-Grid & Water Scheme",
          description = "Installation of high-yield solar borehole pumps providing 50,000 liters daily clean drinking water to over 3,000 households.",
          category = "Water",
          status = "In Progress",
          targetBudget = 18500000.0,
          raisedBudget = 14200000.0,
          location = "Udege District",
          contractor = "AquaSolar Eng. Ltd",
          createdBy = adminUser.uid,
          createdAt = now,
          updatedAt = now
        ),
        AcdaProject(
          title = "National Civic Center Renovation",
          description = "Modernization of the Central Community Assembly Hall with solar backup, executive boardroom, and 500-seat plenary theatre.",
          category = "Infrastructure",
          status = "In Progress",
          targetBudget = 42000000.0,
          raisedBudget = 29500000.0,
          location = "Central Township",
          contractor = "Heritage Builders Consortium",
          createdBy = adminUser.uid,
          createdAt = now,
          updatedAt = now
        ),
        AcdaProject(
          title = "Youth Digital Innovation & STEM Lab",
          description = "Establishing a 50-workstation technology training center with high-speed satellite internet to empower ACDA students in software & AI.",
          category = "Education",
          status = "Planning",
          targetBudget = 12000000.0,
          raisedBudget = 7800000.0,
          location = "Youth Resource Wing",
          contractor = "DigiTech Africa Hub",
          createdBy = adminUser.uid,
          createdAt = now,
          updatedAt = now
        )
      )

      for (proj in defaultProjects) {
        val doc = firestore.collection("projects").document()
        doc.set(proj.copy(id = doc.id)).await()
      }
    }
  }
}
