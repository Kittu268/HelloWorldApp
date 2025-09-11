package com.example.helloworldapp

import android.content.Context
import android.graphics.Bitmap // Added for Bitmap
import android.graphics.BitmapFactory // Added for BitmapFactory
import android.graphics.Paint
import android.graphics.RectF // Added for RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ResumeViewModel : ViewModel() {

    // --- Personal Details (from ResumeStep1Screen) ---
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var linkedInUrl by mutableStateOf("")
    var portfolioUrl by mutableStateOf("")
    var otherUrl by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null) // For the profile picture

    // --- Work Experience (from ResumeStep2Screen) ---
    var jobTitle by mutableStateOf("")
    var companyName by mutableStateOf("")
    var yearsExperience by mutableStateOf("")

    // --- Education (from ResumeStep3Screen) ---
    var schoolName by mutableStateOf("")
    var degree by mutableStateOf("")
    var fieldOfStudy by mutableStateOf("")
    var graduationYear by mutableStateOf("")

    // Function to handle PDF download logic
    fun downloadResumeAsPdf(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val titlePaint = TextPaint().apply {
                color = android.graphics.Color.BLACK
                textSize = 24f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            val textPaint = TextPaint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = 12f
            }

            var yPosition = 60f // Starting Y position
            val xMargin = 40f
            val pageWidth = canvas.width
            val lineSpacing = 20f
            val sectionSpacing = 40f

            // --- Try to load and draw the profile image ---
            var imageBitmap: Bitmap? = null
            imageUri?.let { uri ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        imageBitmap = BitmapFactory.decodeStream(inputStream)
                    }
                } catch (e: IOException) {
                    Log.e("ResumePDF", "Error loading image", e)
                    // Optionally show a toast or log that image loading failed
                }
            }

            var imageDrawnAdjustment = 0f
            imageBitmap?.let { bmp ->
                val imageSize = 120f // Desired size for the image (square)
                val imageTopMargin = yPosition // Use current yPosition as top for the image
                val imageStartMargin = 40f // Same as xMargin for text
                val imageRect = RectF(
                    pageWidth - imageSize - imageStartMargin, // left (align to right)
                    imageTopMargin,                       // top
                    pageWidth - imageStartMargin,         // right
                    imageTopMargin + imageSize            // bottom
                )
                canvas.drawBitmap(bmp, null, imageRect, null)
                // Adjust yPosition to be below the image only if text would overlap
                // For a right-aligned image, the main text flow might not need global adjustment
                // but we might want to ensure the title (fullName) is not too high if image is very tall
                imageDrawnAdjustment = imageSize + lineSpacing // Space occupied by image + a line
            }

            // --- Draw Content ---
            val initialTextYPosition = yPosition
            var titleIsBelowImage = false

            // Full Name (adjust if image is present and tall enough to interfere)
            if (fullName.isNotBlank()) {
                var nameYPos = yPosition
                if (imageDrawnAdjustment > 0 && nameYPos < initialTextYPosition + imageDrawnAdjustment && (pageWidth / 2f) > (pageWidth - 120f - 40f - 20f) ) {
                    // If name is centered and might overlap with right-aligned image, push it down
                    // This condition is a heuristic, might need refinement
                    // A simpler approach might be to always ensure yPosition starts below image height if image is present
                     if (yPosition < initialTextYPosition + imageDrawnAdjustment) {
                         yPosition = initialTextYPosition + imageDrawnAdjustment
                         nameYPos = yPosition
                         titleIsBelowImage = true
                     }
                } else if (imageDrawnAdjustment == 0f) {
                     // No image, normal flow
                } else {
                    // Image is there but name is not overlapping (e.g. name is left aligned)
                }

                canvas.drawText(fullName, pageWidth / 2f, nameYPos, titlePaint)
                yPosition = nameYPos + sectionSpacing
            }

            // If title was pushed below image, ensure subsequent yPosition respects that
            if (titleIsBelowImage && yPosition < initialTextYPosition + imageDrawnAdjustment + sectionSpacing) {
                yPosition = initialTextYPosition + imageDrawnAdjustment + sectionSpacing / 2 // A bit less spacing if image already made a gap
            } else if (imageDrawnAdjustment > 0 && yPosition < initialTextYPosition + imageDrawnAdjustment) {
                 // General case if no title, but other text starts, ensure it's below image
                 yPosition = initialTextYPosition + imageDrawnAdjustment
            }


            // Contact Info
            var contactInfoDrawn = false
            if (email.isNotBlank()) {
                canvas.drawText("Email: $email", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                contactInfoDrawn = true
            }
            if (phone.isNotBlank()) {
                canvas.drawText("Phone: $phone", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                contactInfoDrawn = true
            }
            if (linkedInUrl.isNotBlank()) {
                canvas.drawText("LinkedIn: $linkedInUrl", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                contactInfoDrawn = true
            }
            if (portfolioUrl.isNotBlank()) {
                canvas.drawText("Portfolio: $portfolioUrl", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                contactInfoDrawn = true
            }
            if (otherUrl.isNotBlank()) {
                canvas.drawText("Other: $otherUrl", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                contactInfoDrawn = true
            }

            if (contactInfoDrawn) {
                yPosition += (sectionSpacing - lineSpacing)
            }

            // Work Experience
            if (jobTitle.isNotBlank() || companyName.isNotBlank() || yearsExperience.isNotBlank()) {
                canvas.drawText("--- Work Experience ---", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                if (jobTitle.isNotBlank()) { 
                    var workExperienceText = "Job: $jobTitle"
                    if (companyName.isNotBlank()) workExperienceText += " at $companyName"
                    if (yearsExperience.isNotBlank()) workExperienceText += " ($yearsExperience years)"
                    canvas.drawText(workExperienceText, xMargin, yPosition, textPaint)
                    yPosition += lineSpacing
                }
                yPosition += (sectionSpacing - lineSpacing)
            }

            // Education
            if (schoolName.isNotBlank() || degree.isNotBlank() || fieldOfStudy.isNotBlank() || graduationYear.isNotBlank()) {
                canvas.drawText("--- Education ---", xMargin, yPosition, textPaint)
                yPosition += lineSpacing
                if (schoolName.isNotBlank() || degree.isNotBlank()) { 
                    var educationText = ""
                    if (degree.isNotBlank()) educationText += "$degree "
                    if (fieldOfStudy.isNotBlank()) educationText += "in $fieldOfStudy "
                    if (schoolName.isNotBlank()) educationText += "from $schoolName "
                    if (graduationYear.isNotBlank()) educationText += "($graduationYear)"
                    canvas.drawText(educationText.trim(), xMargin, yPosition, textPaint)
                    yPosition += lineSpacing
                }
                yPosition += (sectionSpacing - lineSpacing)
            }

            pdfDocument.finishPage(page)

            val fileName = "Resume_${fullName.replace(" ", "_")}.pdf"
            val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

            if (documentsDir == null) {
                Log.e("ResumePDF", "Failed to get documents directory.")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: Cannot access storage.", Toast.LENGTH_LONG).show()
                }
                pdfDocument.close()
                return@launch
            }
            val file = File(documentsDir, fileName)

            try {
                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Log.i("ResumePDF", "PDF saved successfully to ${file.absolutePath}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "PDF saved to ${file.name} in Documents", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Log.e("ResumePDF", "Error writing PDF", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                pdfDocument.close() 
            }
        }
    }

    fun clearAllData() {
        fullName = ""
        email = ""
        phone = ""
        linkedInUrl = ""
        portfolioUrl = ""
        otherUrl = ""
        imageUri = null
        jobTitle = ""
        companyName = ""
        yearsExperience = ""
        schoolName = ""
        degree = ""
        fieldOfStudy = ""
        graduationYear = ""
    }
}
