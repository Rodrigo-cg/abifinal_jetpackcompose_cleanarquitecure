package com.abi.abifinal.domain.use_cases.auth

import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.AuthRepository
import javax.inject.Inject

class SingUp @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) = repository.singUp(user)

}