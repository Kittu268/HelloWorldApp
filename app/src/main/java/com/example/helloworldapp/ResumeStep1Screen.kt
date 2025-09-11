package com.example.helloworldapp

import android.net.Uri // Added for image URI
import androidx.activity.compose.rememberLauncherForActivityResult // Added for image picker
import androidx.activity.result.contract.ActivityResultContracts // Added for image picker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helloworldapp.ResumeViewModel // Import the ViewModel
// import androidx.compose.foundation.Image // Will be needed later for actual image display
// import coil.compose.rememberAsyncImagePainter // Will be needed for image display

@Composable
fun ResumeStep1Screen(
    navController: NavController,
    resumeViewModel: ResumeViewModel // Added ViewModel parameter
) {
    // Local states are now removed, will use resumeViewModel directly

    // ActivityResultLauncher for picking an image from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        resumeViewModel.imageUri = uri // Update the ViewModel with the selected image URI
    }

    // Button enabled state now reads from ViewModel
    val isButtonEnabled = resumeViewModel.fullName.isNotBlank() &&
            resumeViewModel.email.isNotBlank() &&
            resumeViewModel.phone.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Consistent spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resume Building - Step 1: Personal Details")

        // Button to select profile picture
        Button(onClick = {
            // Launch the image picker, allowing any image type
            imagePickerLauncher.launch("image/*")
        }) {
            Text("Select Profile Picture (Optional)")
        }

        // Display selected image URI (or a message if none is selected)
        // Reads from ViewModel
        resumeViewModel.imageUri?.let {
            Text("Selected Image URI: $it")
        } ?: Text("No profile picture selected.")

        TextField(
            value = resumeViewModel.fullName, // Read from ViewModel
            onValueChange = { resumeViewModel.fullName = it }, // Write to ViewModel
            label = { Text("Full Name (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.email, // Read from ViewModel
            onValueChange = { resumeViewModel.email = it }, // Write to ViewModel
            label = { Text("Email Address (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.phone, // Read from ViewModel
            onValueChange = { resumeViewModel.phone = it }, // Write to ViewModel
            label = { Text("Phone Number (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.linkedInUrl, // Read from ViewModel
            onValueChange = { resumeViewModel.linkedInUrl = it }, // Write to ViewModel
            label = { Text("LinkedIn Profile URL (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.portfolioUrl, // Read from ViewModel
            onValueChange = { resumeViewModel.portfolioUrl = it }, // Write to ViewModel
            label = { Text("Portfolio/Website URL (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.otherUrl, // Read from ViewModel
            onValueChange = { resumeViewModel.otherUrl = it }, // Write to ViewModel
            label = { Text("Other Link URL (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f)) // Pushes the Next button to the bottom

        Button(
            onClick = {
                // Data is already in ViewModel, just navigate
                navController.navigate("resumeStep2")
            },
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next Step")
        }
    }
}
