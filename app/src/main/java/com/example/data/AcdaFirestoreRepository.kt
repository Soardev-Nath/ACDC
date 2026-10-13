package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.FirebaseBulletin
import com.example.model.FirebaseChatMessage
import com.example.model.FirebaseDonation
import com.example.model.FirebaseForumPost
import com.example.model.FirebaseProject
import com.example.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AcdaFirestoreRepository(
  private val db: FirebaseFirestore
) {
  // Convenience constructor resolving the named database ID from string resources
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  companion object {
    private const val TAG = "AcdaFirestoreRepo"
    const val USERS_COLLECTION = "users"
    const val PROJECTS_COLLECTION = "projects"
    const val BULLETINS_COLLECTION = "bulletins"
    const val FORUM_POSTS_COLLECTION = "forum_posts"
    const val CHATS_COLLECTION = "chats"
    const val DONATIONS_COLLECTION = "donations"
  }

  // --- Real-time Observables ---

  fun observeProjects(): Flow<List<FirebaseProject>> {
    return db.collection(PROJECTS_COLLECTION)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(FirebaseProject::class.java)
      }
  }

  fun observeBulletins(): Flow<List<FirebaseBulletin>> {
    return db.collection(BULLETINS_COLLECTION)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(FirebaseBulletin::class.java)
          .sortedByDescending { it.isPinned }
      }
  }

  fun observeForumPosts(): Flow<List<FirebaseForumPost>> {
    return db.collection(FORUM_POSTS_COLLECTION)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(FirebaseForumPost::class.java)
          .sortedByDescending { it.upvotes }
      }
  }

  fun observeUserProfile(userId: String): Flow<UserProfile?> {
    return db.collection(USERS_COLLECTION).document(userId)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) snapshot.toObject(UserProfile::class.java) else null
      }
  }

  fun observeAllMembers(): Flow<List<UserProfile>> {
    return db.collection(USERS_COLLECTION)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(UserProfile::class.java)
      }
  }

  fun observeChatMessages(chatId: String): Flow<List<FirebaseChatMessage>> {
    return db.collection(CHATS_COLLECTION).document(chatId)
      .collection("messages")
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(FirebaseChatMessage::class.java)
      }
  }

  fun observeDonations(): Flow<List<FirebaseDonation>> {
    return db.collection(DONATIONS_COLLECTION)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(FirebaseDonation::class.java)
      }
  }

  // --- Mutation Operations ---

  suspend fun saveUserProfile(profile: UserProfile) {
    require(profile.userId.isNotBlank()) { "User ID must not be blank" }
    db.collection(USERS_COLLECTION)
      .document(profile.userId)
      .set(profile)
      .await()
  }

  suspend fun updateMemberRole(userId: String, newRole: String) {
    db.collection(USERS_COLLECTION)
      .document(userId)
      .update("role", newRole)
      .await()
  }

  suspend fun createProject(project: FirebaseProject) {
    val docRef = db.collection(PROJECTS_COLLECTION).document()
    val toSave = if (project.id.isBlank()) project.copy(id = docRef.id) else project
    docRef.set(toSave).await()
  }

  suspend fun updateProject(project: FirebaseProject) {
    require(project.id.isNotBlank()) { "Project ID must not be blank" }
    db.collection(PROJECTS_COLLECTION)
      .document(project.id)
      .set(project)
      .await()
  }

  suspend fun deleteProject(projectId: String) {
    db.collection(PROJECTS_COLLECTION)
      .document(projectId)
      .delete()
      .await()
  }

  suspend fun createBulletin(bulletin: FirebaseBulletin) {
    val docRef = db.collection(BULLETINS_COLLECTION).document()
    val toSave = if (bulletin.id.isBlank()) bulletin.copy(id = docRef.id) else bulletin
    docRef.set(toSave).await()
  }

  suspend fun deleteBulletin(bulletinId: String) {
    db.collection(BULLETINS_COLLECTION)
      .document(bulletinId)
      .delete()
      .await()
  }

  suspend fun createForumPost(post: FirebaseForumPost) {
    val docRef = db.collection(FORUM_POSTS_COLLECTION).document()
    val toSave = if (post.id.isBlank()) post.copy(id = docRef.id) else post
    docRef.set(toSave).await()
  }

  suspend fun deleteForumPost(postId: String) {
    db.collection(FORUM_POSTS_COLLECTION)
      .document(postId)
      .delete()
      .await()
  }

  suspend fun toggleUpvoteForumPost(postId: String, currentUpvotes: Long) {
    db.collection(FORUM_POSTS_COLLECTION)
      .document(postId)
      .update("upvotes", currentUpvotes + 1)
      .await()
  }

  suspend fun sendChatMessage(chatId: String, message: FirebaseChatMessage) {
    val docRef = db.collection(CHATS_COLLECTION).document(chatId)
      .collection("messages").document()
    val toSave = if (message.id.isBlank()) message.copy(id = docRef.id) else message
    docRef.set(toSave).await()
  }

  suspend fun recordDonation(donation: FirebaseDonation) {
    val docRef = db.collection(DONATIONS_COLLECTION).document()
    val toSave = if (donation.id.isBlank()) donation.copy(id = docRef.id) else donation
    docRef.set(toSave).await()
  }

  // --- Seed Initial Production Data if Cloud Database is Empty ---
  suspend fun seedProductionDataIfEmpty(authorId: String = "admin_seed") {
    try {
      val existing = db.collection(PROJECTS_COLLECTION).limit(1).get().await()
      if (existing.isEmpty) {
        Log.i(TAG, "Database empty. Seeding official ACDA production records...")
        
        // Projects
        val p1 = FirebaseProject(
          id = "proj_udege_water",
          title = "Udege Community Solar Water Borehole",
          description = "Deep industrial borehole with solar pumping station and 20,000L overhead distribution tank serving 6,500 residents.",
          category = "WATER",
          budget = 14500000L,
          raised = 9800000L,
          percentage = 68L,
          status = "IN_PROGRESS",
          chapter = "Udege",
          location = "Udege Town Center",
          targetDate = "Nov 2026",
          authorId = authorId
        )
        val p2 = FirebaseProject(
          id = "proj_cultural_museum",
          title = "Afo Cultural Museum & Heritage Archives",
          description = "Preservation center for ancestral artifacts, Odu masquerade history, oral storytelling recordings, and cultural gallery.",
          category = "INFRASTRUCTURE",
          budget = 28000000L,
          raised = 21500000L,
          percentage = 77L,
          status = "IN_PROGRESS",
          chapter = "National",
          location = "Nasarawa Central",
          targetDate = "Jan 2027",
          authorId = authorId
        )
        val p3 = FirebaseProject(
          id = "proj_youth_skills",
          title = "Youth Agribusiness & Digital Skills Hub",
          description = "Vocational training workshop, computer lab, and micro-grant mentorship for 400 young Afo entrepreneurs.",
          category = "SKILLS",
          budget = 18000000L,
          raised = 18000000L,
          percentage = 100L,
          status = "COMPLETED",
          chapter = "Abuja Chapter",
          location = "Secretariat Annex",
          targetDate = "Completed",
          authorId = authorId
        )
        db.collection(PROJECTS_COLLECTION).document(p1.id).set(p1).await()
        db.collection(PROJECTS_COLLECTION).document(p2.id).set(p2).await()
        db.collection(PROJECTS_COLLECTION).document(p3.id).set(p3).await()

        // Bulletins
        val b1 = FirebaseBulletin(
          id = "bull_convention_2026",
          title = "Resolution of the 2026 National Executive Council",
          summary = "Harmonized development levies across diaspora chapters and appointment of youth audit committee.",
          content = "At the national executive congress held in Udege, resolutions were passed regarding infrastructural levies, diaspora chapter voting rights, and educational scholarships for underprivileged sons and daughters of Afo.",
          author = "National Secretariat",
          authorId = authorId,
          chapter = "National",
          isPinned = true,
          date = "October 10, 2026"
        )
        val b2 = FirebaseBulletin(
          id = "bull_health_outreach",
          title = "Annual Free Medical Outreach in Loko & Udege",
          summary = "3-day free surgical, optical, and maternal care mission in partnership with Afo Association in Diaspora.",
          content = "All community members are invited to attend the free medical outreach taking place at the Udege Cottage Hospital starting next Friday.",
          author = "Health Committee",
          authorId = authorId,
          chapter = "Udege",
          isPinned = false,
          date = "October 8, 2026"
        )
        db.collection(BULLETINS_COLLECTION).document(b1.id).set(b1).await()
        db.collection(BULLETINS_COLLECTION).document(b2.id).set(b2).await()

        // Forum Posts
        val f1 = FirebaseForumPost(
          id = "forum_solar_security",
          title = "Installation of Solar Streetlights along Udege-Loko Corridor",
          author = "Chief Yakubu Ibrahim",
          authorId = authorId,
          authorRole = "Council Elder",
          chapter = "Udege",
          category = "Infrastructure",
          content = "Given recent trade traffic, lighting our primary border access corridor will enhance night commerce and community security.",
          upvotes = 42L,
          commentCount = 18L,
          isOfficialResolution = true
        )
        val f2 = FirebaseForumPost(
          id = "forum_scholarship_fund",
          title = "2027 University STEM Scholarship Fund Proposal",
          author = "Dr. Amina Musa",
          authorId = authorId,
          authorRole = "Education Chair",
          chapter = "Abuja Chapter",
          category = "Education",
          content = "Proposing a dedicated endowment targeting tuition for 25 Afo female undergraduates in medicine, engineering, and software.",
          upvotes = 67L,
          commentCount = 29L,
          isOfficialResolution = false
        )
        db.collection(FORUM_POSTS_COLLECTION).document(f1.id).set(f1).await()
        db.collection(FORUM_POSTS_COLLECTION).document(f2.id).set(f2).await()

        Log.i(TAG, "Official ACDA production records seeded successfully.")
      }
    } catch (e: Exception) {
      Log.w(TAG, "Seeding check failed (may be unauthenticated or non-empty)", e)
    }
  }
}
