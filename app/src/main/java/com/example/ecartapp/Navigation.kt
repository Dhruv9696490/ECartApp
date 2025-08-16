package com.example.ecartapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecartapp.screens.AuthScreen
import com.example.ecartapp.screens.CategoryItem
import com.example.ecartapp.screens.CheckOutScreen
import com.example.ecartapp.screens.EditScreen
import com.example.ecartapp.screens.HomeScreen
import com.example.ecartapp.screens.ItemScreen
import com.example.ecartapp.screens.LoginScreen
import com.example.ecartapp.screens.SighUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(modifier: Modifier){
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val viewModel: ECartViewModel = viewModel()
    val isLoggedIn = FirebaseAuth.getInstance().currentUser?.uid != null
    val startDestination = if (isLoggedIn) "home" else "auth"
    NavHost(navController,startDestination){
        composable("auth"){
            AuthScreen(modifier, navController, viewModel)
        }
        composable("login"){
            LoginScreen(modifier, navController, viewModel)
        }
        composable("sign"){
            SighUpScreen(modifier, navController, viewModel)
        }
        composable("home"){
            HomeScreen(modifier, navController, viewModel)
        }
        composable("category-item/{id}"){
            val id = it.arguments?.getString("id")?: ""
            CategoryItem(modifier, id)
        }
        composable("item-screen/{id}"){it->
            val id = it.arguments?.getString("id")?: ""
            ItemScreen(modifier, id)
        }
        composable("check-out"){
            CheckOutScreen()
        }
        composable("edit"){
            EditScreen()
        }
    }
}
object GlobalNavigation {
    lateinit var navController: NavHostController

}














