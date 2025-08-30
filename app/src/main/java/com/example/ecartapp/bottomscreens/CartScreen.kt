package com.example.ecartapp.bottomscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.Utils
import com.example.ecartapp.model.CategoryModel
import com.example.ecartapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun CartScreen(modifier: Modifier) {
    var cartItems: UserModel? by remember { mutableStateOf(UserModel()) }
    DisposableEffect(Unit){
        val cartItem = Firebase.firestore.collection("users").document(
            FirebaseAuth.getInstance().currentUser?.uid!!
        ).addSnapshotListener {it,_->
            if(it!=null){
                cartItems=it.toObject(UserModel::class.java)
            }
        }
        onDispose {
            cartItem.remove()
        }
    }
    Column(modifier
        .fillMaxSize()){
        Text(" Cart Items-",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif)
        LazyColumn(modifier = Modifier.weight(1f)){
            items(cartItems?.cartItems?.toList()!!, key = {it.first}){it->
                CartItemScreen(it.first,it.second)
            }
        }
        if(cartItems?.cartItems?.values?.isNotEmpty() == true){
            Button(onClick = {
                GlobalNavigation.navController.navigate("check-out")
            }, modifier = Modifier.fillMaxWidth().padding(8.dp).height(50.dp)){
                Text("Check out", fontSize = 20.sp)
            }
        }
    }
}
@Composable
fun CartItemScreen(id: String,quantity: Long){
    val context = LocalContext.current
    var cartItem: CategoryModel? by remember { mutableStateOf(CategoryModel()) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("icons").collection("products")
            .document(id).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val new=it.result.toObject(CategoryModel::class.java)
                    if (new!=null){
                        cartItem=new
                    }
                }
            }
    }
    Card(modifier = Modifier.wrapContentSize().padding(vertical = 8.dp, horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(8.dp)){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            if(cartItem?.name=="Apple MacBook Pro"){
                AsyncImage(model = cartItem?.image?.firstOrNull(), null,
                    modifier = Modifier.size(120.dp)
                        .graphicsLayer(
                            scaleX = -1.1f,
                            scaleY =  1.1f
                        ))
            }
            else if(cartItem?.name=="Apple iPhone 16 (Black, 128 GB)"){
                AsyncImage(model = cartItem?.image?.firstOrNull(), null,
                    modifier = Modifier.size(120.dp)
                        .graphicsLayer(
                            scaleX = -1.3f,
                            scaleY =  1.3f
                        ))
            }
                else{
            AsyncImage(model = cartItem?.image?.firstOrNull(), null,
                modifier = Modifier.size(120.dp)
                    .graphicsLayer(
                        scaleX = -1f,
                        scaleY = 1f
                    ))
            }

            Column(modifier = Modifier.weight(1f).padding(8.dp)){
                Text(cartItem?.name!!, fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text("₹"+cartItem?.realprice!!, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = {
                        Utils.decreaseItemCart(context,id,false)
                    }){
                        Text("-", fontSize = 24.sp, fontWeight = FontWeight.SemiBold) }
                    Spacer(modifier = Modifier.width(0.dp))
                    Text(quantity.toString(), fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(0.dp))
                    IconButton(onClick = {
                        Utils.addToCart(context,id)
                    }){
                        Text("+", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
//            Text(modifier = Modifier.weight(1f).background(Color.Green), text = "rdrrdde")
            IconButton(onClick = {
                Utils.decreaseItemCart(context,id,true)
            }){
                Icon(Icons.Default.Delete,null,
                    modifier = Modifier.size(30.dp))
            }
        }
    }
}
