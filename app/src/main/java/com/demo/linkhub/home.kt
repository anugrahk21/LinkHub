package com.demo.linkhub

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.clickable
import com.demo.linkhub.models.Collaborator
import com.demo.linkhub.models.Event
import com.demo.linkhub.models.Project
import com.demo.linkhub.models.sampleCollaborators
import com.demo.linkhub.models.sampleEvents
import com.demo.linkhub.models.sampleProjects
import com.demo.linkhub.components.BottomNavigationBar
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.demo.linkhub.components.SkillChip
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder

@Composable
fun HomeScreen(
    navController: NavController,
    email: String?  // Now comes from navigation arguments
) {
    val navigationItems = listOf("Home", "Projects", "Profile", "Notifications")
    val selectedItem by rememberSaveable { mutableStateOf("Home") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Trending") }
    val filterOptions = listOf("Trending", "Newest", "Popular", "Ending Soon")
    
    // Safe email handling
    val displayName = email?.substringBefore('@') ?: "User"
    
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, email = email) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Welcome Header
            item { 
                Text(
                    text = "Welcome, $displayName!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionButton(
                        text = "Post Project",
                        icon = Icons.Default.Business,
                        onClick = { navController.navigate("postProject") }
                    )
                    ActionButton(
                        text = "Explore People",
                        icon = Icons.Default.Group,
                        onClick = { navController.navigate("explorePeople") }
                    )
                    ActionButton(
                        text = "Browse",
                        icon = Icons.Default.Search,
                        onClick = { /* Handle browse projects */ }
                    )
                }
            }
            
            // Search Bar
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* Implement search */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Trending Projects
            item {
                SectionHeader(
                    title = "Trending Projects", 
                    action = {
                        FilterDropdown(
                            options = filterOptions,
                            selectedOption = selectedFilter,
                            onOptionSelected = { selectedFilter = it }
                        )
                    }
                )
                TrendingProjects(projects = sampleProjects, navController = navController, email = email)
            }
            
            // Skilled Collaborators
            item {
                SectionHeader(
                    title = "Skilled Collaborators", 
                    action = { Text("View All") }
                )
                SkilledCollaborators(collaborators = sampleCollaborators)
            }
            
            // Upcoming Events
            item {
                SectionHeader(
                    title = "Upcoming Events", 
                    action = { Text("Calendar") }
                )
                UpcomingEvents(events = sampleEvents)
            }
        }
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by remember { mutableStateOf(false) }
    
    androidx.compose.material3.SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { onSearch(query) },
        active = active,
        onActiveChange = { newActive -> active = newActive },
        placeholder = { Text("Search skills, projects, or people...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (active) {
                IconButton(onClick = { if (query.isNotEmpty()) onQueryChange("") }) {
                    Icon(Icons.Default.Search, "Clear")
                }
            }
        },
        modifier = modifier
    ) {
        // Search suggestions
    }
}

@Composable
fun SectionHeader(title: String, action: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        action()
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun TrendingProjects(projects: List<Project>, navController: NavController, email: String?) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(projects) { project ->
            ProjectCard(
                project = project,
                navController = navController,
                email = email
            )
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    navController: NavController,
    email: String?
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable {
                val encodedEmail = URLEncoder.encode(email ?: "", "UTF-8")
                navController.navigate("projects/$encodedEmail") {
                    launchSingleTop = true
                    popUpTo("home") { 
                        saveState = true
                        inclusive = false
                    }
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if(project.isTrending) {
                TrendingBadge()
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(project.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(project.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Skills: ${project.skillsRequired.joinToString()}", 
                 style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Team: ${project.membersJoined}/${project.teamSize}", 
                 style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Views: ${project.views} • Likes: ${project.likes}",
                 style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun TrendingBadge() {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "🔥 Trending",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SkilledCollaborators(collaborators: List<Collaborator>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(collaborators) { collaborator ->
            CollaboratorCard(collaborator = collaborator)
        }
    }
}

@Composable
fun CollaboratorCard(collaborator: Collaborator) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile image placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.LightGray, CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = collaborator.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = collaborator.experience,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(collaborator.skills) { skill ->
                    SkillChip(
                        skill = skill,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Projects: ${collaborator.projectsCompleted}")
                Button(
                    onClick = { /* Handle connect */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Connect")
                }
            }
        }
    }
}

@Composable
fun UpcomingEvents(events: List<Event>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(events) { event ->
            EventCard(event = event)
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${event.date} • ${event.location}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { /* Handle registration */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Register Now")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { newExpanded -> expanded = newExpanded }
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    TechStackTheme {
        HomeScreen(
            navController = rememberNavController(),
            email = "user@example.com"
        )
    }
} 