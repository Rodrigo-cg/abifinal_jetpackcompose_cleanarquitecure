package com.abi.abifinal.domain.use_cases.users

import android.location.Location
import com.abi.abifinal.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserLocationUseCase(private val repository: UsersRepository) {
    suspend fun getLocationUpdates(interval: Long)= repository.getLocationUpdates(interval)
}
