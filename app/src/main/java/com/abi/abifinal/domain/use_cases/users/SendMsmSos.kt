package com.abi.abifinal.domain.use_cases.users

import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.UsersRepository
import javax.inject.Inject

class SendMsmSos @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(user: User) = repository.sendMsmSos()

}