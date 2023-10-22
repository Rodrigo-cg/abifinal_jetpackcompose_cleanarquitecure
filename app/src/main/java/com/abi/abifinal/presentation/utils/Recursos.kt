package com.abi.abifinal.presentation.utils

import android.location.Location
import kotlinx.coroutines.flow.Flow

sealed class Recursos<out T:Any>{
    data class Success<out T:Any> (val data:T):Recursos<T>()
    data class Error(val errorMessage:String):Recursos<Nothing>()
    data class Loading<out T:Any>(val data:T? = null, val message:String? = null):Recursos<T>()

    data class Error2(val exception: Exception) : Recursos<Nothing>()  //*


}
