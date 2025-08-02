package com.example.ecartapp.componentsview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun HeaderView(modifier: Modifier){
    var name by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        val user = FirebaseAuth.getInstance().currentUser?.uid
        if (!user.isNullOrEmpty()){
            Firebase.firestore.collection("users")
                .document(user)
                .get().addOnCompleteListener{
                    name = it.result.get("name").toString().split(" ").get(0)
                }
        }

    }
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Column{
            Text("Welcome Back!", fontSize = 32.sp,
                color = Color.DarkGray)
            Text(name,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray)
        }
        Icon(Icons.Default.Search,null,Modifier.size(32.dp))
    }
}