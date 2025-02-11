package com.demo.linkhub.models

data class Project(
    val id: String,
    val title: String,
    val description: String,
    val skillsRequired: List<String>,
    val deadline: Long?,
    val teamSize: Int,
    val membersJoined: Int,
    val owner: String,
    val contact: String,
    val isTrending: Boolean = false,
    val views: Int = 0,
    val likes: Int = 0,
    val ownerEmail: String,
    val contactMethods: List<String> = listOf("In-app", "Email", "Phone")
)

// Sample data declarations (ONLY ONCE IN PROJECT)
val sampleProjects = listOf(
    Project(
        "1", "AI-Powered Healthcare App", 
        "Develop an AI-driven platform for medical recommendations",
        listOf("Python", "TensorFlow", "Flutter"),
        System.currentTimeMillis() + 1209600000L, // 14 days
        5, 3, "HealthTech Inc", "contact@healthtech.com",
        isTrending = true, views = 1500, likes = 245,
        ownerEmail = "health@example.com",
        contactMethods = listOf("Email", "Slack")
    ),
    Project(
        "2", "Smart Home Automation", 
        "IoT solution for home automation systems",
        listOf("Kotlin", "Android", "AWS IoT"),
        null, 4, 1, "IoT Solutions", "info@iot.com",
        isTrending = false, views = 800, likes = 120,
        ownerEmail = "iot@example.com",
        contactMethods = listOf("Email", "Phone")
    ),
    Project(
        "3", "E-Commerce Platform", 
        "Next-gen online shopping experience",
        listOf("React", "Node.js", "MongoDB"),
        System.currentTimeMillis() + 2592000000L, // 30 days
        6, 4, "ShopOnline", "support@shoponline.com",
        isTrending = true, views = 2000, likes = 300,
        ownerEmail = "shop@example.com"
    ),
    // Add 3-5 more projects following the same pattern
)

data class Collaborator(
    val id: String,
    val name: String,
    val skills: List<String>,
    val experience: String,
    val projectsCompleted: Int,
    val profileImage: String = "" // Can be URL or resource ID
)

data class Event(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val description: String,
    val organizer: String
)

val sampleCollaborators = listOf(
    Collaborator(
        "1", "Sarah Johnson",
        listOf("Android", "Kotlin", "Jetpack Compose"),
        "5+ years experience", 12,
        "https://example.com/sarah.jpg"
    ),
    Collaborator(
        "2", "Mike Chen",
        listOf("Python", "ML", "Data Science"),
        "AI Specialist", 8,
        "https://example.com/mike.jpg"
    ),
    Collaborator(
        "3", "Emma Wilson",
        listOf("UI/UX", "Figma", "Prototyping"),
        "Senior Designer", 15,
        "https://example.com/emma.jpg"
    ),
    Collaborator(
        "4", "Alex Martinez",
        listOf("Flutter", "Dart", "Firebase"),
        "Mobile Lead", 9,
        "https://example.com/alex.jpg"
    )
)

val sampleEvents = listOf(
    Event(
        "1", "Android Dev Summit",
        "2023-11-15", "Online",
        "Annual conference for Android developers",
        "Google Developer Relations"
    ),
    Event(
        "2", "AI & ML Workshop",
        "2023-12-01", "San Francisco",
        "Hands-on machine learning workshop",
        "AI Tech Community"
    ),
    Event(
        "3", "UX Design Conference",
        "2024-01-20", "New York",
        "Latest trends in user experience design",
        "Design Leaders Forum"
    )
)