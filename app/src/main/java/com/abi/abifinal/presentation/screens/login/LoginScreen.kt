package com.abi.abifinal.presentation.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abi.abifinal.presentation.screens.login.components.Login
import com.abi.abifinal.presentation.screens.login.components.LoginBottomBar
import com.abi.abifinal.presentation.screens.login.components.LoginContent
import com.abi.abifinal.presentation.ui.theme.AbifinalTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {


    Scaffold(
        topBar = {},
        content = {
            LoginContent(navController)
        },
        bottomBar = {
            LoginBottomBar(navController)
        }
    )

    //Manejar estado de petición de login
    Login(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AbifinalTheme(darkTheme = true) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(rememberNavController())
        }
    }
}