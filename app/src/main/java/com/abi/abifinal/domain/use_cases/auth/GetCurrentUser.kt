package com.abi.abifinal.domain.use_cases.auth

import com.abi.abifinal.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUser @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.currentUser

}