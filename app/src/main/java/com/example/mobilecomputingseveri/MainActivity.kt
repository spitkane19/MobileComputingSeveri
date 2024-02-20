package com.example.mobilecomputingseveri

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button


import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.DisposableEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, Lightsensor::class.java))
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "LogIn") {
                composable("LogIn") {
                    UserBox(navController, profileViewModel, applicationContext)

                }
                composable("MainMenu") {
                    MainMenu(navController, profileViewModel)
                }
                composable("ProfilePage"){
                    ProfilePage(navController, profileViewModel)
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun UserBox(navController: NavController, profileViewModel: ProfileViewModel, context: Context) {
    var text by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )
    val fullScreenIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check if the permission is granted after returning from settings
        val canUseFullScreenIntent =
            NotificationManagerCompat.from(context).canUseFullScreenIntent()
    }
    Row{
        Spacer(modifier = Modifier.width(60.dp))
        Column {
            Row {
                Spacer(modifier = Modifier.width(40.dp))
                Image(
                    painter = painterResource(R.drawable.ste_mariacorrection),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)

                )
            }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {Text("Username")}
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {Text("Enter password")}
            )
            Button(onClick = {
                navController.navigate("MainMenu"){
                    popUpTo("MainMenu"){
                        inclusive = true
                    }
                }
            }
            ){
                Text(text = "Log In")
            }
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    launcher.launch(Manifest.permission.USE_FULL_SCREEN_INTENT)
                }
            }) {
                Text(text = "Request permission")
            }
                    
                }
            }



}


