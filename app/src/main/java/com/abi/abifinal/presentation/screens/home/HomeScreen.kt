package com.abi.abifinal.presentation.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abi.abifinal.presentation.MainActivity
import com.abi.abifinal.presentation.navigation.HomeBottomBarNavGraph
import com.abi.abifinal.presentation.navigation.HomeBottomBarScreen
import com.abi.abifinal.presentation.screens.login.LoginViewModel
import com.abi.abifinal.presentation.screens.profile.ProfileViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.chromium.base.ThreadUtils.runOnUiThread
import quevedo.soares.leandro.blemadeeasy.BLE

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController(),
               @ApplicationContext componentActivity: ComponentActivity
) {
    val ble = rememberBLE(componentActivity)
    GlobalScope.launch {
        // You can specify filters for your device, being them 'macAddress', 'service' and 'name'
        val connection = ble?.scanFor(
            // You only need to supply one of these, no need for all of them!
            macAddress = "0C:B8:15:F2:D0:BE",
            name = "ESP32BLE",
            service = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"
        )
        runOnUiThread {
            Toast.makeText(componentActivity, "funciona toast", Toast.LENGTH_SHORT).show()
        }
        // And it will automatically connect to your device, no need to boilerplate
        if (connection != null) {
            // And you can continue with your code
            ble?.stopScan()
            runOnUiThread {
                Toast.makeText(componentActivity, "Conexión establecida", Toast.LENGTH_SHORT).show()
            }
            // For watching strings
            connection.observeString(characteristic = "6e400003-b5a3-f393-e0a9-e50e24dcca9e", charset = Charsets.UTF_8) { value: String ->
                Toast.makeText(componentActivity, value, Toast.LENGTH_SHORT).show()
            }
            connection.observeString(characteristic = "6e400004-b5a3-f393-e0a9-e50e24dcca9e", charset = Charsets.UTF_8) { value: String ->
                Toast.makeText(componentActivity, value, Toast.LENGTH_SHORT).show()
            }

        } else {
            runOnUiThread {
                Toast.makeText(componentActivity, "Dispositivo no disponible", Toast.LENGTH_SHORT).show()
            }
            // Show an Alert or UI with your preferred error message about the device not being available
        }
    }
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        HomeBottomBarNavGraph(navController)
    }


}
@Composable
fun rememberBLE(componentActivity: ComponentActivity): BLE {
    return remember(componentActivity) {
        BLE(componentActivity = componentActivity)
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
    navController:NavHostController
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
