package com.abi.abifinal.domain.use_cases.auth

import com.abi.abifinal.presentation.navigation.AuthScreen

data class AuthUseCase (
    val getCurrentUser: GetCurrentUser,
    val login: Login,
    val logout: Logout,
    val singUp: SingUp
)