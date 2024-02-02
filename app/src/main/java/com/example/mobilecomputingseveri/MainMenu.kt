package com.example.mobilecomputingseveri
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import com.example.mobilecomputingseveri.R

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilecomputingseveri.ui.theme.MobileComputingSeveriTheme
import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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

        }

    }
}

@Composable
fun Profile(context: Context) {
    var imageFileName by remember { mutableStateOf(loadImageFileName(context)) }
    var desc by remember { mutableStateOf("desc") }

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
            Text(text = "Sexy and I know it")
        }
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













