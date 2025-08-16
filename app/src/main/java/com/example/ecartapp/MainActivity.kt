package com.example.ecartapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ecartapp.ui.theme.ECartAppTheme
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECartAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->
                    Navigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Utils.showToast(this,"Payment Success")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Utils.showToast(this,"Payment Failed")
    }
}