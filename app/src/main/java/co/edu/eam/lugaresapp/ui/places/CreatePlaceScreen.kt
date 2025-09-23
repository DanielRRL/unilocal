package co.edu.eam.lugaresapp.ui.places

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

@Composable
fun CreatePlaceScreen(
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            InputText(
                label = stringResource(id = R.string.place_name),
                supportingText = "",
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(
                label = stringResource(id = R.string.place_description),
                supportingText = "",
                value = description,
                onValueChange = { description = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputText(
                label = stringResource(id = R.string.place_category),
                supportingText = "",
                value = category,
                onValueChange = { category = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Implement create place logic */ onNavigateBack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.place_create_button))
            }
        }
    }
}