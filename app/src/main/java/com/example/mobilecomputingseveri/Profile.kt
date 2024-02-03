package com.example.mobilecomputingseveri


import android.content.Context

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme


import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.material.Text

import androidx.compose.material.TextField

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter

import java.io.File



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Profile")
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("LogIn") {
                    popUpTo("LogIn") {
                        inclusive = true
                    }
                }
            }
            ){
                Text("LogOut", fontStyle = FontStyle.Italic, fontSize = 25.sp)
            }


        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            Profile(context)
            Button(onClick = {
                navController.navigate("MainMenu"){
                    popUpTo("MainMenu"){
                        inclusive = true
                    }
                }
            }
            ){
                Text(text = "Back")
            }


        }

    }
}

@Composable
fun Profile(context: Context) {
    var imageFileName by remember { mutableStateOf(loadImageFileName(context)) }
    var desc by remember { mutableStateOf("desc") }
    var userName by remember { mutableStateOf("Severi") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Save the image with the generated unique filename
                val uniqueFileName = generateUniqueFileName(uri)
                saveImageUri(context, uri, uniqueFileName)

                // Save the URI to SharedPreferences
                saveImageUriToPrefs(context, uniqueFileName)

                // Update the imageFileName to use the generated filename
                imageFileName = uniqueFileName
            }
        }
    )

    Row() {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp) // Add padding to vertically center the text
                .fillMaxWidth()
        ) {
            Text(
                text = "Profile Picture",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        }

        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray) // Set the background color here
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 100.dp)
            ){
            Image(
                painter = rememberAsyncImagePainter(File(context.filesDir, imageFileName)),
                contentDescription = desc,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        }
    }

        Spacer(modifier = Modifier.width(8.dp))


    Column {
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username", fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
fun generateUniqueFileName(uri: Uri): String {
    val timeStamp = System.currentTimeMillis()
    return "${uri.lastPathSegment}_$timeStamp"
}

fun saveImageUri(context: Context, uri: Uri, fileName: String) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}

fun saveImageUriToPrefs(context: Context, fileName: String) {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString("imageUri", fileName)
    editor.apply()
}

fun loadImageFileName(context: Context): String {
    val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return prefs.getString("imageUri", "image") ?: "image"
}