package com.abi.abifinal.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import com.abi.abifinal.data.bleresults.EstadoDeConeccion
import com.abi.abifinal.data.bleresults.Sensor1Result
import com.abi.abifinal.data.bleresults.Sensor2Result
import com.abi.abifinal.data.bleresults.Sensor3Result
import com.abi.abifinal.data.bleresults.SensoresRecevidorManager
import com.abi.abifinal.presentation.utils.Recursos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
@SuppressLint("MissingPermission")
class SensoresRecividorBleManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) : SensoresRecevidorManager {

    private val DEVICE_NAME = "ESP32BLE"
    private val TEMP_HUMIDITY_SERVICE_UIID = "6e400001-b5a3-f393-e0a9-e50e24dcca9e"
    private val TEMP_HUMIDITY_CHARACTERISTICS_UUID = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"
    private val TEMP_HUMIDITY_CHARACTERISTICS_UUID2 = "6E400004-B5A3-F393-E0A9-E50E24DCCA9E"
    private val TEMP_HUMIDITY_CHARACTERISTICS_UUID3 = "6E400005-B5A3-F393-E0A9-E50E24DCCA9E"

    override val data: MutableSharedFlow<Recursos<Sensor1Result>> = MutableSharedFlow()
    override val data2: MutableSharedFlow<Recursos<Sensor2Result>> = MutableSharedFlow()
    override val data3: MutableSharedFlow<Recursos<Sensor3Result>> =MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback(){

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if(result.device.name == DEVICE_NAME){
                coroutineScope.launch {
                    data2.emit(Recursos.Loading(message = "Connectingando to device..."))
                }
                if(isScanning){
                    result.device.connectGatt(context,false, gattCallback,BluetoothDevice.TRANSPORT_LE)
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    coroutineScope.launch {
                        data.emit(Recursos.Loading(message = "Discovering Services..."))
                        data2.emit(Recursos.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@SensoresRecividorBleManager.gatt = gatt
                } else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                    coroutineScope.launch {
                        data.emit(Recursos.Success(data = Sensor1Result(0f,
                            EstadoDeConeccion.Disconnected)
                        ))
                        data2.emit(Recursos.Success(data = Sensor2Result(0f,
                            EstadoDeConeccion.Disconnected)
                        ))
                        data3.emit(Recursos.Success(data = Sensor3Result(0f,
                            EstadoDeConeccion.Disconnected)
                        ))
                    }
                    gatt.close()
                }
            }else{
                gatt.close()
                currentConnectionAttempt+=1
                coroutineScope.launch {
                    data2.emit(
                        Recursos.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                    data.emit(
                        Recursos.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if(currentConnectionAttempt<=MAXIMUM_CONNECTION_ATTEMPTS){
                    startReceiving()
                }else{
                    coroutineScope.launch {
                        data.emit(Recursos.Error(errorMessage = "Could not connect to ble device"))
                        data2.emit(Recursos.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt){
                printGattTable()
                coroutineScope.launch {
                    data.emit(Recursos.Loading(message = "Adjusting MTU space..."))
                    data2.emit(Recursos.Loading(message = "Adjusting MTU space..."))
                }
                gatt.requestMtu(517)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            val characteristic1 = gatt.getService(UUID.fromString(TEMP_HUMIDITY_SERVICE_UIID))
                ?.getCharacteristic(UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID))
            val characteristic2 = gatt.getService(UUID.fromString(TEMP_HUMIDITY_SERVICE_UIID))
                ?.getCharacteristic(UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID2))
            val characteristic3 = gatt.getService(UUID.fromString(TEMP_HUMIDITY_SERVICE_UIID))
                ?.getCharacteristic(UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID3))

            characteristic1?.let { enableNotification(it) }
            characteristic2?.let { enableNotification(it) }
            characteristic3?.let { enableNotification(it) }
        }

        /*override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic){
                when(uuid){
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        //XX XX XX XX XX XX
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos: $dataValue")
                        val multiplicator = if(value.first().toInt()> 0) -1 else 1
                        val temperature = value[1].toInt() + value[2].toInt() / 10f
                        val humidity = value[4].toInt() + value[5].toInt() / 10f
                        val bps=0f
                        val tempHumidityResult = SensoresResultado(
                            dataValue.toFloat(),
                            humidity,bps,
                            EstadoDeConeccion.Connected
                        )
                        coroutineScope.launch {
                            data.emit(
                                Recursos.Success(data = tempHumidityResult)
                            )
                        }
                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos (Characteristics 2): $dataValue")
                        // Manejar los datos de la segunda característica aquí
                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID3) -> {
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos (Characteristics 3): $dataValue")
                        // Manejar los datos de la tercera característica aquí
                    }
                    else -> Unit
                }
            }
        }*/

        /*override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic){
                when(uuid){
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos (Characteristics 1): $dataValue")
                        // Manejar los datos de la primera característica aquí


                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID2) -> {
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos (Characteristics 2): $dataValue")
                        // Manejar los datos de la segunda característica aquí
                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID3) -> {
                        val dataValue = String(value) // Convierte los bytes a una cadena
                        Log.d("MyApp", "Datos recibidos (Characteristics 3): $dataValue")
                        // Manejar los datos de la tercera característica aquí

                    }
                    else -> Unit
                }
            }
        }*/

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            var dataValue1: String? = null
            var dataValue2: String? = null
            var dataValue3: String? = null

            with(characteristic){
                when(uuid){
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        dataValue2 = String(value) // Convierte los bytes a una cadena
                        val temperaturaResult = Sensor1Result(
                            dataValue2!!.toFloat(),
                            EstadoDeConeccion.Connected
                        )
                        coroutineScope.launch {
                            data.emit(
                                Recursos.Success(data = temperaturaResult)
                            )
                        }

                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID2) -> {
                        dataValue1 = String(value) // Convierte los bytes a una cadena
                        val bpsResult = Sensor2Result(
                            dataValue1!!.toFloat(),
                            EstadoDeConeccion.Connected
                        )
                        coroutineScope.launch {
                            data2.emit(
                                Recursos.Success(data = bpsResult)
                            )

                        }
                    }
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID3) -> {
                        dataValue3 = String(value) // Convierte los bytes a una cadena
                        val acelerometroResult = Sensor3Result(
                            dataValue3!!.toFloat(),
                            EstadoDeConeccion.Connected
                        )
                        coroutineScope.launch {
                            data3.emit(
                                Recursos.Success(data = acelerometroResult)
                            )

                        }
                    }
                    else -> Unit
                }
            }


        }

    }



    private fun enableNotification(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic, true) == false){
                Log.d("BLEReceiveManager","set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray){
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    private fun findCharacteristics(serviceUUID: String, characteristicsUUID:String):BluetoothGattCharacteristic?{
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Recursos.Loading(message = "Scanning Ble devices..."))
        }
        isScanning = true
        bleScanner.startScan(null,scanSettings,scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }



    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,false) == false){
                Log.d("TempHumidReceiveManager","set charateristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}