 package co.edu.eam.lugaresapp.ui.places

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel
import java.util.UUID

/**
 * PANTALLA DE CREACIÓN DE LUGAR
 * 
 * Permite a los usuarios crear nuevos lugares en la plataforma.
 * 
 * CARACTERÍSTICAS:
 * - Validación de campos obligatorios
 * - Verificación de sesión activa
 * - Asignación automática de ownerId desde SessionManager
 * - Lugares creados entran en estado approved=false (moderación pendiente)
 * - Feedback visual mediante Toast
 * 
 * FLUJO:
 * 1. Usuario llena formulario
 * 2. Sistema valida campos y sesión
 * 3. Si OK: crea Place con ownerId y approved=false
 * 4. Navega de regreso tras creación exitosa
 * 
 * @param placesViewModel ViewModel para gestión de lugares
 * @param usersViewModel ViewModel de usuarios (disponible para futuras validaciones)
 * @param onNavigateBack Callback de navegación hacia atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceScreen(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    onNavigateBack: () -> Unit
) {
    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PlaceType.RESTAURANT) }
    var expandedDropdown by remember { mutableStateOf(false) }
    
    // Gestión de sesión
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título
            Text(
                text = stringResource(id = R.string.place_create_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Campo: Nombre del lugar
            InputText(
                label = stringResource(id = R.string.place_name),
                supportingText = "* Obligatorio",
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            // Campo: Descripción
            InputText(
                label = stringResource(id = R.string.place_description),
                supportingText = "* Mínimo 10 caracteres",
                value = description,
                onValueChange = { description = it },
                onValidate = { it.length < 10 }
            )

            // Campo: Dirección
            InputText(
                label = "Dirección",
                supportingText = "* Obligatorio",
                value = address,
                onValueChange = { address = it },
                onValidate = { it.isBlank() }
            )

            // Campo: Teléfono
            InputText(
                label = "Teléfono",
                supportingText = "Número de contacto",
                value = phone,
                onValueChange = { phone = it },
                onValidate = { false } // Opcional
            )

            // Dropdown: Categoría
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.place_category)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    PlaceType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                selectedType = type
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón: Crear Lugar
            Button(
                onClick = {
                    // ==================== VALIDACIONES ====================
                    
                    // Validar campos obligatorios
                    if (name.isBlank()) {
                        Toast.makeText(
                            context,
                            "El nombre del lugar es obligatorio",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    
                    if (description.length < 10) {
                        Toast.makeText(
                            context,
                            "La descripción debe tener al menos 10 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    
                    if (address.isBlank()) {
                        Toast.makeText(
                            context,
                            "La dirección es obligatoria",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    
                    // Validar sesión activa
                    val currentUserId = sessionManager.getUserId()
                    if (currentUserId == null) {
                        Toast.makeText(
                            context,
                            "Debes iniciar sesión para crear un lugar",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Button
                    }
                    
                    // ==================== CREAR LUGAR ====================
                    
                    val newPlace = Place(
                        id = UUID.randomUUID().toString(),
                        title = name.trim(),
                        description = description.trim(),
                        address = address.trim(),
                        location = Location(0.0, 0.0), // Temporal - Se actualizará en versiones futuras
                        images = listOf("https://via.placeholder.com/300x200?text=Lugar"), // Placeholder
                        phones = if (phone.isNotBlank()) listOf(phone.trim()) else emptyList(),
                        type = selectedType,
                        schedules = emptyList(), // Se implementará en versiones futuras
                        approved = false, // Requiere moderación
                        ownerId = currentUserId,
                        createdAt = System.currentTimeMillis()
                    )
                    
                    // Agregar lugar al ViewModel
                    placesViewModel.addPlace(newPlace)
                    
                    // Feedback exitoso
                    Toast.makeText(
                        context,
                        "Lugar creado. Pendiente de aprobación",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Navegar de regreso
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.place_create_button))
            }
        }
    }
}