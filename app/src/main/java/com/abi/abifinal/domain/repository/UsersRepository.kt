package com.abi.abifinal.domain.repository

import android.content.Context
import android.location.Location
import androidx.activity.ComponentActivity
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.presentation.utils.Recursos
import kotlinx.coroutines.flow.Flow
import quevedo.soares.leandro.blemadeeasy.BLE
import java.io.File

interface UsersRepository {
    suspend fun create(user: User): Response<Boolean>

    suspend fun update(user: User): Response<Boolean>

    suspend fun saveImage(file: File): Response<String>

    fun getUserById(id: String): Flow<User>

    //Sensores
    suspend fun getParametersBle(componentActivity: ComponentActivity):Response<Boolean>

    suspend fun sendMsmSos():Response<Boolean>
/*
    suspend fun updateSensors(user: User): Response<Boolean>*/
    suspend fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()


}