package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
  @DocumentId val id: String = "",
  val userId: String = "",
  val fullName: String = "",
  val email: String = "",
  val role: String = "MEMBER", // ADMIN, MEMBER, CHAPTER_CHAIR
  val chapter: String = "Abuja Chapter",
  val phone: String = "",
  val membershipId: String = "ACDA-2026-0842",
  val isVerified: Boolean = true,
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirebaseProject(
  @DocumentId val id: String = "",
  val title: String = "",
  val description: String = "",
  val category: String = "INFRASTRUCTURE", // INFRASTRUCTURE, WATER, EDUCATION, HEALTH, SKILLS
  val budget: Long = 0L,
  val raised: Long = 0L,
  val percentage: Long = 0L,
  val status: String = "IN_PROGRESS", // PLANNING, IN_PROGRESS, COMPLETED
  val chapter: String = "Udege",
  val location: String = "Udege Development Area",
  val targetDate: String = "Dec 2026",
  val authorId: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirebaseBulletin(
  @DocumentId val id: String = "",
  val title: String = "",
  val summary: String = "",
  val content: String = "",
  val author: String = "National Secretariat",
  val authorId: String = "",
  val chapter: String = "National",
  val isPinned: Boolean = false,
  val date: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirebaseForumPost(
  @DocumentId val id: String = "",
  val title: String = "",
  val author: String = "",
  val authorId: String = "",
  val authorRole: String = "Member",
  val chapter: String = "National",
  val category: String = "General",
  val content: String = "",
  val upvotes: Long = 0L,
  val commentCount: Long = 0L,
  val isOfficialResolution: Boolean = false,
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirebaseChatMessage(
  @DocumentId val id: String = "",
  val chatId: String = "",
  val senderId: String = "",
  val senderName: String = "",
  val text: String = "",
  val type: String = "TEXT", // TEXT, AUDIO, IMAGE, DOCUMENT, LOCATION
  val mediaCaption: String = "",
  val timestamp: String = "",
  val isOutbound: Boolean = false,
  @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirebaseDonation(
  @DocumentId val id: String = "",
  val userId: String = "",
  val userName: String = "",
  val projectId: String = "",
  val projectTitle: String = "",
  val amount: Long = 0L,
  val paymentRef: String = "",
  val timestamp: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null
)
