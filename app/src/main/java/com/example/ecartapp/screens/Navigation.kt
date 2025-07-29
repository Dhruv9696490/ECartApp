package com.example.ecartapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecartapp.ECartViewModel

@Composable
fun Navigation(modifier: Modifier){
    val navController = rememberNavController()
    val viewModel: ECartViewModel = viewModel()
    NavHost(navController,"home"){
        composable("auth"){
            AuthScreen(modifier,navController,viewModel)
        }
        composable("login"){
            LoginScreen(modifier,navController,viewModel)
        }
        composable("sign"){
            SighUpScreen(modifier,navController,viewModel)
        }
        composable("home"){
            HomeScreen(modifier, navController, viewModel)
        }
    }
}