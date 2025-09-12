package com.example.helloworldapp
import com.example.helloworldapp.ReportScreen
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text // Kept for ReportScreen or if others are temporarily simple
import androidx.compose.runtime.Composable // Kept for ReportScreen or if others are temporarily simple
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination // Added this import
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helloworldapp.ui.theme.HelloWorldAppTheme
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewmodel.compose.viewModel // Added for ViewModel
import com.google.firebase.auth.FirebaseAuth // Added for Firebase Auth

// Ensure you have these imports if LoginScreen, SignUpScreen, DashboardScreen
// are in the com.example.helloworldapp package (which they are):
// import com.example.helloworldapp.LoginScreen (implicitly imported if in same package)
// import com.example.helloworldapp.SignUpScreen (implicitly imported if in same package)
// import com.example.helloworldapp.DashboardScreen (implicitly imported if in same package)
import com.example.helloworldapp.ResumeStep1Screen // Added import
import com.example.helloworldapp.ResumeStep2Screen // Added import
import com.example.helloworldapp.ResumeStep3Screen // Added import
// import com.example.helloworldapp.ResumeViewModel // Already implicitly available if in same package


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Save internal test report on a background thread
        /*
        lifecycleScope.launch(Dispatchers.IO) {
            val file = File(filesDir, "test-report-dashboard.txt")
            try {
                FileWriter(file).use { writer ->
                    writer.write("✅ Welcome text displayed\n")
                    writer.write("✅ Stat cards displayed\n")
                    writer.write("✅ Recent activity displayed\n")
                }
                Log.d("TestReport", "Internal report saved at: ${file.absolutePath}")
                // Show Toast on the main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Test report saved!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Log.e("TestReport", "Error saving report", e)
                // Optionally, show error Toast on the main thread
                // withContext(Dispatchers.Main) {
                //     Toast.makeText(this@MainActivity, "Error saving report", Toast.LENGTH_SHORT).show()
                // }
            }
        }
        */

        // ✅ Compose UI with navigation
        setContent {
            HelloWorldAppTheme {
                val navController = rememberNavController()
                val resumeViewModel: ResumeViewModel = viewModel() // Create ViewModel instance

                // Determine start destination based on Firebase auth state
                val firebaseAuth = FirebaseAuth.getInstance()
                val startDestination = if (firebaseAuth.currentUser != null) {
                    "dashboard"
                } else {
                    "login"
                }

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(navController = navController) // Uses LoginScreen from LoginScreen.kt
                    }
                    composable("signup") {
                        SignUpScreen(navController = navController) // Uses SignUpScreen from SignUpScreen.kt
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            navController = navController,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true // Avoid multiple copies of login screen
                                }
                            }
                        )
                    }
                    composable("report") {
                        ReportScreen() // Uses ReportScreen defined below (or from its own file if you create one)
                    }
                    composable("resumeStep1") {
                        ResumeStep1Screen(navController = navController, resumeViewModel = resumeViewModel)
                    }
                    composable("resumeStep2") {
                        ResumeStep2Screen(navController = navController, resumeViewModel = resumeViewModel)
                    }
                    composable("resumeStep3") { // Uncommented and active
                        ResumeStep3Screen(navController = navController, resumeViewModel = resumeViewModel)
                    }
                }
            }
        }
    }
}

// Placeholder for your screen composables
// These should ideally be in their own files for better organization,
// but can be here for simplicity if they are small.

// LoginScreen placeholder removed - assuming it's in LoginScreen.kt
// SignUpScreen placeholder removed - assuming it's in SignUpScreen.kt
// DashboardScreen placeholder removed - it's in DashboardScreen.kt
