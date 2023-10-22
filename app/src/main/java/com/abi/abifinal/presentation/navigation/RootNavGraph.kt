package com.abi.abifinal.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abi.abifinal.presentation.screens.home.HomeScreen
import quevedo.soares.leandro.blemadeeasy.BLE

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(navController: NavHostController,cambiaEstadoBluetooh:()->Unit


) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ){

        authNavGraph(navController = navController)

        composable(route = Graph.HOME){

            HomeScreen(cambiaEstadoBluetooh = cambiaEstadoBluetooh) // Pasar componentActivity aquí
        }

    }

}


