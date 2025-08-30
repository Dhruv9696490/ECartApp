package com.example.ecartapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecartapp.GlobalNavigation
import com.example.ecartapp.Utils
import com.example.ecartapp.model.CategoryModel
import com.example.ecartapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@SuppressLint("SuspiciousIndentation")
@Composable
fun CheckOutScreen() {
    val checkOutItems = remember { mutableStateOf(UserModel()) }
    val items = remember { mutableStateListOf((CategoryModel())) }
    val subTotal = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }
    val tax = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }
    fun total() {
        items.forEach { it ->
            if (it.realprice.isNotEmpty()) {
                val qty = checkOutItems.value.cartItems[it.id] ?: 0
                subTotal.value += it.realprice.toFloat() * qty
            }
        }
        discount.value = subTotal.value * (Utils.discountPercentage()) / 100
        tax.value = subTotal.value * (Utils.taxPercentage()) / 100
        total.value = subTotal.value - (discount.value + tax.value)
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users").document(
            FirebaseAuth.getInstance().currentUser?.uid!!
        ).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val value = it.result.toObject(UserModel::class.java)
                if (value != null) {
                    checkOutItems.                   value = value
                    Firebase.firestore.collection("data").document("icons")
                        .collection("products")
                        .whereIn("id", checkOutItems.value.cartItems.keys.toList()).get()
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                val resultProducts =
                                    result.result.toObjects(CategoryModel::class.java)
                                items.addAll(resultProducts)
                                total()
                            }
                        }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, start = 8.dp, end = 8.dp, bottom = 30.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        IconButton(onClick = { GlobalNavigation.navController.navigateUp()},
            modifier = Modifier.size(50.dp).padding(bottom = 8.dp)){
            Icon(Icons.AutoMirrored.Filled.ArrowBack,null,
                modifier = Modifier.size(40.dp))
        }
        Text("Deliver To:", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Text(checkOutItems.value.address, fontSize = 22.sp, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Text(checkOutItems.value.name, fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold)
        Text(checkOutItems.value.phoneNumber, fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold)

        HorizontalDivider()
        items.forEach { item ->
            val price = item.realprice.toFloatOrNull()
            if (price != null){
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(item.name, fontSize = 22.sp, fontFamily = FontFamily.SansSerif,
                        maxLines = 1, modifier = Modifier.fillMaxWidth().weight(1f), overflow = TextOverflow.Ellipsis)
                    Text("₹$price", fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
                }
            }
        }

        HorizontalDivider()
        RowProperty("Subtotal",subTotal.value)
        HorizontalDivider()
        RowProperty("Tax",tax.value)
        HorizontalDivider()
        RowProperty("Discount",discount.value)
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text("Total: ", fontSize = 30.sp)
            Text("₹"+total.value.toString(), fontSize = 30.sp, fontFamily = FontFamily.SansSerif)
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
             Utils.startPayment(total.value )
        },
            modifier = Modifier.fillMaxWidth().height(60.dp)){
            Text("Pay: ₹"+total.value.toString(), fontSize =22.sp)
        }
    }
}
@Composable
fun RowProperty(name: String, value: Float){
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(name, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif,
            maxLines = 1, modifier = Modifier.fillMaxWidth().weight(1f), overflow = TextOverflow.Ellipsis)
        Text("₹$value", fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
    }
}