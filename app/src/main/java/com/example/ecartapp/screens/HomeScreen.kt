package com.example.ecartapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.ecartapp.ECartViewModel
import com.example.ecartapp.bottomscreens.AccountScreen
import com.example.ecartapp.bottomscreens.CartScreen
import com.example.ecartapp.bottomscreens.FavoriteScreen
import com.example.ecartapp.bottomscreens.HomeNavScreen

@Composable
fun HomeScreen(modifier: Modifier, navController: NavHostController, viewModel: ECartViewModel){
    val bottomBar = listOf<BottomBar>(
        BottomBar(Icons.Default.Home,"Home"),
        BottomBar(Icons.Default.Favorite,"Favorite"),
        BottomBar(Icons.Default.ShoppingCart,"Cart"),
        BottomBar(Icons.Default.AccountCircle,"Profile"),
    )
    var indexOf by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar{
                bottomBar.forEachIndexed {index,bottomBar->
                    NavigationBarItem(
                        selected =index==indexOf,
                        icon = { Icon(bottomBar.icon,null)},
                        onClick = {indexOf=index},
                        label = {Text(bottomBar.label)}
                    )
                }
            }
        }
    ){
        ContentScreen(modifier.padding(it),indexOf)
    }
}
@Composable
fun ContentScreen(modifier: Modifier,selectedIndex: Int){
    when(selectedIndex){
        0->  HomeNavScreen(modifier)
        1-> FavoriteScreen(modifier)
        2-> CartScreen(modifier)
        3-> AccountScreen(modifier)
    }
}
data class BottomBar(
    val icon: ImageVector,
    val label: String
)