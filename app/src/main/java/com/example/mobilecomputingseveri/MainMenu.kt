package com.example.mobilecomputingseveri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun MainMenu(navController: NavController) {

    Column {
        Row {
            Spacer(modifier = Modifier.width(100.dp))
            Image(
                painter = painterResource(R.drawable.ste_mariacorrection),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)

            )
        }
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
            FloatingActionButton(onClick = {
                navController.navigate("LogIn") {
                    popUpTo("LogIn"){
                        inclusive = true
                    }
                }
            }
            ) {
                Text("LogOut", fontStyle = FontStyle.Italic, fontSize = 25.sp)
            }
        }

}






