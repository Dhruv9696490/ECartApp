package com.example.ecartapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject

object Utils {
    fun showToast(context: Context,message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    fun addToCart(context: Context, id: String){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val cart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val cartItem = cart[id] ?: 0
                val currentCartItem = cartItem + 1
                val currentCart = mapOf("cartItems.$id" to currentCartItem)
                userDoc.update(currentCart).addOnCompleteListener {
                    if(it.isSuccessful){
                      showToast(context,"Item Added Successfully")
                    }else{
                        showToast(context,"something went wrong")
                    }
                }
            }
        }
    }
    fun decreaseItemCart(context: Context,id: String , boolean: Boolean=false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            val cart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
            val cartItem = cart[id] ?:0
            val item=cartItem-1
            val currentCart = if(item<=0 || boolean){
                mapOf("cartItems.$id" to FieldValue.delete())
            }else{
                mapOf("cartItems.$id" to item)
            }
            userDoc.update(currentCart).addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(context,"Working")
                }else{
                    showToast(context,"error")
                }
            }
        }
    }
    fun taxPercentage(): Float{
        return 10f
    }
    fun discountPercentage(): Float{
        return 13.0f
    }
    fun razorPayApi(): String{
        return "rzp_test_R5xrWeqnTCCFY1"
    }
    fun startPayment(amount: Float){
        val checkOut = Checkout()
        checkOut.setKeyID(razorPayApi())
        val options = JSONObject()
        options.put("name","Easy Shop")
        options.put("description","")
        options.put(" ",amount*100)
        options.put("currency","INR")
         checkOut.open(GlobalNavigation.navController.context  as Activity,options)
    }
}