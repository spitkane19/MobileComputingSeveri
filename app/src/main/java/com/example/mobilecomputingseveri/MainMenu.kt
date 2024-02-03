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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme


import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.material.Text

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors

import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter

import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Menu")
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
            Chatroom(context)
            Text("Ryhmän jäsenlista:", fontSize = 40.sp)
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .size(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val names =
                    listOf("Severi", "Tuomas", "Benjamin", "Roope", "Pentsa", "Matti", "Pekka", "Teppo")

                for (name in names) {
                    Text(name, fontSize = 80.sp)
                }
            }
            Button(onClick = {
                navController.navigate("Profile"){
                    popUpTo("MainMenu"){
                        inclusive = true
                    }
                }
            }
            ){
                Text(text = "Profile")
            }

        }

    }
}

@Composable
fun Chatroom(context: Context) {
    var imageFileName by remember { mutableStateOf(loadImageFileName(context)) }
    val desc by remember { mutableStateOf("desc") }

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

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(File(context.filesDir, imageFileName)),
            contentDescription = desc,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = "Severi", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Message123")
        }
    }
}





//* TODO:  @Composable for choosing photos, Room database to save the data
//fun PickingPhotos() {
//    var PhotoUri by remember {
//        mutableStateOf<String?>(null)
//    }
//    val context = LocalContext.current
//    val defaultProfilePic = R.drawable.ste_mariacorrection
//
//    val PhotoPickLauncher = rememberLauncher {}
//}













