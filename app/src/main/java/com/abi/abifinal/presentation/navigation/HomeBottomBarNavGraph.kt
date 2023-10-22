package com.abi.abifinal.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abi.abifinal.presentation.screens.dashboard.DashboardScreen
import com.abi.abifinal.presentation.screens.medicamentos.MedicamentosScreen
import com.abi.abifinal.presentation.screens.profile.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeBottomBarNavGraph(navController: NavHostController,cambiaEstadoBluetooh:()->Unit) {

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeBottomBarScreen.Dashboard.route
    ){


        composable(route = HomeBottomBarScreen.Profile.route){
            ProfileScreen(navController =  navController)
        }

        composable(route = HomeBottomBarScreen.Dashboard.route){
            DashboardScreen(navController= navController, cambiaEstadoBluetooh = cambiaEstadoBluetooh)
        }

        composable(route = HomeBottomBarScreen.Medicamentos.route){
            MedicamentosScreen(navController=navController)
        }

        detailsNavGraph(navController)
    }

}

sealed class HomeBottomBarScreen(
    val route: String,
    var tittle: String,
    var icon: ImageVector
) {

    object Medicamentos : HomeBottomBarScreen(
        route = "posts_list",
        tittle = "Mis pastillas",
        icon = Icons.Outlined.List
    )

    object Dashboard : HomeBottomBarScreen(
        route = "my_dashboard",
        tittle = "Dashboard",
        icon = Icons.Outlined.List
    )

    object Profile :
        HomeBottomBarScreen(
            route = "profile",
            tittle = "Perfil",
            icon = Icons.Default.Person)



}
