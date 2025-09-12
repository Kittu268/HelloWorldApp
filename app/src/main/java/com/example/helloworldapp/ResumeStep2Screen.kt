package com.example.helloworldapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
// Removed unused imports: getValue, mutableStateOf, remember, setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helloworldapp.ResumeViewModel // Import the ViewModel

@Composable
fun ResumeStep2Screen(
    navController: NavController,
    resumeViewModel: ResumeViewModel // Added ViewModel parameter
) {
    // Local states are now removed, will use resumeViewModel directly

    // Button enabled state now reads from ViewModel
    val isButtonEnabled = resumeViewModel.jobTitle.isNotBlank() &&
            resumeViewModel.companyName.isNotBlank() &&
            resumeViewModel.yearsExperience.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resume Building - Step 2: Work Experience")

        TextField(
            value = resumeViewModel.jobTitle, // Read from ViewModel
            onValueChange = { resumeViewModel.jobTitle = it }, // Write to ViewModel
            label = { Text("Job Title (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.companyName, // Read from ViewModel
            onValueChange = { resumeViewModel.companyName = it }, // Write to ViewModel
            label = { Text("Company Name (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.yearsExperience, // Read from ViewModel
            onValueChange = { resumeViewModel.yearsExperience = it }, // Write to ViewModel
            label = { Text("Years of Experience (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f).padding(end = 8.dp) // Added padding
            ) {
                Text("Previous Step")
            }

            Button(
                onClick = { navController.navigate("resumeStep3") },
                enabled = isButtonEnabled,
                modifier = Modifier.weight(1f).padding(start = 8.dp) // Added padding
            ) {
                Text("Next Step")
            }
        }
    }
}
