package com.abi.abifinal.presentation.screens.medicamentos_edit

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import com.abi.abifinal.presentation.screens.profile_edit.ProfileEditState
import com.abi.abifinal.presentation.utils.ComposeFileProvider
import com.abi.abifinal.presentation.utils.ResultingActivityHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MedicamentosEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersUseCases: UsersUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var state by mutableStateOf(ProfileEditState())
        private set
    var isEnableButton: Boolean = false


    val data = savedStateHandle.get<String>("user")
    val user = User.fromJson(data!!)
    /////Response Upload Username
    var updateResponse by mutableStateOf<Response<Boolean>?>(null)
        private set
    /////Response Save Image
    var saveImageResponse by mutableStateOf<Response<String>?>(null)
        private set
    //IMAGES
    //var imagesUri by mutableStateOf("")
    //var hasImage by mutableStateOf<Boolean>(false)
    var file: File? = null

    init {
        state = state.copy(username = user.username, image = user.image, phoneNumber = user.phoneNumber)
        file= File(state.image)
        Log.d("filechangeinit", "Datos recibidos DE BFILE: $file")

    }

    fun saveImage() = viewModelScope.launch {

        if (file != null){
            saveImageResponse = Response.Loading
            val result = usersUseCases.saveImage(file!!)
            saveImageResponse = result
        }
    }

    /*FUNCIONES PARA ACCEDER SOLO A LA CAMARA O GALERIA
    fun onGalleryResult(uri: Uri?){
        hasImage = uri != null
        imagesUri = uri

    }
    fun onCameraResult(result: Boolean){
        hasImage = result
    }*/

    val resultingActivityHandler = ResultingActivityHandler()
    fun pickImage() = viewModelScope.launch {
        val result = resultingActivityHandler.getContent("image/*")
        if (result != null){
            file = ComposeFileProvider.createFileFromUri(context, result)
            state = state.copy(image = result.toString())
            Log.d("FILEPICKIMAGE", "Datos recibidos DE PICKIMAGE: $file")

        }

    }
    fun takePhoto() = viewModelScope.launch {
        val result = resultingActivityHandler.takePicturePreview()
        if (result != null){
            state = state.copy(image = ComposeFileProvider.getPathFromBitmap(context, result))
            file = File(state.image)
            Log.d("TAKEPHOTO", "Datos recibidos DE TAKEPHOTO: $file")

        }
    }

    var isUsernameValid by mutableStateOf(false)
        private set
    var usernameErrMsg by mutableStateOf("")
        private set
    var isPhoneNumberValid by mutableStateOf(false)
        private set
    var phoneNumberErrMsg by mutableStateOf("")
        private set
    fun onUsernameInput(username: String) {
        state = state.copy(username = username)

    }
    fun onPhoneNumberInput(phoneNumber: String) {
        state = state.copy(phoneNumber = phoneNumber)
    }

    fun validateUsername() {
        if (state.username.length >= 5) {
            isUsernameValid = true
            usernameErrMsg = ""
        } else {
            isUsernameValid = false
            usernameErrMsg = "Al menos 5 caracteres"
        }
        enableButton()
    }
    fun validatePhoneNumber() {
        val phoneNumber = state.phoneNumber

        if (phoneNumber.isEmpty()) {
            isPhoneNumberValid = false
            phoneNumberErrMsg = "El número de teléfono es requerido"
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            isPhoneNumberValid = false
            phoneNumberErrMsg = "Por favor ingresa un número de teléfono válido"
        } else {
            isPhoneNumberValid = true
            phoneNumberErrMsg = ""
        }

        enableButton()
    }

    fun enableButton() {
        isEnableButton =
            isUsernameValid && isPhoneNumberValid
    }

    fun onUpdate(url: String){
        val myUser = User(
            id = user.id,
            username = state.username,
            image = url,
            phoneNumber = state.phoneNumber

        )
        update(myUser)
    }

    fun update (user: User) = viewModelScope.launch {
        updateResponse = Response.Loading
        val result = usersUseCases.update(user)
        updateResponse = result
    }

}