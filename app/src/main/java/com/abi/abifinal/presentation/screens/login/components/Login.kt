package com.abi.abifinal.presentation.screens.login.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.presentation.navigation.Graph
import com.abi.abifinal.presentation.screens.login.LoginViewModel

@Composable
fun Login(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()){
    when(val loginResponse = viewModel.loginResponse){
        Response.Loading ->{
            ProgressBar()
        }
        is Response.Succes -> {
            LaunchedEffect(Unit){
                navController.navigate(route = Graph.HOME){
                    popUpTo(Graph.AUTHENTICATION) {inclusive = true}
                }
            }
            Toast.makeText(LocalContext.current, "Bienvenido :D", Toast.LENGTH_SHORT).show()
        }
        is Response.Faliure -> {
            Toast.makeText(LocalContext.current, loginResponse.exception.message ?: "Error Desconocido" , Toast.LENGTH_SHORT).show()
        }
        else -> {
            //Toast.makeText(LocalContext.current, "Error Desconocido" , Toast.LENGTH_SHORT).show()
        }

    }
}