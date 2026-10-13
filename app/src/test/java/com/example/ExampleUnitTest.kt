package com.example

import com.example.data.model.AcdaProject
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.ui.components.NavigationTab
import com.example.viewmodel.WhatsAppTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun userProfile_adminVerification() {
    val adminByEmail = UserProfile(
      uid = "u1",
      name = "Nathaniel Rop",
      email = "nathanielrop84@gmail.com",
      role = "member"
    )
    assertTrue("Admin email should grant admin access", adminByEmail.isAdmin)

    val adminByRole = UserProfile(
      uid = "u2",
      name = "President",
      email = "president@acda.org",
      role = "admin"
    )
    assertTrue("Role admin should grant admin access", adminByRole.isAdmin)

    val regularMember = UserProfile(
      uid = "u3",
      name = "Member",
      email = "member@example.com",
      role = "member"
    )
    assertFalse("Regular member should not have admin access", regularMember.isAdmin)
  }

  @Test
  fun chatMessage_adminSenderBadge() {
    val adminMsg = ChatMessage(
      id = "m1",
      senderRole = "admin",
      text = "Official Broadcast"
    )
    assertTrue("Admin message should indicate isFromAdmin", adminMsg.isFromAdmin)

    val memberMsg = ChatMessage(
      id = "m2",
      senderRole = "member",
      text = "Hello everyone"
    )
    assertFalse("Member message should not indicate isFromAdmin", memberMsg.isFromAdmin)
  }

  @Test
  fun acdaProject_progressPercentCalculation() {
    val project = AcdaProject(
      id = "p1",
      title = "Water Borehole",
      targetBudget = 10000000.0,
      raisedBudget = 5000000.0
    )
    assertEquals(0.5f, project.progressPercent, 0.001f)

    val zeroBudgetProject = AcdaProject(
      id = "p2",
      title = "Civic Meeting",
      targetBudget = 0.0,
      raisedBudget = 0.0
    )
    assertEquals(0f, zeroBudgetProject.progressPercent, 0.001f)
  }

  @Test
  fun navigationTabs_consistency() {
    assertEquals(6, NavigationTab.values().size)
    assertEquals("Chats", NavigationTab.CHATS.label)
    assertEquals(3, WhatsAppTab.values().size)
  }
}
