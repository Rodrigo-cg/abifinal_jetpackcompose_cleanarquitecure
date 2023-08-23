package com.abi.abifinal.domain.use_cases.users

import com.abi.abifinal.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserById @Inject constructor(private val repository: UsersRepository) {

    operator fun invoke (id: String) = repository.getUserById(id)

}