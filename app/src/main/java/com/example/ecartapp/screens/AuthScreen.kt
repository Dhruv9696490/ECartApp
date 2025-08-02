package com.example.ecartapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecartapp.ECartViewModel
import com.example.ecartapp.R

@Composable
fun AuthScreen(modifier: Modifier, navController: NavHostController, viewModel: ECartViewModel){
    Column(Modifier.fillMaxSize().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(id = R.drawable.sho2),null,
            Modifier.size(500.dp,300.dp))
        Text("Start your shopping journey now",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ))
        Text("Best shopping app with budget friendly",
            color = Color.DarkGray)
        Spacer(modifier= Modifier.height(8.dp))
        Button(onClick = {
            navController.navigate("login")
        },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(70.dp) ){
            Text("Login", textAlign = TextAlign.Center, fontSize = 20.sp)
        }
        Spacer(modifier= Modifier.height(8.dp))
        OutlinedButton(onClick = {
            navController.navigate("sign")
        },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(70.dp)){
            Text("SignUp", textAlign = TextAlign.Center, fontSize = 20.sp)
        }
    }
}