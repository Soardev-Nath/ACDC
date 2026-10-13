package com.example.data.repository

import com.example.data.model.ChatChannel
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(
  private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

  fun getChannels(): Flow<List<ChatChannel>> = callbackFlow {
    val listener = firestore.collection("channels")
      .orderBy("lastMessageTime", Query.Direction.DESCENDING)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }
        val channels = snapshot?.documents?.mapNotNull { it.toObject(ChatChannel::class.java) } ?: emptyList()
        trySend(channels)
      }
    awaitClose { listener.remove() }
  }

  fun getMessages(channelId: String): Flow<List<ChatMessage>> = callbackFlow {
    val listener = firestore.collection("channels")
      .document(channelId)
      .collection("messages")
      .orderBy("timestamp", Query.Direction.ASCENDING)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }
        val messages = snapshot?.documents?.mapNotNull { it.toObject(ChatMessage::class.java) } ?: emptyList()
        trySend(messages)
      }
    awaitClose { listener.remove() }
  }

  suspend fun sendMessage(
    channelId: String,
    sender: UserProfile,
    text: String,
    type: String = "text"
  ) {
    val messageRef = firestore.collection("channels")
      .document(channelId)
      .collection("messages")
      .document()

    val now = Timestamp.now()
    val message = ChatMessage(
      id = messageRef.id,
      channelId = channelId,
      senderId = sender.uid,
      senderName = sender.name,
      senderRole = sender.role,
      text = text,
      type = type,
      timestamp = now,
      status = "read"
    )

    // Write message and update parent channel atomically or sequentially
    messageRef.set(message).await()

    firestore.collection("channels").document(channelId).update(
      mapOf(
        "lastMessage" to text,
        "lastMessageTime" to now,
        "lastMessageSender" to sender.name
      )
    ).await()
  }

  suspend fun createChannel(
    name: String,
    description: String,
    isGroup: Boolean = true,
    isAnnouncement: Boolean = false,
    creator: UserProfile
  ): String {
    val docRef = firestore.collection("channels").document()
    val now = Timestamp.now()
    val channel = ChatChannel(
      id = docRef.id,
      name = name,
      description = description,
      isGroup = isGroup,
      isAnnouncement = isAnnouncement,
      participantIds = listOf(creator.uid),
      lastMessage = "Channel created",
      lastMessageTime = now,
      lastMessageSender = creator.name,
      unreadCount = 0,
      avatarUrl = "",
      createdBy = creator.uid,
      createdAt = now
    )
    docRef.set(channel).await()
    return docRef.id
  }

  suspend fun deleteMessage(channelId: String, messageId: String) {
    firestore.collection("channels")
      .document(channelId)
      .collection("messages")
      .document(messageId)
      .delete()
      .await()
  }

  suspend fun seedDefaultChannelsIfEmpty(adminUser: UserProfile) {
    val snapshot = firestore.collection("channels").limit(1).get().await()
    if (snapshot.isEmpty) {
      val defaultChannels = listOf(
        Triple("National Executive Council", "Official deliberations of the ACDA Central Executive Committee.", true),
        Triple("ACDA General Community Forum", "All community members voice, updates, and open discussions.", true),
        Triple("Development & Projects Working Group", "Tracking boreholes, schools, and electrification initiatives.", true),
        Triple("Youth & Sports Wing", "Empowering the younger generation, tournaments, and scholarships.", true),
        Triple("Official Announcements & Bulletins", "Verified press releases and executive circulars.", false)
      )

      val now = Timestamp.now()
      for ((name, desc, isGroup) in defaultChannels) {
        val docRef = firestore.collection("channels").document()
        val isAnnounce = name.contains("Announcements", ignoreCase = true)
        val channel = ChatChannel(
          id = docRef.id,
          name = name,
          description = desc,
          isGroup = isGroup,
          isAnnouncement = isAnnounce,
          participantIds = listOf(adminUser.uid),
          lastMessage = "Welcome to the $name channel!",
          lastMessageTime = now,
          lastMessageSender = "ACDA Secretariat",
          unreadCount = 1,
          createdBy = adminUser.uid,
          createdAt = now
        )
        docRef.set(channel).await()

        // Welcome message
        val msgRef = docRef.collection("messages").document()
        val welcomeMsg = ChatMessage(
          id = msgRef.id,
          channelId = docRef.id,
          senderId = adminUser.uid,
          senderName = "ACDA Secretariat",
          senderRole = "admin",
          text = "Welcome to $name. This channel is synchronized live across all ACDA community members and administrators.",
          type = "announcement",
          timestamp = now,
          status = "read"
        )
        msgRef.set(welcomeMsg).await()
      }
    }
  }
}
