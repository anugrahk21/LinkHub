package com.demo.linkhub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.demo.linkhub.R
import com.demo.linkhub.components.BottomNavigationBar
import com.demo.linkhub.models.Project
import com.demo.linkhub.components.SkillChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    email: String?
) {
    var isAvailable by rememberSaveable { mutableStateOf(true) }
    val skills = remember { mutableStateOf(listOf("Kotlin", "Android", "Jetpack Compose", "Firebase", "UI/UX")) }
    val projects = remember {
        mutableStateOf(listOf(
            Project(
                "1", 
                "E-Commerce App", 
                "Mobile shopping platform", 
                listOf("Android", "Kotlin"), 
                null, 
                3, 
                2, 
                "Me", 
                "me@example.com",
                isTrending = false,
                views = 0,
                likes = 0,
                ownerEmail = "me@example.com",
                contactMethods = listOf("Email")
            ),
            Project(
                "2", 
                "Health Tracker", 
                "Fitness monitoring system", 
                listOf("Firebase", "Compose"), 
                null, 
                2, 
                1, 
                "Me", 
                "me@example.com",
                isTrending = false,
                views = 0,
                likes = 0,
                ownerEmail = "me@example.com",
                contactMethods = listOf("In-app")
            )
        ))
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, email = email) },
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                actions = {
                    IconButton(onClick = { /* Handle notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Profile Header
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.ic_profile_placeholder),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { /* Handle profile picture change */ },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Icon(Icons.Default.Edit, "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Alex Developer", style = MaterialTheme.typography.headlineMedium)
                    Text("Full-Stack Mobile Developer", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { navController.navigate("editProfile") }) {
                            Text("Edit Profile")
                        }
                        FilledTonalButton(
                            onClick = { isAvailable = !isAvailable },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAvailable) Color.Green.copy(alpha = 0.2f) 
                                               else Color.Red.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(if (isAvailable) "Available" else "Busy", 
                                color = if (isAvailable) Color.Green else Color.Red)
                        }
                    }
                }
            }

            // Skills Section
            item {
                ProfileSectionHeader(title = "Technical Skills", action = "Edit")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(skills.value) { skill ->
                        SkillChip(skill = skill, onRemove = { /* Handle remove */ })
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Projects Section
            item {
                ProfileSectionHeader(title = "My Projects", action = "View All")
                projects.value.forEach { project ->
                    ProjectItem(project = project)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Social Links
            item {
                ProfileSectionHeader(title = "Social Links", action = "Add")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialLink(icon = Icons.Default.Person, label = "LinkedIn")
                    SocialLink(icon = Icons.Default.Work, label = "GitHub")
                    SocialLink(icon = Icons.Default.Person, label = "Portfolio")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Connections
            item {
                ProfileSectionHeader(title = "Connections", action = "See All")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(10) { index ->
                        ConnectionItem(name = "User ${index + 1}")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSectionHeader(title: String, action: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(action, 
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge)
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun ProjectItem(project: Project) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: Active",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SocialLink(icon: ImageVector, label: String) {
    FilledTonalButton(
        onClick = { /* Handle social link */ },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun ConnectionItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.ic_profile_placeholder),
            contentDescription = "Connection",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Text(name, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TechStackTheme {
        ProfileScreen(
            navController = rememberNavController(),
            email = "user@example.com"
        )
    }
}