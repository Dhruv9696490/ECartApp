package com.example.ecartapp.bottomscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecartapp.ECartState
import com.example.ecartapp.ECartViewModel
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.R
import com.example.ecartapp.Utils
import com.example.ecartapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun AccountScreen(modifier: Modifier) {
    val viewmodel: ECartViewModel = viewModel()
    LaunchedEffect(viewmodel.eCartState.value){
        when(viewmodel.eCartState.value){
           is ECartState.Unauthenticated -> {
               GlobalNavigation.navController.navigate("auth")
           }
            else -> Unit
        }
    }
    val viewModel: ECartViewModel= viewModel()
    var userData: UserModel? by remember { mutableStateOf(UserModel()) }
    val context = LocalContext.current
    var address by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).get().addOnCompleteListener {
                if(it.isSuccessful){
                   userData=it.result.toObject(UserModel()::class.java)
                }
            }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 30.dp)){
        IconButton(onClick = {
            viewModel.signOut()
        }, modifier = Modifier
            .align(alignment = Alignment.TopEnd)
            .padding(16.dp)){
            Icon(Icons.AutoMirrored.Filled.ExitToApp,null,
                modifier = Modifier.size(50.dp))
        }
        Column(modifier.fillMaxSize(),
            horizontalAlignment =Alignment.CenterHorizontally ){
            Text("Profile",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif)
            Image(painter = painterResource(id = R.drawable.img),null, modifier = Modifier.size(300.dp))

            Spacer(modifier = Modifier.padding(4.dp))
            //   HorizontalDivider(modifier = Modifier.padding(8.dp))
            Text(userData?.name!!,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif)
            Spacer(modifier = Modifier.padding(4.dp))
            Card(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)){
                Text(
                    "Email: " + userData?.email!!,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
            //  HorizontalDivider(modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.padding(4.dp))
            Card(modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)){
                Text(
                    "Contact: " + userData?.phoneNumber!!,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        "Home Address",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    TextField(
                        value = address,
                        onValueChange = {address=it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(60.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        placeholder = { Text("enter your home address") }
                        ,keyboardActions = KeyboardActions(onDone = {
                            if(address.isNotEmpty()){
                                Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
                                    .update("address",address).addOnCompleteListener {
                                        if(it.isSuccessful){
                                            Utils.showToast(context,"Address Updated Successfully")
                                        }
                                        else{
                                            Utils.showToast(context,"Something Went Wrong")
                                        }
                                    }
                            }else{
                                Utils.showToast(context,"Address Can't Be Empty")
                            }
                        }))
                }
            }
        }
    }
