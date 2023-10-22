package com.abi.abifinal.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import com.abi.abifinal.core.Constants
import com.abi.abifinal.data.location.hasLocationPermission
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.UsersRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.chromium.base.ThreadUtils.runOnUiThread
import quevedo.soares.leandro.blemadeeasy.BLE
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class UsersRepositoryImpl @Inject constructor(
    @Named(Constants.USERS) private val usersRef: CollectionReference,
    @Named(Constants.USERS) private val storageUsersRef: StorageReference,
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
    private val context: Context
) : UsersRepository {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    ///Sin inyección de dependencias
    /*val firestore = Firebase.firestore
    val usersRef = firestore.collection("Users")*/

    override suspend fun create(user: User): Response<Boolean> {
        return try {
            user.password = ""
            usersRef.document(user.id).set(user).await()
            Response.Succes(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Faliure(e)
        }
    }

    override suspend fun update(user: User): Response<Boolean> {
        return try {
            val map: MutableMap<String, Any> = HashMap()
            map["username"] = user.username
            map["image"] = user.image
            map["phoneNumber"]=user.phoneNumber
            usersRef.document(user.id).update(map).await()
            Response.Succes(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Faliure(e)
        }
    }

    override suspend fun saveImage(file: File): Response<String> {
        return try {
            if(file.toString().contains("firebase")){
                return Response.Succes(file.toString())

            }
            val fromFile = Uri.fromFile(file)
            val ref = storageUsersRef.child(file.name)
            val uploadTask = ref.putFile(fromFile).await()
            val url = ref.downloadUrl.await()
            Log.d("url", "Datos recibidos DE url: $url")

            return Response.Succes(url.toString())

        } catch (e: Exception) {

            e.printStackTrace()
            Response.Faliure(e)

        }
    }

    override fun getUserById(id: String): Flow<User> = callbackFlow {
        val snapshotListener = usersRef.document(id).addSnapshotListener { snapshot, e ->

            val user = snapshot?.toObject(User::class.java) ?: User()
            trySend(user)

        }

        awaitClose {
            snapshotListener.remove()
        }
    }


    override suspend fun getParametersBle(componentActivity: ComponentActivity): Response<Boolean> {
        var ble=BLE(componentActivity =componentActivity)

        GlobalScope.launch {
            // You can specify filters for your device, being them 'macAddress', 'service' and 'name'
            val connection = ble?.scanFor(
                // You only need to supply one of these, no need for all of them!
                macAddress = "0C:B8:15:F2:D0:BE",
                name = "ESP32BLE",
                service = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"
            )
            runOnUiThread {
                //Toast.makeText(context, "funciona toast", Toast.LENGTH_SHORT).show()
            }
            // And it will automatically connect to your device, no need to boilerplate
            if (connection != null) {
                // And you can continue with your code
                ble?.stopScan()
                runOnUiThread {
                    //  Toast.makeText(context, "Conexión establecida", Toast.LENGTH_SHORT).show()
                }
                // For watching strings
                connection.observeString(characteristic = "6e400003-b5a3-f393-e0a9-e50e24dcca9e", charset = Charsets.UTF_8) { value: String ->
                    //Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                }
                connection.observeString(characteristic = "6e400004-b5a3-f393-e0a9-e50e24dcca9e", charset = Charsets.UTF_8) { value: String ->
                    // Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                }

            } else {
                runOnUiThread {
                    //Toast.makeText(context, "Dispositivo no disponible", Toast.LENGTH_SHORT).show()
                }
                // Show an Alert or UI with your preferred error message about the device not being available
            }
        }
        return Response.Succes(true)
    }




    override suspend fun sendMsmSos(): Response<Boolean> {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLocationUpdates(interval:Long): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()){
                throw UsersRepository.LocationException("Mission location permission")
            }
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && !isNetworkEnabled) {
                throw UsersRepository.LocationException("GPS is disabled")
            }

            val request = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            locationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

}