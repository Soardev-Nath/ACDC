package com.example.model

enum class AcdaCategory(val label: String, val icon: String) {
  ALL("All Updates", "dashboard"),
  PROJECTS("Development Projects", "build"),
  NEWS("Official News", "campaign"),
  TOWN_HALL("Town Hall Forum", "forum"),
  MEMBERSHIP("Membership & Chapters", "badge")
}

data class AcdaProject(
  val id: String,
  val title: String,
  val category: String,
  val location: String,
  val targetAmountNaira: Long,
  val raisedAmountNaira: Long,
  val progressPercent: Int,
  val statusText: String, // "Under Construction", "85% Completed", "Procurement Phase"
  val description: String,
  val imageResId: Int,
  val beneficiaries: String,
  val completionTargetDate: String,
  val projectCommitteeLead: String
)

data class AcdaNewsBulletin(
  val id: String,
  val title: String,
  val date: String,
  val category: String, // "Executive Communique", "Youth Empowerment", "Cultural Festival", "Public Notice"
  val author: String,
  val authorRole: String,
  val summary: String,
  val fullContent: String,
  val isImportant: Boolean = false
)

data class AcdaForumPost(
  val id: String,
  val authorName: String,
  val authorRoleOrChapter: String,
  val avatarUrl: String? = null,
  val timeAgo: String,
  val title: String,
  val content: String,
  var likesCount: Int,
  var repliesCount: Int,
  var isLiked: Boolean = false,
  val tag: String
)

data class AcdaMemberProfile(
  val memberName: String = "Nathaniel Rop (Hon. Member)",
  val memberId: String = "ACDA-NAS-2026-0842",
  val chapter: String = "National Chapter (Nasarawa / Udege)",
  val membershipType: String = "Full Registered Member",
  val registrationDate: String = "January 2024",
  val status: String = "ACTIVE & IN GOOD STANDING",
  val duesStatus: String = "Annual Levy Cleared"
)

data class AcdaExecutive(
  val name: String,
  val role: String,
  val chapter: String,
  val contact: String
)
