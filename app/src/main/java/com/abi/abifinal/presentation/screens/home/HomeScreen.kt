package com.abi.abifinal.presentation.screens.home

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.abi.abifinal.presentation.navigation.HomeBottomBarNavGraph
import com.abi.abifinal.presentation.navigation.HomeBottomBarScreen
import com.abi.abifinal.presentation.screens.login.LoginViewModel
import quevedo.soares.leandro.blemadeeasy.BLE

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    //val ble = BLE(componentActivity = LocalContext.current)

    /*LaunchedEffect(Unit) {
        // Realiza operaciones con los parámetros si es necesario
    }*/
    //viewModel.getParametersBle(ble)

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        HomeBottomBarNavGraph(navController)
    }
}
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        HomeBottomBarScreen.Posts,
        HomeBottomBarScreen.MyPosts,
        HomeBottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBasDestination = screens.any { it.route == currentDestination?.route }

    if (bottomBasDestination){

        NavigationBar() {
            screens.forEach{ screen ->

                AddItem(screen = screen, currentDestination = currentDestination, navController = navController)

            }
        }

    }

}

@Composable
fun RowScope.AddItem(
    screen: HomeBottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    NavigationBarItem(
        label = {
            Text(text = screen.tittle)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(unselectedIconColor = LocalContentColor.current.copy(alpha = 1.0F)),
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Nav Icon")
        }
    )

}