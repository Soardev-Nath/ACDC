package com.example

import com.example.model.ExploreFilterCategory
import com.example.viewmodel.ExploreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExploreViewModelTest {

  @Test
  fun `initial state has Oakland location and 3 meetups`() {
    val viewModel = ExploreViewModel()
    assertEquals("Oakland & Bay Area", viewModel.selectedLocation.value)
    assertEquals(ExploreFilterCategory.ALL, viewModel.selectedFilter.value)
    assertEquals(3, viewModel.meetups.value.size)
    assertEquals(2, viewModel.discussions.value.size)
    assertEquals(3, viewModel.popularClubs.value.size)
  }

  @Test
  fun `toggleFeaturedJoin toggles joined status`() {
    val viewModel = ExploreViewModel()
    assertFalse(viewModel.isFeaturedJoined.value)
    viewModel.toggleFeaturedJoin()
    assertTrue(viewModel.isFeaturedJoined.value)
  }

  @Test
  fun `toggleMeetupRsvp changes rsvp status`() {
    val viewModel = ExploreViewModel()
    val initialRsvp = viewModel.meetups.value.first { it.id == "meetup_run" }.isRsvped
    assertFalse(initialRsvp)

    viewModel.toggleMeetupRsvp("meetup_run")
    val updatedRsvp = viewModel.meetups.value.first { it.id == "meetup_run" }.isRsvped
    assertTrue(updatedRsvp)
  }

  @Test
  fun `togglePostLike increments and decrements like count`() {
    val viewModel = ExploreViewModel()
    val initialPost = viewModel.discussions.value.first { it.id == "disc_2" }
    val initialCount = initialPost.likeCount
    assertFalse(initialPost.isLiked)

    viewModel.togglePostLike("disc_2")
    val likedPost = viewModel.discussions.value.first { it.id == "disc_2" }
    assertTrue(likedPost.isLiked)
    assertEquals(initialCount + 1, likedPost.likeCount)

    viewModel.togglePostLike("disc_2")
    val unlikedPost = viewModel.discussions.value.first { it.id == "disc_2" }
    assertFalse(unlikedPost.isLiked)
    assertEquals(initialCount, unlikedPost.likeCount)
  }

  @Test
  fun `createDiscussionPost adds new post to top of discussions`() {
    val viewModel = ExploreViewModel()
    val initialSize = viewModel.discussions.value.size
    viewModel.createDiscussionPost(
      clubName = "Earth & Clay Club",
      title = "New reduction firing schedule",
      content = "Testing gas draft cones 06 to 6"
    )

    val updatedPosts = viewModel.discussions.value
    assertEquals(initialSize + 1, updatedPosts.size)
    assertEquals("New reduction firing schedule", updatedPosts.first().title)
    assertEquals("Sofia Vance", updatedPosts.first().authorName)
  }
}
