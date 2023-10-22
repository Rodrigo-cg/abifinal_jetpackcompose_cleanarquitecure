package com.abi.abifinal.presentation.screens.medicamentos_edit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abi.abifinal.R
import com.abi.abifinal.presentation.components.DefaultButton
import com.abi.abifinal.presentation.components.DefaultTextField
import com.abi.abifinal.presentation.components.DialogCapturePicture
import com.abi.abifinal.presentation.screens.medicamentos_edit.MedicamentosEditViewModel
import com.abi.abifinal.presentation.screens.profile_edit.ProfileEditViewModel
import com.abi.abifinal.presentation.ui.theme.DarkGray500
import com.abi.abifinal.presentation.ui.theme.Pink500

@Composable
fun MedicamentosEditContent(navController: NavController, viewModel: MedicamentosEditViewModel = hiltViewModel()) {

    //val context = LocalContext.current
    val state = viewModel.state
    viewModel.resultingActivityHandler.handle()

    var dialogState = remember{ mutableStateOf(false) }
    DialogCapturePicture(
        status = dialogState,
        takePhoto = { viewModel.takePhoto() },
        pickImage = {viewModel.pickImage()}
    )

    /**************
    val imagePicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
    onResult = { uri ->
    uri?.let { viewModel.onGalleryResult(it) }
    }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(),
    onResult = {
    viewModel.onCameraResult(it)
    }
    )
     ***************/

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .background(Pink500),
        ) {
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkGray500),
            modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 150.dp)
        ) {

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text(
                    text = "Complete los campos requeridos para generar horarios para que tome sus pastillas",
                    modifier = Modifier.padding(top = 40.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Ingresa tus nuevos datos",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.username,
                    onValueChange = { viewModel.onUsernameInput(it) },
                    label = "Descripcion de pastillas y/o tratamiento",
                    icon = Icons.Default.Create,
                    keyboardType = KeyboardType.Text,
                    errorMessage = viewModel.usernameErrMsg,
                    validateField = {
                        viewModel.validateUsername()
                    }
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberInput(it) },
                    label = "Digite el numero de dias que tomara pastillas",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    errorMessage = viewModel.phoneNumberErrMsg,
                    validateField = {
                        viewModel.validatePhoneNumber()
                    }
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberInput(it) },
                    label = "Digite cuantas veces repetira la pastilla durante el dia",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    errorMessage = viewModel.phoneNumberErrMsg,
                    validateField = {
                        viewModel.validatePhoneNumber()
                    }
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberInput(it) },
                    label = "Repeticiones de pastillas por dia",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    errorMessage = viewModel.phoneNumberErrMsg,
                    validateField = {
                        viewModel.validatePhoneNumber()
                    }
                )

                DefaultButton(
                    textButton = "Actualizar",
                    onClick = {
                        viewModel.saveImage()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 40.dp),
                    colorIcon = Color.White,
                    icono = Icons.Default.Edit
                )

            }

        }

    }

}