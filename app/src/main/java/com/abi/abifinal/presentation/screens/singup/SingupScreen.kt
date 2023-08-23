package com.abi.abifinal.presentation.screens.singup

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abi.abifinal.presentation.components.DefaultTopBar
import com.abi.abifinal.presentation.screens.singup.components.SingUp
import com.abi.abifinal.presentation.screens.singup.components.SingupContent
import com.abi.abifinal.presentation.ui.theme.AbifinalTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingupScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                tittle = "Nuevo Usuario",
                upAvailable = true,
                navController = navController
            )

        },
        content = {
            SingupContent(navController)
        },
        bottomBar = {

        }
    )

    SingUp(navController = navController)
}
@Composable
fun SignupContent(navController: NavHostController) {
    // Implement your content here
    // For example, a form for user signup
}

@Composable
fun SignUp(navController: NavHostController) {
    // Implement your signup component here
    // This could be a button to initiate the signup process
}
@Preview(showBackground = true, showSystemUi = true, )
@Composable
fun Preview() {
    AbifinalTheme(darkTheme = true) {
        SingupScreen(rememberNavController())
    }

}