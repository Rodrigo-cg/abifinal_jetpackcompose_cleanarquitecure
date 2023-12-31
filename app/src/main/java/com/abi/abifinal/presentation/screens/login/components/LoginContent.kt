package com.abi.abifinal.presentation.screens.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abi.abifinal.R
import com.abi.abifinal.presentation.components.DefaultButton
import com.abi.abifinal.presentation.components.DefaultTextField
import com.abi.abifinal.presentation.screens.login.LoginViewModel
import com.abi.abifinal.presentation.ui.theme.AbifinalTheme
import com.abi.abifinal.presentation.ui.theme.DarkGray500
import com.abi.abifinal.presentation.ui.theme.Pink500

@Composable
fun LoginContent(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    //var context = LocalContext.current
    //val loginFlow = viewModel.loginFlow.collectAsState()

    val state = viewModel.state

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Pink500),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(top = 30.dp)
                        .padding(bottom = 30.dp),
                    painter = painterResource(id = R.drawable.control),
                    contentDescription = "Control de Xbox 360"
                )

                Text(text = "Hola, soy ABI y estoy lista para ayudarte !")
            }

        }

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkGray500),
            modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 280.dp)
        ) {

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Text(
                    text = "LOGIN",
                    modifier = Modifier.padding(top = 40.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Por favor inicia sesión para continuar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 25.dp),
                    value = state.email,
                    onValueChange = { viewModel.onEmailInput(it) },
                    label = "E-Mail",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    errorMessage = viewModel.emailErrorMessage,
                    validateField = {
                        viewModel.validateEmail()
                    }
                )

                DefaultTextField(
                    modifier = Modifier.padding(top = 5.dp),
                    value = state.password,
                    onValueChange = { viewModel.onPasswordInput(it) },
                    label = "Contraseña",
                    icon = Icons.Default.Lock,
                    hideText = true,
                    keyboardType = KeyboardType.Password,
                    errorMessage = viewModel.passwordErrorMessage,
                    validateField = {
                        viewModel.validatePassword()
                    }
                )

                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 45.dp),
                    textButton = "Iniciar Sesión",
                    onClick = {

                        viewModel.login()

                    },
                    enable = viewModel.isEnableLoginButton,
                    colorIcon = Color.White
                )

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AbifinalTheme(darkTheme = true) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginContent(rememberNavController())
        }
    }
}