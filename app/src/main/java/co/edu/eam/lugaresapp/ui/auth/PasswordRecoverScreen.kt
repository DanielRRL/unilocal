package co.edu.eam.lugaresapp.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.components.InputText

@Composable
fun PasswordRecoverScreen(
    onNavigateBack: () -> Unit
) {
    var method by remember { mutableStateOf("email") }
    var input by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.recover_title))

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .selectable(selected = method == "email", onClick = { method = "email" })
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = method == "email", onClick = { method = "email" })
                    Text(text = stringResource(id = R.string.recover_method_email))
                }

                Row(
                    modifier = Modifier
                        .selectable(selected = method == "phone", onClick = { method = "phone" }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = method == "phone", onClick = { method = "phone" })
                    Text(text = stringResource(id = R.string.recover_method_phone))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            InputText(
                label = stringResource(id = R.string.recover_input_hint),
                supportingText = "",
                value = input,
                onValueChange = { input = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Implement recovery: send email or sms */ onNavigateBack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.recover_button))
            }
        }
    }
}