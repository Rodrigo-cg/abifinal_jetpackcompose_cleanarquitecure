package com.abi.abifinal.presentation.screens.profile_edit.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.presentation.screens.login.components.ProgressBar
import com.abi.abifinal.presentation.screens.profile_edit.ProfileEditViewModel

@Composable
fun Update(viewModel: ProfileEditViewModel = hiltViewModel()) {

    when(val updateResponse = viewModel.updateResponse){
        Response.Loading -> {
            ProgressBar()
        }
        is Response.Succes -> {
            Toast.makeText(LocalContext.current, "Datos acrualizados", Toast.LENGTH_LONG).show()
        }
        is Response.Faliure -> {
            Toast.makeText(LocalContext.current, updateResponse.exception.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
        }
        else -> {

        }
    }


}