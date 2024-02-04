package com.example.mobilecomputingseveri
import com.example.mobilecomputingseveri.ProfileViewModel

import android.content.Context

import android.net.Uri
import android.util.Log

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


import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter

import java.io.File



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavController, profileViewModel: ProfileViewModel) {
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





    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            Profile(context, profileViewModel)
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
fun Profile(context: Context, profileViewModel: ProfileViewModel) {



    Row() {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
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
                .background(color = Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 100.dp)
            ){

                loadImageFileName(context,profileViewModel)

        }

    }

        Spacer(modifier = Modifier.width(8.dp))

        loadUsername(context,profileViewModel)






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
fun saveImageToDatabase(context: Context, fileName: String, profileViewModel: ProfileViewModel){
    profileViewModel.updatePictureData(1, fileName)
}
fun saveUsernameToDatabase(context: Context, username: String, profileViewModel: ProfileViewModel){
    profileViewModel.updateUserName(1, username)
}


@Composable
fun loadImageFileName(context: Context, profileViewModel: ProfileViewModel) {
    val pictureData: String? by profileViewModel.pictureData.observeAsState()
    var photoPickerResult by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(pictureData, photoPickerResult) {
        profileViewModel.getPictureDataByProfileId(1)

        onDispose {
            // Cleanup if needed
        }
    }

    val desc by remember { mutableStateOf("desc") }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let {
                val uniqueFileName = generateUniqueFileName(uri)
                saveImageUri(context, uri, uniqueFileName)
                saveImageToDatabase(context, uniqueFileName, profileViewModel)
                photoPickerResult = uri
            }
        }
    )

    // Check if pictureData is null or empty
    if (pictureData.isNullOrEmpty()) {

        Spacer(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .clickable {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    } else {
        // Draw the image using the pictureData
        Image(
            painter = rememberAsyncImagePainter(File(context.filesDir, pictureData)),
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
@Composable
fun loadUsername(context: Context, profileViewModel: ProfileViewModel) {
    val usernameData: String by profileViewModel.usernameData.observeAsState("")
    var editableText by remember { mutableStateOf("") }

    DisposableEffect(usernameData) {
        profileViewModel.getUsernameByProfileId(1)


        onDispose {
        }
    }

    Column {
        Text(
            text = usernameData?: "DefaultUsername",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )

        TextField(
            value = editableText,
            onValueChange = {
                editableText = it
            },
            label = { Text("New username", fontWeight = FontWeight.Bold) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                saveUsernameToDatabase(context, editableText, profileViewModel)
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text("Save new name")
        }
    }
}
