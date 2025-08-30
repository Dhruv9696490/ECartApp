package com.example.ecartapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EditScreen(){
    var user: UserModel? by remember { mutableStateOf(UserModel()) }
    var name by remember { mutableStateOf(user?.name) }
    var email by remember { mutableStateOf(user?.email) }
    var phoneNo by remember { mutableStateOf(user?.phoneNumber) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    user = it.result.toObject(UserModel::class.java)
                }
            }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, start = 20.dp, end = 20.dp)
        ){
            IconButton(onClick = { GlobalNavigation.navController.navigateUp()},
                modifier = Modifier.size(40.dp).padding(0.dp).align(Alignment.TopStart)){
                Icon(Icons.AutoMirrored.Filled.ArrowBack,null,
                    modifier = Modifier.size(40.dp))
            }
            Card(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .align(Alignment.Center),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)){
                Column(
                    modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        "Edit Screen",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    name?.let { it1 ->
                        OutlinedTextField(
                            value = it1,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            placeholder = { Text("enter your name") })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    email?.let { it1 ->
                        OutlinedTextField(
                            value = it1,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            placeholder = { Text("enter your Email") })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    phoneNo?.let { it1 ->
                        OutlinedTextField(
                            value = it1,
                            onValueChange = { phoneNo = it },
                            label = { Text("Phone Number") },
                            placeholder = { Text("enter your phone number") })
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    Button(onClick = {
                        if (!name.isNullOrEmpty() && !email.isNullOrEmpty() && !phoneNo.isNullOrEmpty()) {
                            val editItem = mapOf(
                                "name" to name,
                                "email" to email,
                                "phoneNumber" to phoneNo
                            )
                            Firebase.firestore.collection("users")
                                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                                .update(editItem)

                        }
                        GlobalNavigation.navController.popBackStack()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(50.dp)) {
                        Text("Confirm", fontSize = 20.sp)
                    }
                }
            }

        }
    }