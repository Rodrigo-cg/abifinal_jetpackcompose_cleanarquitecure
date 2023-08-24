package com.abi.abifinal.presentation.screens.singup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abi.abifinal.domain.model.Response
import com.abi.abifinal.domain.model.User
import com.abi.abifinal.domain.use_cases.auth.AuthUseCase
import com.abi.abifinal.domain.use_cases.users.UsersUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingupViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val usersUseCases: UsersUseCases
) : ViewModel() {

    var state by mutableStateOf(SingUpState(age = "",))
        private set

    var isUsernameValid by mutableStateOf(false)
        private set
    var usernameErrMsg by mutableStateOf("")
        private set

    var isConfirmPasswordValid by mutableStateOf(false)
        private set
    var confirmPasswordErrMsg by mutableStateOf("")
        private set

    var isEmailValid by mutableStateOf(false)
        private set
    var emailErrorMessage by mutableStateOf("")
        private set

    var isPasswordValid by mutableStateOf(false)
        private set
    var passwordErrorMessage by mutableStateOf("")
        private set

    var isPhoneNumberValid by mutableStateOf(false)
        private set
    var phoneNumberErrMsg by mutableStateOf("")
        private set

    var isFullNameValid by mutableStateOf(false)
        private set
    var fullNameErrMsg by mutableStateOf("")
        private set

    var isAgeValid by mutableStateOf(false)
        private set
    var ageErrMsg by mutableStateOf("")
        private set

    var isDniValid by mutableStateOf(false)
        private set
    var dniErrMsg by mutableStateOf("")
        private set

    var user = User()

    fun onEmailInput(email: String){
        state = state.copy(email = email)
    }
    fun onUsernameInput(username: String){
        state = state.copy(username = username)

    }
    fun onPasswordInput(password: String){
        state = state.copy(passwoord = password)
    }
    fun onConfirmPasswordInput(passwordConfirm: String){
        state = state.copy(confirmPassword = passwordConfirm)
    }
    fun onPhoneNumberInput(phoneNumber: String) {
        state = state.copy(phoneNumber = phoneNumber)
    }

    fun onFullNameInput(fullName: String) {
        state = state.copy(fullName = fullName)
    }

    fun onAgeInput(age: String) {
        state = state.copy(age = age)
    }
    fun onDniInput(dni: String) {
        state = state.copy(dni = dni)
    }

    //Boton
    var isEnableButton: Boolean = false

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

    fun validateFullName() {
        val fullName = state.fullName

        if (fullName.isEmpty()) {
            isFullNameValid = false
            fullNameErrMsg = "El nombre completo es requerido"
        } else if (fullName.length < 2) {
            isFullNameValid = false
            fullNameErrMsg = "Por favor ingresa un nombre válido"
        } else {
            isFullNameValid = true
            fullNameErrMsg = ""
        }

        enableButton()
    }
    fun validateAge() {
        val age = state.age

        if (age.isEmpty()) {
            ageErrMsg = "La edad es requerida"
        } else {
            val ageValue = age.toIntOrNull()
            if (ageValue == null || ageValue < 0 || ageValue > 120) {
                isAgeValid=false
                ageErrMsg = "Por favor ingresa una edad válida"
            } else {
                isAgeValid=true
                ageErrMsg = ""
            }
        }

        enableButton()
    }
    fun validateDni() {
        val dni = state.dni

        if (dni.isEmpty()) {
            ageErrMsg = "El dni es requerida"
        } else {
            if (dni.length==9) {
                isDniValid= true
                dniErrMsg = ""
            } else {
                isDniValid= false

                dniErrMsg = "Por favor ingresa un dni válida"

            }
        }

        enableButton()
    }
    fun enableButton() {
        isEnableButton =
            isEmailValid && isPasswordValid && isUsernameValid && isConfirmPasswordValid
    }

    fun validateConfirmPassword() {
        if (state.passwoord == state.confirmPassword) {
            isConfirmPasswordValid = true
            confirmPasswordErrMsg = ""
        } else {
            isConfirmPasswordValid = false
            confirmPasswordErrMsg = "Las contraseñas no coiinciden"
        }

        enableButton()
    }

    fun validateUsername() {
        if (state.username.length >= 5) {
            isUsernameValid = true
            usernameErrMsg= ""
        } else {
            isUsernameValid = false
            usernameErrMsg = "Al menos 5 caracteres"
        }
        enableButton()
    }


    fun onSingUp() {
        user.username = state.username
        user.email = state.email
        user.password = state.passwoord


        singUp(user)

    }

    fun createUser() = viewModelScope.launch {
        user.id = authUseCase.getCurrentUser()!!.uid
        usersUseCases.create(user)
    }

    var singupResponse by mutableStateOf<Response<FirebaseUser>?>(null)
        private set

    fun singUp(user: User) = viewModelScope.launch {
        singupResponse = Response.Loading
        val result = authUseCase.singUp(user)
        singupResponse = result
    }

    fun validateEmail() {
        //Valida el correo
        if (Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            isEmailValid = true
            emailErrorMessage = ""
        } else {
            isEmailValid = false
            emailErrorMessage = "El email no es válido"
        }
        enableButton()
    }

    fun validatePassword() {
        if (state.passwoord.length >= 6) {
            isPasswordValid = true
            passwordErrorMessage= ""
        } else {
            isPasswordValid = false
            passwordErrorMessage = "Se necesitan al menos 6 caracteres"
        }
        enableButton()
    }


}