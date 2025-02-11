package com.demo.linkhub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostProjectScreen(navController: NavController) {
    var projectTitle by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var selectedSkills by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var deadline by rememberSaveable { mutableStateOf<Long?>(null) }
    var teamSize by rememberSaveable { mutableStateOf("") }
    var showPreview by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccess by remember { mutableStateOf(false) }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                Text("Post New Project", style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = projectTitle,
                onValueChange = { projectTitle = it },
                label = { Text("Project Title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = projectTitle.isEmpty()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                isError = description.isEmpty()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Skills Selector
            TagInput(
                selectedTags = selectedSkills,
                onTagAdded = { selectedSkills = selectedSkills + it },
                onTagRemoved = { selectedSkills = selectedSkills - it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Deadline Picker
            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = deadline?.let { formatDate(it) } ?: "Select Deadline",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text("Deadline") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Team Size Selector
            TeamSizeDropdown(
                selectedSize = teamSize,
                onSizeSelected = { teamSize = it }
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { showPreview = true },
                    enabled = projectTitle.isNotEmpty() && description.isNotEmpty()
                ) {
                    Text("Preview")
                }
                
                Button(
                    onClick = {
                        if (validateForm()) {
                            navController.popBackStack()
                            showSuccess = true
                        }
                    },
                    enabled = projectTitle.isNotEmpty() && description.isNotEmpty()
                ) {
                    Text("Submit Project")
                }
            }
        }
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            snackbarHostState.showSnackbar("Project posted successfully!")
            showSuccess = false
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        deadline = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }
                ) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showPreview) {
        PreviewDialog(
            projectTitle = projectTitle,
            description = description,
            skills = selectedSkills,
            deadline = deadline,
            teamSize = teamSize,
            onDismiss = { showPreview = false },
            onConfirm = {
                showPreview = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
private fun TagInput(
    selectedTags: List<String>,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit
) {
    var newTag by remember { mutableStateOf("") }
    
    Column {
        OutlinedTextField(
            value = newTag,
            onValueChange = { newTag = it },
            label = { Text("Required Skills *") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (newTag.isNotEmpty()) {
                    IconButton(onClick = {
                        if (newTag.isNotBlank()) {
                            onTagAdded(newTag)
                            newTag = ""
                        }
                    }) {
                        Text("Add", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        )
        
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedTags) { tag ->
                SkillChip(tag = tag, onRemove = { onTagRemoved(tag) })
            }
        }
    }
}

@Composable
private fun SkillChip(tag: String, onRemove: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(tag, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onRemove) {
                Text("×", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamSizeDropdown(selectedSize: String, onSizeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val teamSizes = listOf("1-3", "3-5", "5-10", "10+")
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedSize,
            onValueChange = {},
            readOnly = true,
            label = { Text("Team Size") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            teamSizes.forEach { size ->
                DropdownMenuItem(
                    text = { Text(size) },
                    onClick = {
                        onSizeSelected(size)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PreviewDialog(
    projectTitle: String,
    description: String,
    skills: List<String>,
    deadline: Long?,
    teamSize: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Project Preview",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text("Title: $projectTitle", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Description: $description", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Skills: ${skills.joinToString()}", style = MaterialTheme.typography.bodyMedium)
                deadline?.let {
                    Text("Deadline: ${formatDate(it)}", style = MaterialTheme.typography.bodyMedium)
                }
                Text("Team Size: $teamSize", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Edit", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
}

private fun validateForm(): Boolean {
    // Add more complex validation if needed
    return true
}

// Add to MainActivity navigation
// composable("postProject") { PostProjectScreen(navController) } 