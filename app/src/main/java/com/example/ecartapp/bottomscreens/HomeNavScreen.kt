package com.example.ecartapp.bottomscreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ecartapp.ECartState
import com.example.ecartapp.ECartViewModel
import com.example.ecartapp.GlobalNavigation.navController
import com.example.ecartapp.Utils
import com.example.ecartapp.Utils.showToast
import com.example.ecartapp.componentsview.BannerView
import com.example.ecartapp.componentsview.CategoryView
import com.example.ecartapp.componentsview.HeaderView
import com.example.ecartapp.model.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@Composable
fun HomeNavScreen(modifier: Modifier){
    val viewModel: ECartViewModel =viewModel()
    LaunchedEffect(viewModel.eCartState.value){
        when(viewModel.eCartState.value){
            is ECartState.Unauthenticated -> navController.navigate("auth")
            else -> Unit
        }
    }
    var items by remember { mutableStateOf(listOf(CategoryModel())) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("icons").collection("products")
            .get().addOnSuccessListener{
                    items=it.toObjects(CategoryModel::class.java).plus(it.toObjects(CategoryModel::class.java)).plus(it.toObjects(CategoryModel::class.java))
            }
    }
    Column(Modifier.fillMaxSize().padding(top = 64.dp, start = 8.dp, end = 8.dp )){
        HeaderView(Modifier.height(73.dp))
        Spacer(modifier = Modifier.height((0).dp))
        BannerView(modifier = Modifier.height(140.dp))
        CategoryView()
        LazyVerticalGrid(GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding((0).dp)){
            items(items){it->
                HomeScreenItems(it)
            }
        }
    }
}

@Composable
fun HomeScreenItems(item: CategoryModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(200.dp,300.dp)
            .clickable(onClick = {
                navController.navigate("item-screen/" + item.id)
            }),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ){
        Box{
            Column(
                modifier = Modifier.padding(top = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                AsyncImage(
                    item.image.firstOrNull(), null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .graphicsLayer(
                            scaleX = -1.2f,
                            scaleY = 1.2f
                        )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    item.name,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹ ${item.realprice}", color = Color.DarkGray)
                    IconButton(onClick = {
                        Utils.addToCart(context, item.id)
                    }){
                        Icon(Icons.Default.ShoppingCart, null)
                    }

                }

            }
            IconButton(onClick = {
                addItem(context,item.id)
            }, modifier = Modifier.size(40.dp).align(alignment = Alignment.TopEnd)
                .background(Color.White, CircleShape)
            ){
                Icon(Icons.Default.Favorite,null,modifier = Modifier.size(30.dp),
                    tint = Color.DarkGray)
            }
        }
    }
}
fun addItem(context: Context,id: String){
       val firebase = Firebase.firestore.collection("users").document(Utils.userId())
            firebase.update("favorite", FieldValue.arrayUnion(id)).addOnCompleteListener{
                if(it.isSuccessful){
                    showToast(context,"item added to favorite")
                }else{
                    showToast(context,"Error 404")
              }
            }
    }
