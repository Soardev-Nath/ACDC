package com.example.data.repository

import com.example.data.model.TownHallTopic
import com.example.data.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TownHallRepository(
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

  fun getTopics(): Flow<List<TownHallTopic>> = callbackFlow {
    val listener = firestore.collection("townhall_topics")
      .orderBy("createdAt", Query.Direction.DESCENDING)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }
        val topics = snapshot?.documents?.mapNotNull { it.toObject(TownHallTopic::class.java) } ?: emptyList()
        trySend(topics)
      }
    awaitClose { listener.remove() }
  }

  suspend fun createTopic(
    title: String,
    description: String,
    speaker: String,
    scheduledDate: String,
    creator: UserProfile
  ): String {
    val docRef = firestore.collection("townhall_topics").document()
    val now = Timestamp.now()
    val topic = TownHallTopic(
      id = docRef.id,
      title = title,
      description = description,
      speaker = speaker,
      scheduledDate = scheduledDate,
      status = "active",
      upvotesCount = 1,
      upvoterIds = listOf(creator.uid),
      createdBy = creator.uid,
      createdAt = now
    )
    docRef.set(topic).await()
    return docRef.id
  }

  suspend fun toggleUpvote(topicId: String, userId: String, currentUpvoters: List<String>) {
    val isUpvoted = currentUpvoters.contains(userId)
    val docRef = firestore.collection("townhall_topics").document(topicId)
    if (isUpvoted) {
      docRef.update(
        mapOf(
          "upvotesCount" to FieldValue.increment(-1),
          "upvoterIds" to FieldValue.arrayRemove(userId)
        )
      ).await()
    } else {
      docRef.update(
        mapOf(
          "upvotesCount" to FieldValue.increment(1),
          "upvoterIds" to FieldValue.arrayUnion(userId)
        )
      ).await()
    }
  }

  suspend fun updateTopicStatus(topicId: String, newStatus: String) {
    firestore.collection("townhall_topics").document(topicId)
      .update("status", newStatus)
      .await()
  }

  suspend fun deleteTopic(topicId: String) {
    firestore.collection("townhall_topics").document(topicId).delete().await()
  }

  suspend fun seedDefaultTopicsIfEmpty(adminUser: UserProfile) {
    val snapshot = firestore.collection("townhall_topics").limit(1).get().await()
    if (snapshot.isEmpty) {
      val now = Timestamp.now()
      val defaultTopics = listOf(
        TownHallTopic(
          title = "Q4 Community Development Review & 2027 Strategic Roadmap",
          description = "Plenary assembly to deliberate on rural feeder road construction, healthcare subsidies, and the annual agricultural grant allocation.",
          speaker = "ACDA National President & Exco",
          scheduledDate = "Saturday, 25th Oct • 10:00 AM",
          status = "active",
          upvotesCount = 48,
          upvoterIds = listOf(adminUser.uid),
          createdBy = adminUser.uid,
          createdAt = now
        ),
        TownHallTopic(
          title = "Motion on Community Security & Youth Vigilante Equipment",
          description = "Deliberation on securing perimeter boundaries, acquiring high-frequency comms radios, and community policing partnerships.",
          speaker = "Chief Security Officer",
          scheduledDate = "Saturday, 1st Nov • 2:00 PM",
          status = "active",
          upvotesCount = 37,
          upvoterIds = emptyList(),
          createdBy = adminUser.uid,
          createdAt = now
        )
      )

      for (top in defaultTopics) {
        val doc = firestore.collection("townhall_topics").document()
        doc.set(top.copy(id = doc.id)).await()
      }
    }
  }
}
