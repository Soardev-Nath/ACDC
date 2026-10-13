package com.example.data.model

import com.google.firebase.Timestamp

data class UserProfile(
  val uid: String = "",
  val name: String = "",
  val email: String = "",
  val role: String = "member", // "admin" or "member"
  val avatarUrl: String = "",
  val about: String = "ACDA Community Member",
  val phone: String = "",
  val membershipId: String = "",
  val branch: String = "National Secretariat",
  val createdAt: Timestamp? = null,
  val updatedAt: Timestamp? = null
) {
  val isAdmin: Boolean
    get() = role.equals("admin", ignoreCase = true) || email.equals("nathanielrop84@gmail.com", ignoreCase = true)
}

data class ChatChannel(
  val id: String = "",
  val name: String = "",
  val description: String = "",
  val isGroup: Boolean = true,
  val isAnnouncement: Boolean = false,
  val participantIds: List<String> = emptyList(),
  val lastMessage: String = "",
  val lastMessageTime: Timestamp? = null,
  val lastMessageSender: String = "",
  val unreadCount: Int = 0,
  val avatarUrl: String = "",
  val createdBy: String = "",
  val createdAt: Timestamp? = null
)

data class ChatMessage(
  val id: String = "",
  val channelId: String = "",
  val senderId: String = "",
  val senderName: String = "",
  val senderRole: String = "member",
  val text: String = "",
  val imageUrl: String = "",
  val type: String = "text", // "text", "image", "system", "announcement"
  val timestamp: Timestamp? = null,
  val status: String = "read", // "sent", "delivered", "read"
  val replyTo: String = ""
) {
  val isFromAdmin: Boolean
    get() = senderRole.equals("admin", ignoreCase = true)
}

data class AcdaProject(
  val id: String = "",
  val title: String = "",
  val description: String = "",
  val category: String = "Infrastructure", // "Infrastructure", "Water", "Education", "Healthcare", "Agriculture"
  val status: String = "In Progress", // "Planning", "In Progress", "Completed"
  val targetBudget: Double = 0.0,
  val raisedBudget: Double = 0.0,
  val location: String = "",
  val contractor: String = "",
  val createdBy: String = "",
  val createdAt: Timestamp? = null,
  val updatedAt: Timestamp? = null
) {
  val progressPercent: Float
    get() = if (targetBudget > 0) ((raisedBudget / targetBudget).coerceIn(0.0, 1.0)).toFloat() else 0f
}

data class TownHallTopic(
  val id: String = "",
  val title: String = "",
  val description: String = "",
  val speaker: String = "National President",
  val scheduledDate: String = "Every Last Saturday",
  val status: String = "active", // "upcoming", "active", "concluded"
  val upvotesCount: Int = 0,
  val upvoterIds: List<String> = emptyList(),
  val createdBy: String = "",
  val createdAt: Timestamp? = null
)

data class SystemStats(
  val totalMembers: Int = 0,
  val totalChannels: Int = 0,
  val totalProjects: Int = 0,
  val totalTopics: Int = 0,
  val totalProjectFunding: Double = 0.0
)
