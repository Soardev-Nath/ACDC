package com.example.model

enum class MessageStatus {
  SENDING,
  SENT,       // Single check
  DELIVERED,  // Double grey checks
  READ        // Double blue checks
}

enum class WhatsAppMessageType {
  TEXT,
  IMAGE,
  AUDIO,
  DOCUMENT,
  LOCATION
}

data class WhatsAppMessage(
  val id: String,
  val senderName: String,
  val isOutbound: Boolean,
  val type: WhatsAppMessageType,
  val text: String = "",
  val timestamp: String,
  val status: MessageStatus = MessageStatus.READ,
  val mediaResId: Int? = null,
  val mediaUrl: String? = null,
  val mediaCaption: String? = null,
  val audioDurationSec: Int = 24,
  val audioProgressSec: Int = 0,
  val isAudioPlaying: Boolean = false,
  val audioSpeedMultiplier: Float = 1.0f,
  val documentName: String? = null,
  val documentSize: String? = null,
  val locationName: String? = null,
  val reactionEmoji: String? = null
)

data class WhatsAppConversation(
  val id: String,
  val name: String,
  val isGroup: Boolean = false,
  val isVerified: Boolean = false,
  val groupMembersCount: Int? = null,
  val avatarResId: Int? = null,
  val avatarUrl: String? = null,
  val lastMessageText: String,
  val lastMessageTime: String,
  val unreadCount: Int = 0,
  val isPinned: Boolean = false,
  val isMuted: Boolean = false,
  val isOnline: Boolean = false,
  val statusSubtitle: String = "online",
  val phoneOrRole: String = "ACDA Member",
  val messages: List<WhatsAppMessage> = emptyList()
)

data class WhatsAppStatusStory(
  val id: String,
  val authorName: String,
  val authorAvatarResId: Int,
  val timeAgo: String,
  val caption: String,
  val mediaResId: Int,
  val isViewed: Boolean = false
)
