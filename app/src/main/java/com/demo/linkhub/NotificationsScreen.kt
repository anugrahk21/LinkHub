package com.demo.linkhub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.tooling.preview.Preview
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.navigation.compose.rememberNavController
import com.demo.linkhub.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    email: String?
) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var selectedSort by rememberSaveable { mutableStateOf("Newest First") }
    
    val notifications = remember { mutableStateListOf(
        Notification(
            id = "1",
            type = NotificationType.INVITE,
            title = "Project Invitation",
            message = "John Doe invited you to join 'AI Chatbot Dev'",
            timestamp = Date().time - (2 * 60 * 60 * 1000),
            isRead = false
        ),
        Notification(
            id = "2",
            type = NotificationType.REQUEST,
            title = "Approval Needed",
            message = "Review pending for UI/UX design submission",
            timestamp = Date().time - (4 * 60 * 60 * 1000),
            isRead = false
        ),
        Notification(
            id = "3",
            type = NotificationType.GENERAL,
            title = "System Update",
            message = "New app features available in version 2.1",
            timestamp = Date().time - (24 * 60 * 60 * 1000),
            isRead = true
        )
    )}

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, email = email) },
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {}
                    ) {
                        Text(
                            text = selectedSort,
                            modifier = Modifier.padding(end = 16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                containerColor = Color.Transparent
            ) {
                NotificationCategory.values().forEachIndexed { index, category ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(category.title) }
                    )
                }
            }

            when(selectedTab) {
                0 -> NotificationList(
                    notifications = notifications.filter { it.type == NotificationType.INVITE },
                    onAction = { id, action -> handleNotificationAction(id, action) },
                    onDismiss = { notifications.removeAll { it.id == it.id } }
                )
                1 -> NotificationList(
                    notifications = notifications.filter { it.type == NotificationType.REQUEST },
                    onAction = { id, action -> handleNotificationAction(id, action) },
                    onDismiss = { notifications.removeAll { it.id == it.id } }
                )
                2 -> NotificationList(
                    notifications = notifications.filter { it.type == NotificationType.GENERAL },
                    onAction = { id, action -> handleNotificationAction(id, action) },
                    onDismiss = { notifications.removeAll { it.id == it.id } }
                )
            }
        }
    }
}

@Composable
private fun NotificationList(
    notifications: List<Notification>,
    onAction: (String, NotificationAction) -> Unit,
    onDismiss: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        if (notifications.isEmpty()) {
            item {
                Text(
                    text = "No notifications here yet",
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        items(notifications) { notification ->
            NotificationCard(
                notification = notification,
                onAction = onAction,
                onDismiss = onDismiss
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun NotificationCard(
    notification: Notification,
    onAction: (String, NotificationAction) -> Unit,
    onDismiss: (String) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) MaterialTheme.colorScheme.surface 
                            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = { onDismiss(notification.id) }) {
                    Icon(Icons.Filled.Close, contentDescription = "Dismiss")
                }
            }
            
            Text(
                text = notification.message,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTimeSince(notification.timestamp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                when(notification.type) {
                    NotificationType.INVITE -> {
                        Row {
                            Button(
                                onClick = { onAction(notification.id, NotificationAction.ACCEPT) },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Accept")
                            }
                            OutlinedButton(
                                onClick = { onAction(notification.id, NotificationAction.DECLINE) }
                            ) {
                                Text("Decline")
                            }
                        }
                    }
                    NotificationType.REQUEST -> {
                        OutlinedButton(
                            onClick = { onAction(notification.id, NotificationAction.VIEW) }
                        ) {
                            Text("Review")
                        }
                    }
                    NotificationType.GENERAL -> {
                        OutlinedButton(
                            onClick = { onAction(notification.id, NotificationAction.VIEW) }
                        ) {
                            Text("View Details")
                        }
                    }
                }
            }
        }
    }
}

private fun handleNotificationAction(id: String, action: NotificationAction) {
    when(action) {
        NotificationAction.ACCEPT -> { /* Handle accept */ }
        NotificationAction.DECLINE -> { /* Handle decline */ }
        NotificationAction.VIEW -> { /* Handle view details */ }
    }
}

private fun formatTimeSince(timestamp: Long): String {
    val hours = ((Date().time - timestamp) / 3600000).toInt()
    return when {
        hours < 1 -> "Just now"
        hours < 24 -> "$hours hours ago"
        else -> "${hours / 24} days ago"
    }
}

enum class NotificationCategory(val title: String) {
    INVITES("Invites"),
    REQUESTS("Requests"),
    GENERAL("General")
}

enum class NotificationAction {
    ACCEPT, DECLINE, VIEW
}

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: Long,
    var isRead: Boolean
)

enum class NotificationType {
    INVITE, REQUEST, GENERAL
}

@Preview(showBackground = true)
@Composable
fun NotificationsPreview() {
    TechStackTheme {
        NotificationsScreen(
            navController = rememberNavController(),
            email = "user@example.com"
        )
    }
} 