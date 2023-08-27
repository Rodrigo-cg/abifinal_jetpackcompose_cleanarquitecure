package com.abi.abifinal.domain.use_cases.users

import android.content.Context
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.UsersRepository
import quevedo.soares.leandro.blemadeeasy.BLE
import javax.inject.Inject

class GetParametersBle @Inject constructor(private val repository: UsersRepository) {

    suspend operator fun invoke(ble: BLE) = repository.getParametersBle(ble)

}