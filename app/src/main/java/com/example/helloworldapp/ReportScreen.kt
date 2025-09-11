package com.example.helloworldapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen() {
    val context = LocalContext.current
    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "📄 Test Report",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("reportTitle")
        )

        Button(
            onClick = {
                val reportText = "Test Report\n\n✅ Welcome text displayed\n✅ Stat cards displayed\n✅ Recent activity displayed"
                val file = saveReportToExternalFile(context, "report.txt", reportText)
                openReportFile(context, file)
                statusMessage = "Report saved and opened."
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("saveAndOpenButton")
        ) {
            Text("💾 Save & Open Report")
        }

        Button(
            onClick = {
                val file = File(context.getExternalFilesDir(null), "report.txt")
                if (file.exists()) {
                    shareReportFile(context, file)
                    statusMessage = "Sharing report..."
                } else {
                    statusMessage = "Report file not found."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("shareReportButton")
        ) {
            Text("📤 Share Report")
        }

        if (statusMessage.isNotBlank()) {
            Text(
                text = statusMessage,
                modifier = Modifier.testTag("statusMessage")
            )
        }
    }
}

// Utility functions
fun saveReportToExternalFile(context: Context, fileName: String, content: String): File {
    val file = File(context.getExternalFilesDir(null), fileName)
    FileWriter(file).use { it.write(content) }
    return file
}

fun openReportFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "text/plain")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    context.startActivity(Intent.createChooser(intent, "Open Report With"))
}

fun shareReportFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
}
