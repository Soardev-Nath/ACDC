package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.ChatMessage
import com.example.model.GlazeRecipe
import com.example.model.MeetupInfo
import com.example.model.MessageType
import com.example.model.ReservationInfo
import com.example.model.Sender
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {

  private val _messages = MutableStateFlow<List<ChatMessage>>(initialMessages())
  val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

  private val _isElenaTyping = MutableStateFlow(true)
  val isElenaTyping: StateFlow<Boolean> = _isElenaTyping.asStateFlow()

  private val _inputText = MutableStateFlow("")
  val inputText: StateFlow<String> = _inputText.asStateFlow()

  private val _isPlayingAudio = MutableStateFlow(false)
  val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

  private val _audioPlayProgress = MutableStateFlow(0.28f)
  val audioPlayProgress: StateFlow<Float> = _audioPlayProgress.asStateFlow()

  private val _meetupInfo = MutableStateFlow(MeetupInfo())
  val meetupInfo: StateFlow<MeetupInfo> = _meetupInfo.asStateFlow()

  private val _selectedReservation = MutableStateFlow<ReservationInfo?>(null)
  val selectedReservation: StateFlow<ReservationInfo?> = _selectedReservation.asStateFlow()

  private val _glazeRecipe = MutableStateFlow(GlazeRecipe())
  val glazeRecipe: StateFlow<GlazeRecipe> = _glazeRecipe.asStateFlow()

  private var audioJob: Job? = null

  fun onInputTextChanged(text: String) {
    _inputText.value = text
  }

  fun sendMessage(customText: String? = null) {
    val textToSend = (customText ?: _inputText.value).trim()
    if (textToSend.isEmpty()) return

    val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
    val newMessage = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      sender = Sender.SOFIA,
      type = MessageType.TEXT,
      text = textToSend,
      timestamp = currentTime,
      isRead = true
    )

    _messages.value = _messages.value + newMessage
    if (customText == null) {
      _inputText.value = ""
    }

    triggerElenaResponse(textToSend)
  }

  fun sendQuickReply(replyText: String) {
    sendMessage(replyText)
  }

  fun toggleAudio() {
    if (_isPlayingAudio.value) {
      audioJob?.cancel()
      _isPlayingAudio.value = false
    } else {
      _isPlayingAudio.value = true
      audioJob = viewModelScope.launch {
        var progress = 0f
        while (progress <= 1f) {
          _audioPlayProgress.value = progress
          delay(280) // 28 seconds total
          progress += 0.01f
        }
        _audioPlayProgress.value = 0f
        _isPlayingAudio.value = false
      }
    }
  }

  fun setSelectedReservation(reservation: ReservationInfo) {
    _selectedReservation.value = reservation
  }

  fun clearSelectedReservation() {
    _selectedReservation.value = null
  }

  fun attachBisquePhoto() {
    val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
    val photoMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      sender = Sender.SOFIA,
      type = MessageType.MEDIA,
      mediaResId = R.drawable.img_glaze_tile,
      mediaBadge = "Sofia's Bisque Mugs • Ready for rutile dip",
      mediaCaption = "Here are the two trimmed mug bases! Clay body is Laguna B-Mix 5 with light grog.",
      timestamp = currentTime
    )
    _messages.value = _messages.value + photoMsg
    triggerElenaResponse("mugs")
  }

  fun requestWheelBooking() {
    sendMessage("Can I reserve Wheel #4 again for the Sunday glaze session?")
  }

  fun shareGlazeFormula() {
    sendMessage("Could you also bring the copper carbonate powder tomorrow? Would love to test a turquoise break!")
  }

  fun sendStudioLocation() {
    sendMessage("See you at 428 Kiln Way tomorrow evening!")
  }

  private fun triggerElenaResponse(userText: String) {
    _isElenaTyping.value = true
    viewModelScope.launch {
      delay(2200)
      _isElenaTyping.value = false
      val replyTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
      val replyText = when {
        userText.contains("porcelain", ignoreCase = true) || userText.contains("tools", ignoreCase = true) ->
          "Perfect! I'll leave the Kemper trimming blades right next to Wheel #4 for you. Can't wait! 🏺✨"
        userText.contains("copper", ignoreCase = true) || userText.contains("recipe", ignoreCase = true) ->
          "Yes! I have a fresh jar of 200-mesh copper carb ready in the chemical cabinet. We'll run a mini test strip!"
        userText.contains("wheel", ignoreCase = true) || userText.contains("Sunday", ignoreCase = true) ->
          "Booked! Added you to the Sunday studio board for Wheel #4 from 2:00 PM – 4:00 PM."
        userText.contains("wait", ignoreCase = true) ->
          "It's going to be our best glaze session yet! Bring your notebook so we can log all the dip densities."
        else ->
          "Sounds wonderful Sofia! Let me know if you need anything else before tomorrow evening. See you on the wheel! 🙌"
      }

      val replyMsg = ChatMessage(
        id = "msg_elena_${System.currentTimeMillis()}",
        sender = Sender.ELENA,
        type = MessageType.TEXT,
        text = replyText,
        timestamp = replyTime,
        isRead = true
      )
      _messages.value = _messages.value + replyMsg
    }
  }

  private fun initialMessages(): List<ChatMessage> {
    return listOf(
      ChatMessage(
        id = "msg_1",
        sender = Sender.ELENA,
        type = MessageType.TEXT,
        text = "Hey Sofia! Excited to see you at tomorrow's glaze jam. Make sure to bring your bisque pieces if you have any ready from last week's handbuilding session! 🏺",
        timestamp = "4:18 PM"
      ),
      ChatMessage(
        id = "msg_2",
        sender = Sender.ELENA,
        type = MessageType.MEDIA,
        mediaResId = R.drawable.img_glaze_tile,
        mediaBadge = "Cone 6 Reduction • Rutile Amber Test",
        mediaCaption = "Just unloaded the kiln! This is the recipe we're brushing on tomorrow evening. It turns out so warm and earthy.",
        timestamp = "4:20 PM"
      ),
      ChatMessage(
        id = "msg_3",
        sender = Sender.SOFIA,
        type = MessageType.TEXT,
        text = "That glaze looks gorgeous! Can't wait. Quick question: is there extra wheel time before the kiln load? I wanted to trim two mug bases.",
        timestamp = "4:22 PM",
        isRead = true
      ),
      ChatMessage(
        id = "msg_4",
        sender = Sender.ELENA,
        type = MessageType.AUDIO,
        audioDuration = 28,
        audioPosition = 8,
        timestamp = "4:25 PM"
      ),
      ChatMessage(
        id = "msg_5",
        sender = Sender.ELENA,
        type = MessageType.RESERVATION,
        reservation = ReservationInfo(
          id = "PASS-EF-9482",
          wheelName = "Shimpo Wheel #4",
          studio = "Studio B",
          timeSlot = "5:30 PM – 6:30 PM (Pre-jam session)",
          status = "RESERVED FOR SOFIA",
          date = "Friday, Nov 21, 2026",
          holderName = "Sofia Vance",
          studioPassCode = "EF-WB-04"
        ),
        timestamp = "4:26 PM"
      ),
      ChatMessage(
        id = "msg_6",
        sender = Sender.SOFIA,
        type = MessageType.TEXT,
        text = "Elena, you are an absolute lifesaver! I will be there at 5:30 sharp with the porcelain tools. See you tomorrow! 🙌",
        timestamp = "4:28 PM",
        isRead = true
      )
    )
  }
}
