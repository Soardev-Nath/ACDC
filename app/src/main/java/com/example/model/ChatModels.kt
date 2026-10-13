package com.example.model

enum class Sender {
  ELENA,
  SOFIA
}

enum class MessageType {
  TEXT,
  MEDIA,
  AUDIO,
  RESERVATION
}

data class ReservationInfo(
  val id: String = "PASS-EF-9482",
  val wheelName: String = "Shimpo Wheel #4",
  val studio: String = "Studio B",
  val timeSlot: String = "5:30 PM – 6:30 PM (Pre-jam session)",
  val status: String = "RESERVED FOR SOFIA",
  val date: String = "Friday, Nov 21, 2026",
  val holderName: String = "Sofia Vance",
  val studioPassCode: String = "EF-WB-04",
  val wheelModel: String = "Shimpo Whisper RK-Whisper 1/2HP",
  val stationAmenities: String = "Bat pins included • Splash pan clean • Water bucket provided"
)

data class ChatMessage(
  val id: String,
  val sender: Sender,
  val type: MessageType,
  val text: String = "",
  val timestamp: String,
  val isRead: Boolean = true,
  val mediaResId: Int? = null,
  val mediaBadge: String? = null,
  val mediaCaption: String? = null,
  val audioDuration: Int = 28,
  val audioPosition: Int = 8,
  val reservation: ReservationInfo? = null
)

data class MeetupInfo(
  val title: String = "Urban Sunset Pottery & Glaze Jam",
  val dateBadgeMonth: String = "NOV",
  val dateBadgeDay: String = "21",
  val timeSummary: String = "Tomorrow 6:30 PM",
  val fullDateTime: String = "Friday, Nov 21 • 6:30 PM – 9:00 PM",
  val location: String = "Earth & Fire Studio • 428 Kiln Way, Arts District",
  val hostName: String = "Elena Rostova",
  val hostRole: String = "Master Ceramist & Studio Founder",
  val rsvpStatus: String = "Going",
  val totalAttendees: Int = 14,
  val description: String = "Join us for our monthly hands-on glaze experimentation evening! We will be testing new Cone 6 reduction recipes including Rutile Amber, Celadon Mist, and Tenmoku Gold. Bring your bisqueware ready for wax resist and dip/brush glazing.",
  val whatToBring: List<String> = listOf(
    "Bisque pieces (up to 4 vessels)",
    "Favorite detail glaze brushes & wax resist",
    "Linen apron & hand towel",
    "Notebook or digital camera for test recording"
  ),
  val stations: List<String> = listOf(
    "Station 1: Dip Tanks (Rutile Amber & Chun Blue)",
    "Station 2: Precision Brushing & Wax Resist",
    "Station 3: Wheel Trimming & Stamp Bar (Studio B)",
    "Station 4: Kiln Staging & Pyrometric Cone Placements"
  )
)

data class GlazeRecipe(
  val name: String = "Rutile Amber Test Tile",
  val firing: String = "Cone 6 Reduction • (2232°F / 1222°C)",
  val finish: String = "Warm honey amber with buttery micro-crystalline breaks",
  val baseFormula: List<Pair<String, String>> = listOf(
    "Custer Feldspar" to "40.0 %",
    "Silica (325 mesh)" to "30.0 %",
    "Whiting (Calcium Carb)" to "20.0 %",
    "EPK (Kaolin)" to "10.0 %"
  ),
  val colorantsAndAdditions: List<Pair<String, String>> = listOf(
    "Rutile (Granular)" to "+ 8.0 %",
    "Red Iron Oxide" to "+ 2.5 %",
    "Bentonite (suspender)" to "+ 1.5 %"
  ),
  val specificGravity: String = "1.46 – 1.48 (creamy pour)",
  val notes: String = "Brushed 2 coats over white stoneware clay. Unloaded warm from reduction kiln. Shows incredible caramel pooling around carved flutes and handles."
)
