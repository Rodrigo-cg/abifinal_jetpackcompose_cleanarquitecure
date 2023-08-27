package com.abi.abifinal.data.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.abi.abifinal.core.Constants
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.repository.UsersRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
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
) : UsersRepository {

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
            usersRef.document(user.id).update(map).await()
            Response.Succes(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Faliure(e)
        }
    }

    override suspend fun saveImage(file: File): Response<String> {
        return try {

            val fromFile = Uri.fromFile(file)
            val ref = storageUsersRef.child(file.name)
            val uploadTask = ref.putFile(fromFile).await()
            val url = ref.downloadUrl.await()
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

    override suspend fun getParametersBle(ble: BLE): Response<Boolean> {
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


    override suspend fun getGpsRealTime(): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMsmSos(): Response<Boolean> {
        TODO("Not yet implemented")
    }


}