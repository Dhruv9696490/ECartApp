package com.example.ecartapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ecartapp.ECartState
import com.example.ecartapp.ECartViewModel
import com.example.ecartapp.R

@Composable
fun SighUpScreen(modifier: Modifier, navController: NavHostController, viewModel: ECartViewModel){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    val context =LocalContext.current
    LaunchedEffect(viewModel.eCartState.value){
        when(viewModel.eCartState.value){
            is ECartState.Authenticated -> navController.navigate("home")
            is ECartState.Error -> Toast.makeText(context,(viewModel.eCartState.value as ECartState.Error).error, Toast.LENGTH_LONG).show()
            else -> Unit
        }
    }
    Column(modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(id = R.drawable.onlineshopping),null,
            Modifier.size(500.dp,300.dp))
        Text("Hello Buddy!",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Create an new account",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            ))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = name,
            onValueChange = {name=it},
            label = {Text("Name")},
            placeholder = {Text("Enter your full name")},
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = email,
            onValueChange = {email=it},
            label = {Text("Email")},
            placeholder = {Text("Enter your email")},
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = phoneNumber,
            onValueChange = {phoneNumber=it},
            label = {Text("Mobile No.")},
            placeholder = {Text("Enter your Mobile Number")},
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(value = password,
            onValueChange = {password=it},
            label = {Text("Password")},
            placeholder = {Text("Enter your password")},
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
            visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = {
            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty()){
                viewModel.sign(name,email,password,phoneNumber)
            }else{
                Toast.makeText(context,"enter the login details", Toast.LENGTH_LONG).show()
            }
        },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            enabled = viewModel.eCartState.value!= ECartState.Loading){
            Text("SignIn", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Serif)
        }
        TextButton(onClick = {
            navController.navigate("login")
        }){
            Text("Already have account, login!")
        }
    }
}