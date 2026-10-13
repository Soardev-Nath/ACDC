package com.example

import com.example.model.Sender
import com.example.viewmodel.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial messages include 6 messages matching design stream`() {
    val viewModel = ChatViewModel()
    val messages = viewModel.messages.value
    assertEquals(6, messages.size)
    assertEquals(Sender.ELENA, messages[0].sender)
    assertEquals(Sender.SOFIA, messages[2].sender)
  }

  @Test
  fun `sendMessage appends Sofia message to message stream`() {
    val viewModel = ChatViewModel()
    val initialCount = viewModel.messages.value.size
    viewModel.sendMessage("See you at 5:30 with porcelain tools!")

    val updatedMessages = viewModel.messages.value
    assertEquals(initialCount + 1, updatedMessages.size)
    assertEquals(Sender.SOFIA, updatedMessages.last().sender)
    assertEquals("See you at 5:30 with porcelain tools!", updatedMessages.last().text)
  }

  @Test
  fun `sendQuickReply adds quick reply message`() {
    val viewModel = ChatViewModel()
    val initialCount = viewModel.messages.value.size
    viewModel.sendQuickReply("Can't wait!")

    val updatedMessages = viewModel.messages.value
    assertEquals(initialCount + 1, updatedMessages.size)
    assertEquals("Can't wait!", updatedMessages.last().text)
  }

  @Test
  fun `meetupInfo contains correct title and date`() {
    val viewModel = ChatViewModel()
    val meetup = viewModel.meetupInfo.value
    assertEquals("Urban Sunset Pottery & Glaze Jam", meetup.title)
    assertEquals("NOV", meetup.dateBadgeMonth)
    assertEquals("21", meetup.dateBadgeDay)
  }
}
