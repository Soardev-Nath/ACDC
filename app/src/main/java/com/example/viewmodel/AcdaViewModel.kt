package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AcdaProject
import com.example.data.model.SystemStats
import com.example.data.model.TownHallTopic
import com.example.data.model.UserProfile
import com.example.data.repository.ProjectRepository
import com.example.data.repository.TownHallRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AcdaViewModel(
  private val projectRepository: ProjectRepository = ProjectRepository(),
  private val townHallRepository: TownHallRepository = TownHallRepository()
) : ViewModel() {

  val projects: StateFlow<List<AcdaProject>> = projectRepository.getProjects()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val topics: StateFlow<List<TownHallTopic>> = townHallRepository.getTopics()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _selectedCategory = MutableStateFlow<String?>(null)
  val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

  val filteredProjects: StateFlow<List<AcdaProject>> = combine(projects, _selectedCategory) { list, category ->
    if (category == null) list else list.filter { it.category.equals(category, ignoreCase = true) }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val systemStats: StateFlow<SystemStats> = combine(projects, topics) { projList, topicList ->
    SystemStats(
      totalMembers = 1250,
      totalChannels = 6,
      totalProjects = projList.size,
      totalTopics = topicList.size,
      totalProjectFunding = projList.sumOf { it.raisedBudget }
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SystemStats())

  fun setCategory(category: String?) {
    _selectedCategory.value = category
  }

  fun createProject(
    title: String,
    description: String,
    category: String,
    targetBudget: Double,
    raisedBudget: Double,
    location: String,
    contractor: String,
    creator: UserProfile
  ) {
    viewModelScope.launch {
      projectRepository.createProject(
        title = title,
        description = description,
        category = category,
        targetBudget = targetBudget,
        raisedBudget = raisedBudget,
        location = location,
        contractor = contractor,
        creator = creator
      )
    }
  }

  fun updateProject(
    projectId: String,
    title: String,
    description: String,
    status: String,
    raisedBudget: Double,
    targetBudget: Double,
    location: String,
    contractor: String
  ) {
    viewModelScope.launch {
      projectRepository.updateProject(
        projectId = projectId,
        title = title,
        description = description,
        status = status,
        raisedBudget = raisedBudget,
        targetBudget = targetBudget,
        location = location,
        contractor = contractor
      )
    }
  }

  fun deleteProject(projectId: String) {
    viewModelScope.launch {
      projectRepository.deleteProject(projectId)
    }
  }

  fun createTopic(
    title: String,
    description: String,
    speaker: String,
    scheduledDate: String,
    creator: UserProfile
  ) {
    viewModelScope.launch {
      townHallRepository.createTopic(title, description, speaker, scheduledDate, creator)
    }
  }

  fun toggleUpvote(topicId: String, userId: String, upvoterIds: List<String>) {
    viewModelScope.launch {
      townHallRepository.toggleUpvote(topicId, userId, upvoterIds)
    }
  }

  fun updateTopicStatus(topicId: String, newStatus: String) {
    viewModelScope.launch {
      townHallRepository.updateTopicStatus(topicId, newStatus)
    }
  }

  fun deleteTopic(topicId: String) {
    viewModelScope.launch {
      townHallRepository.deleteTopic(topicId)
    }
  }
}
