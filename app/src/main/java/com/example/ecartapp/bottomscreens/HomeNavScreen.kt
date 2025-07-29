package com.example.ecartapp.bottomscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecartapp.componentsview.HeaderView

@Composable
fun HomeNavScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().padding(8.dp)){
        HeaderView()
    }
}