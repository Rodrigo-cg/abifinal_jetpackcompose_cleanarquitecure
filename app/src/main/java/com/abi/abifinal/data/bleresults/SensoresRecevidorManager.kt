package com.abi.abifinal.data.bleresults

import com.abi.abifinal.presentation.utils.Recursos
import kotlinx.coroutines.flow.MutableSharedFlow

interface SensoresRecevidorManager {
    val data: MutableSharedFlow<Recursos<Sensor1Result>>
    val data2: MutableSharedFlow<Recursos<Sensor2Result>>
    val data3: MutableSharedFlow<Recursos<Sensor3Result>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}