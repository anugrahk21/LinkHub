@file:OptIn(ExperimentalMaterial3Api::class)

package com.demo.linkhub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.width
import com.demo.linkhub.components.BottomNavigationBar
import com.demo.linkhub.models.Project
import com.demo.linkhub.components.SkillChip
import com.demo.linkhub.models.sampleProjects
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import java.net.URLEncoder

@Composable
fun ProjectsScreen(navController: NavController, email: String?) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedSkill by rememberSaveable { mutableStateOf("All Skills") }
    var sortBy by rememberSaveable { mutableStateOf("Newest") }
    val filterOptions = listOf("Newest", "Trending", "Deadline", "Team Size")
    
    val filteredProjects = sampleProjects.filter { project: Project ->
        (project.title.contains(searchQuery, true) ||
         project.description.contains(searchQuery, true) ||
         project.skillsRequired.any { skill -> 
             skill.contains(searchQuery, true) 
         }) &&
        (selectedSkill == "All Skills" || project.skillsRequired.contains(selectedSkill))
    }.let { projects ->
        when(sortBy) {
            "Trending" -> projects.filter { it.isTrending }
            "Deadline" -> projects.sortedBy { it.deadline ?: Long.MAX_VALUE }
            "Team Size" -> projects.sortedBy { project: Project -> project.teamSize }
            else -> projects.sortedByDescending { project: Project -> project.id.toLong() }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tech Projects", style = MaterialTheme.typography.headlineSmall)
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController, email = email) }
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
                ProjectSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.weight(2f)
                )
                
                SortFilterDropdown(
                    selected = sortBy,
                    options = filterOptions,
                    onSelected = { sortBy = it }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Projects Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProjects) { project ->
                    ProjectCard(
                        project = project,
                        onViewDetails = {
                            navController.navigate("projectDetails/${project.id}/${URLEncoder.encode(email, "UTF-8")}") {
                                launchSingleTop = true
                                popUpTo("home") {
                                    saveState = true
                                    inclusive = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(project.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Team: ${project.membersJoined}/${project.teamSize}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onViewDetails,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Details")
            }
        }
    }
}

@Composable
fun ProjectDetailsScreen(projectId: String, navController: NavController, email: String?) {
    val project = sampleProjects.first { it.id == projectId }
    
    var showContactDialog by remember { mutableStateOf(false) }
    var messageContent by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, email) },
        topBar = {
            TopAppBar(
                title = { Text(project.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Project Details", style = MaterialTheme.typography.headlineMedium)
                    
                    Text(project.description, style = MaterialTheme.typography.bodyLarge)
                    
                    // Skills
                    Text("Required Skills:", style = MaterialTheme.typography.titleMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(project.skillsRequired) { skill ->
                            SkillChip(skill = skill)
                        }
                    }
                    
                    // Team Info
                    Text("Team Information", style = MaterialTheme.typography.titleMedium)
                    TeamInfo(membersJoined = project.membersJoined, teamSize = project.teamSize)
                    
                    // Project Owner
                    Text("Project Owner:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "${project.owner}\n" +
                               "Email: ${project.ownerEmail}\n" +
                               "Contact Methods: ${project.contactMethods.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { /* Handle join */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(if (project.membersJoined < project.teamSize) "Request Join" else "Project Full")
                        }
                        
                        Button(
                            onClick = { showContactDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Text("Contact Owner")
                        }
                    }
                }
            }
            
            // Add contact section
            item {
                Column {
                    Text(
                        "Contact Owner:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    
                    Button(
                        onClick = { showContactDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text("Send Message to Owner")
                    }

                    // Add contact dialog
                    if (showContactDialog) {
                        AlertDialog(
                            onDismissRequest = { showContactDialog = false },
                            title = { Text("Contact Project Owner") },
                            text = {
                                Column {
                                    Text("Project: ${project.title}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = messageContent,
                                        onValueChange = { messageContent = it },
                                        label = { Text("Your message") },
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 4
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (messageContent.isNotBlank()) {
                                            // Handle actual message sending
                                            showContactDialog = false
                                        }
                                    },
                                    enabled = messageContent.isNotBlank()
                                ) {
                                    Text("Send Message")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showContactDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamInfo(membersJoined: Int, teamSize: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Members Joined: $membersJoined")
            Text("Team Size: $teamSize")
        }
    }
}

@Composable
fun SortFilterDropdown(
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .width(120.dp)
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        modifier = modifier
    )
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun ProjectsPreview() {
    TechStackTheme {
        ProjectsScreen(
            navController = rememberNavController(),
            email = "user@example.com"
        )
    }
}

// Add to MainActivity.kt navigation:
// composable("projects") { ProjectsScreen(navController = navController) }
// composable("projectDetails/{projectId}") { backStackEntry ->
//     ProjectDetailsScreen(
//         projectId = backStackEntry.arguments?.getString("projectId") ?: "",
//         navController = navController
//     )
// }