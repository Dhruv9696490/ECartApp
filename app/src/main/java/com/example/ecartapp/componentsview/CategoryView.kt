package com.example.ecartapp.componentsview

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecartapp.model.IconCategoryModel
import com.example.ecartapp.GlobalNavigation
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryView(modifier: Modifier){
    var categories by remember {  mutableStateOf<List<IconCategoryModel>>(emptyList())}
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("icons").collection("categories")
            .get().addOnCompleteListener {
                categories = it.result.documents.mapNotNull {
                    it.toObject(IconCategoryModel::class.java)
                }
            }
    }
    Column{
        Text("Category",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray)
        LazyRow{
            items(categories){item->
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    AsyncImage(model = item.url,null,
                        modifier = Modifier.size(110.dp).padding(4.dp)
                            .clip(RoundedCornerShape(196.dp))
                            .border(1.dp, Color.Black, CircleShape)
                            .clickable(onClick = {
                                GlobalNavigation.navController.navigate("category-item/"+item.id)
                            }))
                }
            }
        }
    }
}