package com.abi.abifinal.presentation.screens.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.use_cases.auth.AuthUseCase
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import quevedo.soares.leandro.blemadeeasy.BLE
import javax.inject.Inject

class HomeScreenViewModel  @Inject constructor(
    private val authUseCase: AuthUseCase,
    val usersUseCases: UsersUseCases,
) : ViewModel() {

    var userData by mutableStateOf(User())
        private set

    init {
        getUserById()
    }

    private fun getUserById( )= viewModelScope.launch {
        usersUseCases.getUserById(authUseCase.getCurrentUser()!!.uid).collect(){user ->
            userData = user
        }
    }
     fun getParametersBle(ble: BLE)=viewModelScope.launch {
        usersUseCases.getParametersBle(ble)
    }


    fun logout() {
        authUseCase.logout()
    }



}