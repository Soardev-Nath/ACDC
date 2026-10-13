package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.ClubItem
import com.example.model.DiscussionPostItem
import com.example.model.ExploreFilterCategory
import com.example.model.MeetupCardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExploreViewModel : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedFilter = MutableStateFlow(ExploreFilterCategory.ALL)
  val selectedFilter: StateFlow<ExploreFilterCategory> = _selectedFilter.asStateFlow()

  private val _selectedLocation = MutableStateFlow("Oakland & Bay Area")
  val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

  private val _isFeaturedJoined = MutableStateFlow(false)
  val isFeaturedJoined: StateFlow<Boolean> = _isFeaturedJoined.asStateFlow()

  private val _meetups = MutableStateFlow<List<MeetupCardItem>>(initialMeetups())
  val meetups: StateFlow<List<MeetupCardItem>> = _meetups.asStateFlow()

  private val _discussions = MutableStateFlow<List<DiscussionPostItem>>(initialDiscussions())
  val discussions: StateFlow<List<DiscussionPostItem>> = _discussions.asStateFlow()

  private val _popularClubs = MutableStateFlow<List<ClubItem>>(initialClubs())
  val popularClubs: StateFlow<List<ClubItem>> = _popularClubs.asStateFlow()

  private val _showCreateDialog = MutableStateFlow(false)
  val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun onFilterSelected(filter: ExploreFilterCategory) {
    _selectedFilter.value = filter
  }

  fun onLocationSelected(location: String) {
    _selectedLocation.value = location
  }

  fun toggleFeaturedJoin() {
    _isFeaturedJoined.value = !_isFeaturedJoined.value
  }

  fun toggleMeetupRsvp(meetupId: String) {
    _meetups.value = _meetups.value.map {
      if (it.id == meetupId) it.copy(isRsvped = !it.isRsvped) else it
    }
  }

  fun togglePostLike(postId: String) {
    _discussions.value = _discussions.value.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else post.likeCount - 1
        post.copy(isLiked = newLiked, likeCount = newCount)
      } else post
    }
  }

  fun togglePostBookmark(postId: String) {
    _discussions.value = _discussions.value.map { post ->
      if (post.id == postId) post.copy(isBookmarked = !post.isBookmarked) else post
    }
  }

  fun toggleClubJoin(clubId: String) {
    _popularClubs.value = _popularClubs.value.map { club ->
      if (club.id == clubId) club.copy(isJoined = !club.isJoined) else club
    }
  }

  fun setShowCreateDialog(show: Boolean) {
    _showCreateDialog.value = show
  }

  fun createDiscussionPost(clubName: String, title: String, content: String) {
    val newPost = DiscussionPostItem(
      id = "post_${System.currentTimeMillis()}",
      clubName = clubName,
      clubIcon = "palette",
      timeAgo = "Just now",
      authorName = "Sofia Vance",
      authorHandle = "@sofia_v",
      authorAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCcsZd1MkxiDQTURSnkNvsVWMuWGYjgaoeTyLqQCQFHsFCT14wN6-sTJy4cuNxcKojO7_0pqmsRZQhIOW9nN_N69TE-BF--RyiynsDyf03wNhTtu4fNWka3Sz6Ee-AM0eD68L14i9GfzGRYEAc9fBRWPFdrSlAk-iFSYNZAEZDDSJylzntIYXWkJzG7hYwtvxOp0bJNGbnWR1S_OPK3BNARXj9jllXAGzL2XJm4zsbkzB9nhFeTQaMt",
      title = title,
      bodySnippet = content,
      replyCount = 0,
      likeCount = 1,
      isLiked = true,
      statusBadge = "New discussion"
    )
    _discussions.value = listOf(newPost) + _discussions.value
    _showCreateDialog.value = false
  }

  private fun initialMeetups(): List<MeetupCardItem> {
    return listOf(
      MeetupCardItem(
        id = "meetup_pottery",
        dayOfWeek = "THU",
        dayOfMonth = "18",
        title = "Urban Sunset Pottery Jam",
        clubName = "Earth & Clay Club",
        clubIcon = "nest_eco_leaf",
        timeAndDuration = "6:30 PM • 2.5 hrs",
        costOrAttendees = "$15 material fee",
        tagText = "18/20 spots filled",
        tagType = "secondary",
        avatars = listOf(
          "https://lh3.googleusercontent.com/aida-public/AB6AXuDsCQeNT721ICBlCRUABs5skhDQchM9KnNsDr1xJW1VNlRCSg1nD2A4B3mYgaKq4aT-rygaA2LzITwnpSWomEi2cc42pm9RWyqpZpoMILYltvcA7FhCqbl92Sxun_-KzjaTJh4ZSstvHiOznlx4Ra2WT4AzuJYJAhkKgPTwuDv5PCFLVJRILHTSlVX2KLv7ia75Vs-QrHo93DTMx2rbU6WjY5_JdAbfIbX77GByOdX6n4MQ9onpKOey",
          "https://lh3.googleusercontent.com/aida-public/AB6AXuBI88ISETg-BSae-hDaMS4VR8U63lmYzmcACs2qGfLDjWOKue_JuaSw74BSNl4o3VfyzpYpewhMlLTC0YjTRLCsqhD-4aIThI1-J8zCd5NduEMESXE3lwI2EYnXdl0uus9kG4bF-cFlUH4bEcXHOY594Lg62-_qGix6R16kZoA8tn98zcvPKQmGUQX5N8TiqvKl8JcB9Lr1nJfSuQ_U1iclwR_GtlBItZhJ0pVN5ZQLA0TTELnupPzJ"
        ),
        hostInfo = "Elena Rostova",
        isRsvped = true
      ),
      MeetupCardItem(
        id = "meetup_run",
        dayOfWeek = "SAT",
        dayOfMonth = "20",
        title = "Golden Gate 10k Shakeout Run",
        clubName = "SF Striders & Weekend Trail",
        clubIcon = "sprint",
        timeAndDuration = "7:30 AM • Crissy Field",
        costOrAttendees = "64 attending",
        tagText = "Free",
        tagType = "surface",
        avatars = listOf(
          "https://lh3.googleusercontent.com/aida-public/AB6AXuCcsZd1MkxiDQTURSnkNvsVWMuWGYjgaoeTyLqQCQFHsFCT14wN6-sTJy4cuNxcKojO7_0pqmsRZQhIOW9nN_N69TE-BF--RyiynsDyf03wNhTtu4fNWka3Sz6Ee-AM0eD68L14i9GfzGRYEAc9fBRWPFdrSlAk-iFSYNZAEZDDSJylzntIYXWkJzG7hYwtvxOp0bJNGbnWR1S_OPK3BNARXj9jllXAGzL2XJm4zsbkzB9nhFeTQaMt",
          "https://lh3.googleusercontent.com/aida-public/AB6AXuAX2dn3nLL1U118kTSVOZBDnzWomwWoSmDZ4PV4SaXrkH9dat55DfqD1KSzGarsHX63poYVzzCuGufdj1NJBA96YQVvW1qKznS5cTB7HPZvg-wQn-AoRynWSRMP00-Y-eOFi6aRQ5XOmPi2jKjBTwUHxXCb7htDM2_2rhsSqSpVxDtIczrMzWjiCrIllLhoxRmozjPOxxiWYM6xFulCV9op7qmOv94kij1VRLRSIJyGRGeIb8u2AcBO"
        ),
        hostInfo = "All paces welcome",
        isRsvped = false
      ),
      MeetupCardItem(
        id = "meetup_photo",
        dayOfWeek = "SUN",
        dayOfMonth = "21",
        title = "Analog 35mm Photo Walk & Zine Edit",
        clubName = "Film & Photo Collective",
        clubIcon = "camera",
        timeAndDuration = "2:00 PM • Fruitvale Plaza",
        costOrAttendees = "Bring 1 roll of film",
        tagText = "12 attending",
        tagType = "surface",
        avatars = listOf(
          "https://lh3.googleusercontent.com/aida-public/AB6AXuAurpikjYABRbaPXusq7D3KP_iTkO2bcoBj3-mAXu9o7pPZkbJdRqVvQMSgz-uz_LtWEznwaxnRfFQUpUSQWvwCrAPXO6GII_1_qzS3ny2uoRqENztD5O1ZF2F2aQugAfAD0DQoBEp1Oc7qm4wIxvVFVoyuOmCnZ0jxjRp28eHZPlg4zq2WZozuv0puCMQGU9D8kVqnbKgIT-V3aDt1NFAv650JLMyT3M7COThKAcbE5YSN-SpEorai"
        ),
        hostInfo = "Hosted by Maya R.",
        isRsvped = false
      )
    )
  }

  private fun initialDiscussions(): List<DiscussionPostItem> {
    return listOf(
      DiscussionPostItem(
        id = "disc_1",
        clubName = "Film & Photo Collective",
        clubIcon = "camera_roll",
        timeAgo = "2h ago",
        authorName = "Devon Vance",
        authorHandle = "@devonfilm",
        authorAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAurpikjYABRbaPXusq7D3KP_iTkO2bcoBj3-mAXu9o7pPZkbJdRqVvQMSgz-uz_LtWEznwaxnRfFQUpUSQWvwCrAPXO6GII_1_qzS3ny2uoRqENztD5O1ZF2F2aQugAfAD0DQoBEp1Oc7qm4wIxvVFVoyuOmCnZ0jxjRp28eHZPlg4zq2WZozuv0puCMQGU9D8kVqnbKgIT-V3aDt1NFAv650JLMyT3M7COThKAcbE5YSN-SpEorai",
        title = "Where are you developing Kodak Gold 200 locally with fast scan turnaround?",
        bodySnippet = "Dropped 3 rolls in Downtown Berkeley last week but need scans ready before Sunday's gallery submission. Any local community darkrooms doing same-day C-41?",
        replyCount = 34,
        likeCount = 82,
        isLiked = true,
        statusBadge = "Active thread"
      ),
      DiscussionPostItem(
        id = "disc_2",
        clubName = "Oakland Sourdough Bakers",
        clubIcon = "bakery_dining",
        timeAgo = "4h ago",
        authorName = "Elena Rostova",
        authorHandle = "@elena_bakes",
        authorAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAMbP98dCCbIezRXyxlkRw08-tBV7yItHdh-0N1JP0pUSNyjcPBxbUl9HaBl5nx3lmOO8gyJ0lyRPRzTuxQe7Fz6UrfT3hWQ71v0Yb-bblZJsYV5J-b71cehX-5--cbc4E0qDSnTf0_YxbOqAV5CTjyP_85UMoIhB4fXhEFWCTaoMArEujEnLe5IYrXxcqOptzyzUiYoYs2K4PcwNkkFK2VLIvXxO8Q5I8HihhL7P9kJOlhZjppxHIE",
        title = "Crumb check! 78% hydration country loaf baked in cast iron combo cooker",
        mediaUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDdLbeyKXKi1ukFkYxO6wfITvfFQt1WQxd1c2ulwmkqO6sAo9nrNhb8quazeqcbzHHBxsbD3gfJj1ylVI9XZrbo2co24VhCmUd5P1pJqiAjSMxJYmL7_yn6HQKiv7xmU46TSkeyYh_WD-Ud-gN-KwLKf_JvktIIEX2tqYPkAcgRFHGy3jywruytVZMhMusgT9nfWlEqmJ_Ub3v3BRVN0mzTPXOwYxJ12tkkemZqTKf0lq-LqU3XNryk",
        mediaBadge = "Cold retard: 14 hrs",
        replyCount = 45,
        likeCount = 129,
        isLiked = false,
        statusBadge = "Last reply 4m ago"
      )
    )
  }

  private fun initialClubs(): List<ClubItem> {
    return listOf(
      ClubItem(
        id = "club_botanists",
        name = "Bay Urban Botanists",
        categoryBadge = "Nature",
        badgeColorType = "secondary",
        iconName = "potted_plant",
        tagline = "Cuttings swaps & greenhouse crawls",
        memberCountText = "840 members • Weekly meetups",
        isJoined = false
      ),
      ClubItem(
        id = "club_coffee",
        name = "East Bay Filter Coffee Guild",
        categoryBadge = "Gastronomy",
        badgeColorType = "tertiary",
        iconName = "local_cafe",
        tagline = "Blind cuppings & roaster visits",
        memberCountText = "512 members • Bi-weekly",
        isJoined = false
      ),
      ClubItem(
        id = "club_woodcraft",
        name = "Makers & Whittlers Guild",
        categoryBadge = "Craft",
        badgeColorType = "primary",
        iconName = "carpenter",
        tagline = "Woodcraft, tools & spoon carving",
        memberCountText = "320 members • Monthly jam",
        isJoined = false
      )
    )
  }
}
