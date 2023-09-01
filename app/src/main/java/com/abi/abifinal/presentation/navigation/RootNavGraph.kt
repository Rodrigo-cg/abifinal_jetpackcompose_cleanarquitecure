package com.abi.abifinal.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abi.abifinal.presentation.screens.home.HomeScreen
import com.abi.abifinal.presentation.screens.home.rememberBLE

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ){

        authNavGraph(navController = navController)

        composable(route = Graph.HOME){

            HomeScreen(componentActivity = componentActivity) // Pasar componentActivity aquí
        }

    }

}


