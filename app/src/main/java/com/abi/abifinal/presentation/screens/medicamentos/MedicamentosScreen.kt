package com.abi.abifinal.presentation.screens.medicamentos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abi.abifinal.presentation.screens.profile.ProfileViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.abi.abifinal.presentation.navigation.DetailsScreen
import com.abi.abifinal.presentation.screens.medicamentos.components.MedicamentosContent
import com.abi.abifinal.presentation.screens.profile.components.ProfileContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicamentosScreen(navController: NavHostController, viewModel: MedicamentosViewModel= hiltViewModel()) {

    Scaffold (
        content = {
            MedicamentosContent(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 70.dp),
                onClick = {
                    navController.navigate(DetailsScreen.MedicamentosEdit.passUser(viewModel.userData.toJson()))
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    )

}
