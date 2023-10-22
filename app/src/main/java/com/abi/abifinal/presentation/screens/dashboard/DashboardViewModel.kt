package com.abi.abifinal.presentation.screens.dashboard

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.abifinal.data.bleresults.EstadoDeConeccion
import com.abi.abifinal.data.bleresults.SensoresRecevidorManager
import com.abi.abifinal.data.location.LocationResultGps
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.use_cases.auth.AuthUseCase
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import com.abi.abifinal.presentation.utils.Recursos
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel  @Inject constructor(
    private val authUseCase: AuthUseCase,
    val usersUseCases: UsersUseCases,
    private val temperatureAndHumidityReceiveManager: SensoresRecevidorManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : ViewModel(){
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var smsSendingJob: Job? = null
    private val database = FirebaseDatabase.getInstance()
    private val locationReference = database.getReference("locations")
    var userData by mutableStateOf(User())
        private set
    private fun getUserById( )= viewModelScope.launch {
        usersUseCases.getUserById(authUseCase.getCurrentUser()!!.uid).collect(){user ->
            userData = user
            Log.d("Numero telefono", "telefono: $userData")

        }
    }

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var temperature by mutableStateOf(0f)
        private set

    var acelerometro by mutableStateOf(1f)
        private set

    var bps by mutableStateOf(5f)
        private set

    val valorCriticoTemp=100
    val valorCriticoBps=100
    val valorCriticoAcelerometro=1.5

    var connectionState by mutableStateOf<EstadoDeConeccion>(EstadoDeConeccion.Uninitialized)


    private fun subscribeToChanges(){
        viewModelScope.launch {
            temperatureAndHumidityReceiveManager.data.collect{ result ->
                when(result){
                    is Recursos.Success -> {
                        connectionState = result.data.connectionState
                        temperature = result.data.temperature
                        //Log.d("ViewModelChange", "Datos recibidos : $temperature")
                        if (temperature > valorCriticoTemp) {
                            iniciarEnvioMensajesSMS(userData.phoneNumber, "Temperatura crítica: $temperature")
                        }
                    }

                    is Recursos.Loading -> {
                        initializingMessage = result.message
                        connectionState = EstadoDeConeccion.CurrentlyInitializing
                    }

                    is Recursos.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = EstadoDeConeccion.Uninitialized
                    }

                    else -> {}
                }
            }
        }
    }
    private fun subscribeToChanges2(){
        viewModelScope.launch {
            temperatureAndHumidityReceiveManager.data2.collect{ result ->
                when(result){
                    is Recursos.Success -> {
                        connectionState = result.data.connectionState
                        bps = result.data.bps
                        //bps = 8f
                        //Log.d("phoneNumber", "Datos recibidos DE phoneNumber: ${userData.phoneNumber}")
                        if (bps > valorCriticoBps) {
                            iniciarEnvioMensajesSMS(userData.phoneNumber, "Bps crítico: $temperature")
                        }
                    }

                    is Recursos.Loading -> {
                        initializingMessage = result.message
                        connectionState = EstadoDeConeccion.CurrentlyInitializing
                    }

                    is Recursos.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = EstadoDeConeccion.Uninitialized
                    }

                    else -> {}
                }
            }

        }
    }
    private fun subscribeToChanges3(){
        viewModelScope.launch {
            temperatureAndHumidityReceiveManager.data3.collect{ result ->
                when(result){
                    is Recursos.Success -> {
                        connectionState = result.data.connectionState
                        acelerometro = result.data.acelerometro
                        //bps = 8f
//                        Log.d("ViewModelChange", "Datos recibidos DE BPS: $bps")

                        if (acelerometro > valorCriticoAcelerometro) {
                            iniciarEnvioMensajesSMS(userData.phoneNumber, "Acelerometro crítico: $acelerometro")
                        }

                    }

                    is Recursos.Loading -> {
                        initializingMessage = result.message
                        connectionState = EstadoDeConeccion.CurrentlyInitializing
                    }

                    is Recursos.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = EstadoDeConeccion.Uninitialized
                    }

                    else -> {}
                }
            }

        }
    }
        fun disconnect(){
        temperatureAndHumidityReceiveManager.disconnect()
    }

    fun reconnect(){
        temperatureAndHumidityReceiveManager.reconnect()
    }

    fun initializeConnection(){
        errorMessage = null
        getUserById()

        subscribeToChanges()
        subscribeToChanges2()
        subscribeToChanges3()
        getCurrentLocation()

        temperatureAndHumidityReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        temperatureAndHumidityReceiveManager.closeConnection()
        smsSendingJob?.cancel()
    }

    private fun enviarMensajeSMS(numeroDestino: String, mensaje: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(numeroDestino, null, mensaje, null, null)
        //Log.d("SMS", "Mensaje enviado")
    }
    private fun debeEnviarMensajeSMS(): Boolean {
        // Implementa lógica aquí para determinar si es necesario enviar el mensaje SMS.
        // Por ejemplo, puedes verificar si los valores están dentro de un rango crítico nuevamente aquí.

        return temperature > 100 || acelerometro > 1.5 || bps > 100
    }
    private fun iniciarEnvioMensajesSMS(numeroDestino: String, mensaje: String) {
        smsSendingJob?.cancel() // Cancela la corutina anterior si está en curso

        smsSendingJob = viewModelScope.launch {
            while (true) {
                if (debeEnviarMensajeSMS()) {
                    enviarMensajeSMS(numeroDestino, mensaje)
                }

                // Espera 60 segundos antes de verificar nuevamente
                delay(60000)
            }
        }
    }

     fun getCurrentLocation() {

         viewModelScope.launch {
             var location = usersUseCases.getCurrentUserLocationUseCase.getLocationUpdates(10000L)        .catch { e -> e.printStackTrace() }
                 .onEach { location ->
                     //val lat = location.latitude.toString().takeLast(3)
                     val lat = location.latitude.toString()
                     val long = location.longitude.toString()
                     Log.d("GPS", "latitud: $lat ; longitud: $long  ")
                 }
                 .launchIn(serviceScope)
         }


    }
}