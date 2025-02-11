package com.demo.linkhub.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import java.net.URLEncoder

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavController, email: String?) {
    val items = listOf(
        NavItem("Home", Icons.Default.Home, "home/${URLEncoder.encode(email, "UTF-8")}"),
        NavItem("Projects", Icons.AutoMirrored.Filled.List, "projects/${URLEncoder.encode(email, "UTF-8")}"),
        NavItem("Profile", Icons.Default.Person, "profile/${URLEncoder.encode(email, "UTF-8")}"),
        NavItem("Notifications", Icons.Default.Notifications, "notifications/${URLEncoder.encode(email, "UTF-8")}")
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = false,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home") { 
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 