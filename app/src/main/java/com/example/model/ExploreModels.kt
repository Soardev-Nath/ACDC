package com.example.model

enum class ExploreFilterCategory(val label: String, val iconName: String) {
  ALL("All", "auto_awesome"),
  CERAMICS("Ceramics & Craft", "palette"),
  TRAIL("Trail & Run", "hiking"),
  PHOTO("Film & Photo", "photo_camera"),
  BOOKS("Book Swap", "menu_book")
}

data class MeetupCardItem(
  val id: String,
  val dayOfWeek: String,
  val dayOfMonth: String,
  val title: String,
  val clubName: String,
  val clubIcon: String,
  val timeAndDuration: String,
  val costOrAttendees: String,
  val tagText: String,
  val tagType: String, // "primary", "secondary", "tertiary"
  val avatars: List<String>,
  val hostInfo: String? = null,
  var isRsvped: Boolean = false
)

data class DiscussionPostItem(
  val id: String,
  val clubName: String,
  val clubIcon: String,
  val timeAgo: String,
  val authorName: String,
  val authorHandle: String,
  val authorAvatarUrl: String,
  val title: String,
  val bodySnippet: String? = null,
  val mediaUrl: String? = null,
  val mediaBadge: String? = null,
  var replyCount: Int,
  var likeCount: Int,
  var isLiked: Boolean = false,
  var isBookmarked: Boolean = false,
  val statusBadge: String? = null
)

data class ClubItem(
  val id: String,
  val name: String,
  val categoryBadge: String,
  val badgeColorType: String, // "primary", "secondary", "tertiary"
  val iconName: String,
  val tagline: String,
  val memberCountText: String,
  var isJoined: Boolean = false
)
