package com.demo.linkhub

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

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.background
import androidx.navigation.compose.rememberNavController
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.ExperimentalFoundationApi
import com.demo.linkhub.components.SkillChip

data class User(
    val id: String,
    val name: String,
    val skills: List<String>,
    val availability: String,
    val currentProject: String,
    val experience: String
)

val sampleUsers = listOf(
    User(
        "1",
        "Alex Chen",
        listOf("Android", "Kotlin", "Jetpack Compose"),
        "Available",
        "AI Chatbot Development",
        "5+ years"
    ),
    User(
        "2",
        "Maria Gonzalez",
        listOf("UI/UX", "Figma", "Prototyping"),
        "Busy",
        "Fitness App Redesign",
        "3 years"
    ),
    User(
        "3",
        "James Smith",
        listOf("Flutter", "Dart", "Firebase"),
        "Available",
        "E-commerce Platform",
        "2 years"
    ),
    User(
        "4",
        "Sarah Johnson",
        listOf("Go", "Rust", "Microservices"),
        "Busy",
        "High-frequency Trading System",
        "7 years"
    ),
    User(
        "5",
        "Michael Chen",
        listOf("Python", "TensorFlow", "PyTorch"),
        "Available",
        "Predictive Analytics Engine",
        "4 years"
    ),
    User(
        "6",
        "Emma Wilson",
        listOf("AWS", "Docker", "Kubernetes"),
        "Available",
        "CI/CD Pipeline Optimization",
        "3 years"
    ),
    User(
        "7",
        "David Kim",
        listOf("React", "TypeScript", "Node.js"),
        "Busy",
        "Real-time Collaboration Tool",
        "5 years"
    ),
    User(
        "8",
        "Priya Sharma",
        listOf("Swift", "iOS", "ARKit"),
        "Available",
        "AR Navigation App",
        "2 years"
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExplorePeopleScreen(navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedSkill by rememberSaveable { mutableStateOf("All Skills") }
    val skillsFilterOptions = listOf("All Skills") + listOf("Android", "Kotlin", "UI/UX", "Flutter", "AI/ML")
    
    val filteredUsers = sampleUsers.filter { user ->
        (user.name.contains(searchQuery, true) ||
         user.skills.any { it.contains(searchQuery, true) }) &&
        (selectedSkill == "All Skills" || user.skills.contains(selectedSkill))
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Explore Collaborators", style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search and Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.weight(2f)
                )
                
                SkillFilterDropdown(
                    selectedSkill = selectedSkill,
                    options = skillsFilterOptions,
                    onSkillSelected = { selectedSkill = it }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // User Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredUsers) { user ->
                    UserCard(user = user) {
                        navController.navigate("profile/${user.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onViewProfile: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .padding(8.dp)
                )
                
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(user.name, style = MaterialTheme.typography.titleMedium)
                    AvailabilityBadge(availability = user.availability)
                }
            }
            
            // Skills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(user.skills) { skill ->
                    SkillChip(skill = skill)
                }
            }
            
            // Project Info
            Column {
                Text("Current Project:", style = MaterialTheme.typography.labelSmall)
                Text(
                    user.currentProject,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Action Button
            ElevatedButton(
                onClick = onViewProfile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(if (user.availability == "Available") "Connect" else "View Profile")
            }
        }
    }
}

@Composable
fun AvailabilityBadge(availability: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (availability == "Available") Color(0xFF4CAF50) 
                    else MaterialTheme.colorScheme.error
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            availability,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillFilterDropdown(
    selectedSkill: String,
    options: List<String>,
    onSkillSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedSkill,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .width(150.dp)
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { skill ->
                DropdownMenuItem(
                    text = { Text(skill) },
                    onClick = {
                        onSkillSelected(skill)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExplorePeoplePreview() {
    TechStackTheme {
        ExplorePeopleScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by remember { mutableStateOf(false) }
    
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        modifier = modifier,
        placeholder = { Text("Search collaborators...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
    ) {
        // Search suggestions
    }
} 