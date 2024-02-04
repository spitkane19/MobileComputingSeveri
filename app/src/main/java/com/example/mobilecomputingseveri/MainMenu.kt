package com.example.mobilecomputingseveri

import android.content.Context


import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState

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
import androidx.compose.runtime.DisposableEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(navController: NavController, profileViewModel: ProfileViewModel) {
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
            Chatroom(context, profileViewModel)
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
                navController.navigate("ProfilePage"){
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
fun Chatroom(context: Context, profileViewModel: ProfileViewModel) {
    val usernameData: String by profileViewModel.usernameData.observeAsState("")
    DisposableEffect(usernameData) {

        profileViewModel.getUsernameByProfileId(1)

        onDispose {
            // Cleanup if needed
        }
    }

    Row(modifier = Modifier.padding(all = 8.dp)) {
        loadImageFileName(context, profileViewModel)
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = usernameData ?: "DefaultUsername",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Message123")
        }
    }
}
