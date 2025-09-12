package com.example.helloworldapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
// Removed NavGraph.Companion.findStartDestination as it's not used for PDF download button
import com.example.helloworldapp.ResumeViewModel

@Composable
fun ResumeStep3Screen(
    navController: NavController,
    resumeViewModel: ResumeViewModel
) {
    val context = LocalContext.current // Get the current context

    val isButtonEnabled = resumeViewModel.schoolName.isNotBlank() &&
            resumeViewModel.degree.isNotBlank() &&
            resumeViewModel.fieldOfStudy.isNotBlank() &&
            resumeViewModel.graduationYear.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resume Building - Step 3: Education")

        TextField(
            value = resumeViewModel.schoolName,
            onValueChange = { resumeViewModel.schoolName = it },
            label = { Text("School/University Name (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.degree,
            onValueChange = { resumeViewModel.degree = it },
            label = { Text("Degree (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.fieldOfStudy,
            onValueChange = { resumeViewModel.fieldOfStudy = it },
            label = { Text("Field of Study (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = resumeViewModel.graduationYear,
            onValueChange = { resumeViewModel.graduationYear = it },
            label = { Text("Graduation Year (Required)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Previous Step")
            }

            Button(
                onClick = {
                    // Call the ViewModel function to handle PDF download
                    resumeViewModel.downloadResumeAsPdf(context)
                    // Optionally, navigate somewhere or show a confirmation after attempting download
                    // For example, to go back to the dashboard:
                    // navController.navigate("dashboard") {
                    //     popUpTo(navController.graph.findStartDestination().id) {
                    //         inclusive = true
                    //     }
                    //     launchSingleTop = true
                    // }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Download as PDF")
            }
        }
    }
}
