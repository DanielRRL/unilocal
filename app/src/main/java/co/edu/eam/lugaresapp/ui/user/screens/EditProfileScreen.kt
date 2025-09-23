package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

@Composable
fun EditProfileScreen(
    usersViewModel: UsersViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // You can prefill fields from usersViewModel.currentUser if available

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            InputText(
                label = stringResource(id = R.string.profile_name),
                supportingText = "",
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(
                label = stringResource(id = R.string.profile_lastname),
                supportingText = "",
                value = lastname,
                onValueChange = { lastname = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(
                label = stringResource(id = R.string.profile_email),
                supportingText = stringResource(id = R.string.txt_email_error),
                value = email,
                onValueChange = { email = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Implement update logic */ onNavigateBack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.profile_update_button))
            }
        }
    }
}