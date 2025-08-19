package com.example.ecartapp.bottomscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.R
import com.example.ecartapp.Utils
import com.example.ecartapp.model.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun FavoriteScreen(modifier: Modifier) {
    val context = LocalContext.current
    var favItems by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(Unit){
        FirebaseAuth.getInstance().currentUser?.uid?.let {id->
            Firebase.firestore.collection("users").document(id)
                .get().addOnCompleteListener {
                    if (it.isSuccessful){
                        favItems= it.result.get("favorite") as? List<String> ?: emptyList()
                    }else{
                        Utils.showToast(context,"Error 404")
                    }
                }
        }
    }
    Column(Modifier.fillMaxSize().padding(top=40.dp, bottom = 100.dp)){
        Text("Favorite Items-",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif)
        LazyColumn(modifier = Modifier.weight(1f)){
            items(favItems,{it}){items->
                    FavItemScreen(items){ removeId ->
                        favItems = favItems.filterNot { it == removeId }
                    }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Button(onClick = {
            val idNo=2
            GlobalNavigation.navController.navigate("home/${idNo}")
        },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.white)),
            modifier = Modifier.fillMaxWidth().height(60.dp)){
            Text("Go To Cart", fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
        }
    }
}

@Composable
fun FavItemScreen(id: String,onRemove:(String)-> Unit){
    val context = LocalContext.current
    var category by remember { mutableStateOf(CategoryModel()) }
    DisposableEffect(Unit) {
        val firebase = Firebase.firestore.collection("data").document("icons")
            .collection("products").document(id).addSnapshotListener {it,_->
                category = it?.toObject(CategoryModel::class.java) as CategoryModel
            }
        onDispose { firebase.remove() }
    }
    Card(elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.padding(16.dp),
        onClick = {
            GlobalNavigation.navController.navigate("item-screen/${id}")
        },
        colors = CardDefaults.cardColors(containerColor = Color.White)){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            Column(
                verticalArrangement = Arrangement.Center){
                AsyncImage(model = category.image.firstOrNull(),null,
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).padding(start = 16.dp),
                    contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.height(2.dp))
                Text(category.name,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column{
                IconButton(onClick = {
                    Utils.removeItem(context,id)
                    onRemove(id)
                },
                    modifier = Modifier.size(70.dp)
                ){
                    Icon(painter = painterResource(R.drawable.hearted),null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.Unspecified)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("₹"+category.realprice,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}