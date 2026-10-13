package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatChannel
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class WhatsAppTab(val title: String) {
  CHATS("CHATS"),
  STATUS("STATUS"),
  CALLS("CALLS")
}

enum class ChatFilter {
  ALL, UNREAD, GROUPS
}

data class ActiveCallState(
  val contactName: String,
  val isVideo: Boolean = false,
  val durationSeconds: Int = 0
)

data class ActiveStoryState(
  val authorName: String,
  val title: String,
  val timeAgo: String = "Today, 10:15 AM"
)

@OptIn(ExperimentalCoroutinesApi::class)
class WhatsAppChatViewModel(
  private val chatRepository: ChatRepository = ChatRepository()
) : ViewModel() {

  val allChannels: StateFlow<List<ChatChannel>> = chatRepository.getChannels()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _activeTab = MutableStateFlow(WhatsAppTab.CHATS)
  val activeTab: StateFlow<WhatsAppTab> = _activeTab.asStateFlow()

  private val _activeFilter = MutableStateFlow(ChatFilter.ALL)
  val activeFilter: StateFlow<ChatFilter> = _activeFilter.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _activeChannelId = MutableStateFlow<String?>(null)
  val activeChannelId: StateFlow<String?> = _activeChannelId.asStateFlow()

  val activeChannel: StateFlow<ChatChannel?> = combine(allChannels, activeChannelId) { list, id ->
    list.find { it.id == id }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val messages: StateFlow<List<ChatMessage>> = _activeChannelId.flatMapLatest { id ->
    if (id != null) chatRepository.getMessages(id) else flowOf(emptyList())
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredChannels: StateFlow<List<ChatChannel>> = combine(allChannels, _activeFilter, _searchQuery) { channels, filter, query ->
    channels.filter { channel ->
      val matchesQuery = query.isEmpty() || channel.name.contains(query, ignoreCase = true) || channel.lastMessage.contains(query, ignoreCase = true)
      val matchesFilter = when (filter) {
        ChatFilter.ALL -> true
        ChatFilter.UNREAD -> channel.unreadCount > 0
        ChatFilter.GROUPS -> channel.isGroup
      }
      matchesQuery && matchesFilter
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val totalUnreadCount: StateFlow<Int> = allChannels.combine(_activeChannelId) { channels, _ ->
    channels.sumOf { it.unreadCount }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  // Calls and Stories
  private val _activeCall = MutableStateFlow<ActiveCallState?>(null)
  val activeCall: StateFlow<ActiveCallState?> = _activeCall.asStateFlow()

  private val _activeStory = MutableStateFlow<ActiveStoryState?>(null)
  val activeStory: StateFlow<ActiveStoryState?> = _activeStory.asStateFlow()

  fun setTab(tab: WhatsAppTab) {
    _activeTab.value = tab
  }

  fun setFilter(filter: ChatFilter) {
    _activeFilter.value = filter
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun openChannel(channelId: String) {
    _activeChannelId.value = channelId
  }

  fun closeChannel() {
    _activeChannelId.value = null
  }

  fun sendMessage(sender: UserProfile, text: String, type: String = "text") {
    val channelId = _activeChannelId.value ?: return
    if (text.isBlank()) return
    viewModelScope.launch {
      chatRepository.sendMessage(channelId, sender, text.trim(), type)
    }
  }

  fun createNewChannel(
    name: String,
    description: String,
    isGroup: Boolean,
    isAnnouncement: Boolean,
    creator: UserProfile
  ) {
    viewModelScope.launch {
      val newId = chatRepository.createChannel(name, description, isGroup, isAnnouncement, creator)
      _activeChannelId.value = newId
    }
  }

  fun deleteMessage(messageId: String) {
    val channelId = _activeChannelId.value ?: return
    viewModelScope.launch {
      chatRepository.deleteMessage(channelId, messageId)
    }
  }

  fun startCall(contactName: String, isVideo: Boolean) {
    _activeCall.value = ActiveCallState(contactName, isVideo)
  }

  fun endCall() {
    _activeCall.value = null
  }

  fun viewStory(authorName: String, title: String) {
    _activeStory.value = ActiveStoryState(authorName, title)
  }

  fun closeStory() {
    _activeStory.value = null
  }
}
