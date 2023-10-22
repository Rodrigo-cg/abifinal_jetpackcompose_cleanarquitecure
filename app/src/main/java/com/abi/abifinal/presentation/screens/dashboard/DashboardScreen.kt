package com.abi.abifinal.presentation.screens.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.abi.abifinal.presentation.navigation.DetailsScreen
import com.abi.abifinal.presentation.screens.dashboard.components.DashboardContent
import com.abi.abifinal.presentation.screens.profile.components.ProfileContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.chromium.base.ThreadUtils.runOnUiThread
import quevedo.soares.leandro.blemadeeasy.BLE

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(cambiaEstadoBluetooh:()->Unit
                        ,navController: NavHostController){





    Scaffold(
        topBar = {},
        content = {
            DashboardContent(cambiaEstadoBluetooh,navController)
        },
        bottomBar = {}
    )

}