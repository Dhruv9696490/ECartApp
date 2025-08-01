package com.example.ecartapp.bottomscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecartapp.componentsview.BannerView
import com.example.ecartapp.componentsview.CategoryView
import com.example.ecartapp.componentsview.HeaderView

@Composable
fun HomeNavScreen(modifier: Modifier){
    Column(Modifier.fillMaxSize().padding(top = 64.dp, start = 8.dp, end = 8.dp )){
        HeaderView(Modifier.height(73.dp))
        Spacer(modifier = Modifier.height((0).dp))
        BannerView(modifier = Modifier.height(140.dp))
        CategoryView(modifier = Modifier)
    }
}