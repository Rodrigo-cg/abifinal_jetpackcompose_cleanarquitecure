package com.abi.abifinal.presentation.screens.medicamentos_edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.abi.abifinal.presentation.components.DefaultTopBar
import com.abi.abifinal.presentation.screens.medicamentos_edit.components.MedicamentosEditContent
import com.abi.abifinal.presentation.screens.profile_edit.components.ProfileEditContent
import com.abi.abifinal.presentation.screens.profile_edit.components.SaveImage
import com.abi.abifinal.presentation.screens.profile_edit.components.Update

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MedicamentosEditScreen(navController: NavHostController, user: String) {

    Log.d("ProfileEditScreen", "usuario: $user")

    Scaffold(
        topBar = {
            DefaultTopBar(
                tittle = "Agregar Horarios de Medicamento",
                upAvailable = true,
                navController = navController
            )

        },
        content = {
            MedicamentosEditContent(navController)
        },
        bottomBar = {

        }
    )

    SaveImage()
    Update()


}