package com.demo.linkhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.linkhub.ui.theme.TechStackTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechStackTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") { 
                        LoginScreen(
                            navController = navController,
                            onLoginSuccess = {}
                        ) 
                    }
                    composable("home/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email")
                        HomeScreen(
                            navController = navController,
                            email = email
                        )
                    }
                    composable("postProject") {
                        PostProjectScreen(navController = navController)
                    }
                    composable("explorePeople") {
                        ExplorePeopleScreen(navController = navController)
                    }
                    composable("projects/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email")
                        ProjectsScreen(
                            navController = navController,
                            email = email
                        )
                    }
                    composable(
                        "projectDetails/{projectId}/{email}",
                        arguments = listOf(
                            navArgument("projectId") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        ProjectDetailsScreen(
                            projectId = backStackEntry.arguments?.getString("projectId") ?: "",
                            navController = navController,
                            email = backStackEntry.arguments?.getString("email")
                        )
                    }
                    composable("profile/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email")
                        ProfileScreen(
                            navController = navController,
                            email = email
                        )
                    }
                    composable("notifications/{email}") { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email")
                        NotificationsScreen(
                            navController = navController,
                            email = email
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: (String) -> Unit = {}) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isEmailValid = email.contains("@") && email.length > 5
    val isPasswordValid = password.length >= 6

    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tagline
    Text(
            text = "Connect. Collaborate. Innovate.",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Tech Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = email.isNotEmpty() && !isEmailValid
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = password.isNotEmpty() && !isPasswordValid
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = { onLoginSuccess(email) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEmailValid && isPasswordValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Login to TechConnect", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Help Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /* Forgot Password */ }) {
                Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
            }
            TextButton(onClick = { /* Create Account */ }) {
                Text("Create Account", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    TechStackTheme {
        LoginScreen()
    }
}