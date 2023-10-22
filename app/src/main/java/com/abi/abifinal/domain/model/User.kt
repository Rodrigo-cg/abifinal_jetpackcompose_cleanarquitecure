package com.abi.abifinal.domain.model

import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class User (
    var id: String ="",
    var username: String = "",
    var email: String= "",
    var password: String= "",
    var image: String = "",
    var age: String = "",
    var phoneNumber: String = "",
    var fullName: String = "",
    var dni: String="",
    var numerocontacto1: String="",
    var numerocontacto2: String="",
 //Sensores
    var temperature: String="",
    var position:String="",
    var msm:String="",
    var bpm:String=""
    ){
    fun toJson2(): String = Gson().toJson(User(
        id,
        username,
        email,
        password,
        age,
        phoneNumber,
        fullName,
        dni,

        temperature,
        position,
        msm,
        bpm,

        if (image != "") URLEncoder.encode(image, StandardCharsets.UTF_8.toString()) else ""

    ))
    fun toJson(): String = Gson().toJson(User(
        id,
        username,
        email,
        password,
        if (image != "") URLEncoder.encode(image, StandardCharsets.UTF_8.toString()) else ""

    ))

    companion object{
        fun fromJson(data: String): User = Gson().fromJson(data, User::class.java)
    }

}