package com.example.helloworldapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth // Ensure this import is present

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun SignUpScreen(navController: NavController) {
    var email by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var password by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var confirmPassword by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var errorMessage by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    var isLoading by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text("Create Account", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { androidx.compose.material3.Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material3.Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { androidx.compose.material3.Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator()
        } else {
            androidx.compose.material3.Button(
                onClick = {
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match."
                    } else if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Email and password cannot be empty."
                    } else if (password.length < 6) { // Optional: Add password strength check
                        errorMessage = "Password should be at least 6 characters."
                    }
                    else {
                        errorMessage = null
                        isLoading = true
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    // User created successfully
                                    // Optionally, send verification email:
                                    // task.result?.user?.sendEmailVerification()
                                    println("Account created successfully for: $email")
                                    // Navigate to login screen
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.startDestinationId) { // Clears back stack to the start (login)
                                            inclusive = true
                                        }
                                        launchSingleTop = true // Avoid multiple copies of login screen
                                    }
                                } else {
                                    // If sign up fails, display a message to the user.
                                    errorMessage = task.exception?.message ?: "Sign up failed. Please try again."
                                    println("Account creation failed: ${task.exception?.message}")
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.material3.Text("Sign Up")
            }
        }

        errorMessage?.let {
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
        }

        androidx.compose.material3.TextButton(onClick = {
            // Navigate back to login, ensuring it's the only one on the stack
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }) {
            androidx.compose.material3.Text("Already have an account? Login")
        }
    }
}
