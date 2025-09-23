package co.edu.eam.lugaresapp.ui.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

@Composable
fun RegisterScreen(
    usersViewModel: UsersViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre
            InputText(
                label = stringResource(id = R.string.register_name),
                supportingText = stringResource(id = R.string.txt_required_field),
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Apellido
            InputText(
                label = stringResource(id = R.string.register_lastname),
                supportingText = stringResource(id = R.string.txt_required_field),
                value = lastname,
                onValueChange = { lastname = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Correo
            InputText(
                label = stringResource(id = R.string.register_email),
                supportingText = stringResource(id = R.string.txt_email_error),
                value = email,
                onValueChange = { email = it },
                onValidate = { it.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(it).matches() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Teléfono
            InputText(
                label = stringResource(id = R.string.register_phone),
                supportingText = stringResource(id = R.string.txt_required_field),
                value = phone,
                onValueChange = { phone = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Contraseña
            InputText(
                label = stringResource(id = R.string.register_password),
                supportingText = stringResource(id = R.string.txt_password_error),
                value = password,
                onValueChange = { password = it },
                onValidate = { it.isBlank() || it.length < 5 },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Confirmar contraseña
            InputText(
                label = stringResource(id = R.string.register_confirm_password),
                supportingText = stringResource(id = R.string.txt_required_field),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                onValidate = { it.isBlank() || confirmPassword != password },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    // Simple validation
                    if (name.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val created = usersViewModel.createUser(name, lastname, email, phone, password)
                    if (created) {
                        onNavigateToHome()
                    } else {
                        Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.register_button))
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { onNavigateToLogin() }) {
                Text(text = stringResource(id = R.string.login_button))
            }

        }
    }
}