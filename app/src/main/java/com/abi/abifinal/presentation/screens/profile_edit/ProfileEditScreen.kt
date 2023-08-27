package com.abi.abifinal.presentation.screens.profile_edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.abi.abifinal.domain.use_cases.users.SaveImage
import com.abi.abifinal.domain.use_cases.users.Update
import com.abi.abifinal.presentation.components.DefaultTopBar
import com.abi.abifinal.presentation.screens.profile_edit.components.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileEditScreen(navController: NavHostController, user: String) {

    Log.d("ProfileEditScreen", "usuario: $user")

    Scaffold(
        topBar = {
            DefaultTopBar(
                tittle = "Editar usuario",
                upAvailable = true,
                navController = navController
            )

        },
        content = {
            ProfileEditContent(navController)
        },
        bottomBar = {

        }
    )

    SaveImage()
    Update()


}