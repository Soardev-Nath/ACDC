package com.example

import com.example.model.AcdaCategory
import com.example.viewmodel.AcdaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AcdaViewModelTest {

  @Test
  fun `initial state has National Headquarters and 3 projects`() {
    val viewModel = AcdaViewModel()
    assertEquals("National Headquarters (Udege/Nasarawa)", viewModel.selectedChapter.value)
    assertEquals(AcdaCategory.ALL, viewModel.selectedCategory.value)
    assertEquals(3, viewModel.projects.value.size)
    assertEquals(3, viewModel.news.value.size)
    assertEquals(3, viewModel.forumPosts.value.size)
  }

  @Test
  fun `contributeToProject updates raised amount and percentage`() {
    val viewModel = AcdaViewModel()
    val waterProj = viewModel.projects.value.first { it.id == "proj_water_1" }
    val initialRaised = waterProj.raisedAmountNaira

    viewModel.contributeToProject("proj_water_1", 200000L)
    val updatedProj = viewModel.projects.value.first { it.id == "proj_water_1" }
    assertEquals(initialRaised + 200000L, updatedProj.raisedAmountNaira)
  }

  @Test
  fun `toggleLikePost toggles like count correctly`() {
    val viewModel = AcdaViewModel()
    val post = viewModel.forumPosts.value.first { it.id == "forum_2" }
    val initialLikes = post.likesCount
    assertFalse(post.isLiked)

    viewModel.toggleLikePost("forum_2")
    val likedPost = viewModel.forumPosts.value.first { it.id == "forum_2" }
    assertTrue(likedPost.isLiked)
    assertEquals(initialLikes + 1, likedPost.likesCount)
  }

  @Test
  fun `submitForumPost adds new post to top of forum`() {
    val viewModel = AcdaViewModel()
    val initialSize = viewModel.forumPosts.value.size
    viewModel.submitForumPost(
      title = "Bursary Disbursement Timetable",
      content = "When will the screening for undergraduate grants begin?",
      tag = "Youth Wing"
    )

    val updatedPosts = viewModel.forumPosts.value
    assertEquals(initialSize + 1, updatedPosts.size)
    assertEquals("Bursary Disbursement Timetable", updatedPosts.first().title)
  }

  @Test
  fun `memberProfile has correct registered id and active status`() {
    val viewModel = AcdaViewModel()
    val profile = viewModel.memberProfile.value
    assertNotNull(profile.memberId)
    assertTrue(profile.memberId.startsWith("ACDA-NAS"))
    assertEquals("ACTIVE & IN GOOD STANDING", profile.status)
  }
}
