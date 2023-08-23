package com.abi.abifinal.presentation.screens.singup.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.presentation.navigation.Graph
import com.abi.abifinal.presentation.screens.login.components.ProgressBar
import com.abi.abifinal.presentation.screens.singup.SingupViewModel

@Composable
fun SingUp(navController: NavController, viewModel : SingupViewModel = hiltViewModel()) {
    when(val singupResponse = viewModel.singupResponse){
        Response.Loading ->{
            ProgressBar()
        }
        is Response.Succes ->{
            LaunchedEffect(Unit){
                viewModel.createUser()
                navController.popBackStack(Graph.AUTHENTICATION, true)
                navController.navigate(route = Graph.HOME)
            }
        }
        is Response.Faliure -> {
            Toast.makeText(LocalContext.current, singupResponse.exception.message, Toast.LENGTH_SHORT).show()
        }
        else -> {

        }
    }
}