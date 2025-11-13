@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.model.Schedule
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel
import java.time.LocalTime
import java.util.UUID

/**
 * PANTALLA DE CREACIÓN DE LUGAR - Formulario completo
 * 
 * Permite a los usuarios crear nuevos lugares con toda la información requerida.
 * 
 * CARACTERÍSTICAS IMPLEMENTADAS:
 * - Campos de información básica (nombre, descripción)
 * - Categoría del lugar (dropdown)
 * - Ubicación (dirección, ciudad, departamento)
 * - Mapa interactivo para seleccionar coordenadas GPS
 * - Horarios de atención (día, hora apertura, hora cierre)
 * - Teléfonos de contacto
 * - Validación de campos obligatorios
 * - Verificación de sesión activa
 * - Asignación automática de ownerId
 * - Estado de moderación (approved=false)
 * 
 * FLUJO:
 * 1. Usuario completa formulario con todos los campos
 * 2. Sistema valida campos obligatorios y sesión
 * 3. Si OK: crea Place con toda la información
 * 4. Lugar queda pendiente de aprobación (approved=false)
 * 5. Navega de regreso tras creación exitosa
 * 
 * @param placesViewModel ViewModel para gestión de lugares
 * @param usersViewModel ViewModel de usuarios
 * @param onNavigateBack Callback de navegación hacia atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceScreen(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    onNavigateBack: () -> Unit
) {
    /**
     * ESTADOS DEL FORMULARIO - Información básica
     */
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PlaceType.RESTAURANT) }
    var expandedDropdown by remember { mutableStateOf(false) }
    
    /**
     * ESTADOS DEL FORMULARIO - Ubicación
     */
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var latitude by remember { mutableDoubleStateOf(4.4687891) } // Centro de Armenia
    var longitude by remember { mutableDoubleStateOf(-75.6491181) }
    
    /**
     * ESTADOS DEL FORMULARIO - Horarios
     */
    var selectedDay by remember { mutableStateOf("Lunes") }
    var openTime by remember { mutableStateOf("08:00") }
    var closeTime by remember { mutableStateOf("18:00") }
    var schedulesList by remember { mutableStateOf<List<Schedule>>(emptyList()) }
    
    /**
     * ESTADOS DEL FORMULARIO - Contacto
     */
    var phone by remember { mutableStateOf("") }
    
    /**
     * Gestión de sesión
     */
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /**
             * TÍTULO
             */
            Text(
                text = "Formulario de registro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            /**
             * SECCIÓN: INFORMACIÓN BÁSICA
             */
            Text(
                text = "Información básica",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Campo: Nombre del lugar
            InputText(
                label = "Nombre",
                supportingText = "* Obligatorio",
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            // Dropdown: Categoría
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría *") },
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
                            text = { Text(type.displayName) },
                            onClick = {
                                selectedType = type
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            // Campo: Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción *") },
                supportingText = { Text("Mínimo 10 caracteres") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Divider()

            /**
             * SECCIÓN: UBICACIÓN
             */
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Campo: Dirección
            InputText(
                label = "Dirección",
                supportingText = "* Obligatorio",
                value = address,
                onValueChange = { address = it },
                onValidate = { it.isBlank() }
            )

            // Campo: Ciudad
            InputText(
                label = "Ciudad",
                supportingText = "* Obligatorio",
                value = city,
                onValueChange = { city = it },
                onValidate = { it.isBlank() }
            )

            // Campo: Departamento
            InputText(
                label = "Departamento",
                supportingText = "* Obligatorio",
                value = department,
                onValueChange = { department = it },
                onValidate = { it.isBlank() }
            )

            /**
             * MAPA INTERACTIVO
             * Muestra la ubicación seleccionada en el mapa
             * Click en el mapa actualiza las coordenadas GPS
             */
            Text(
                text = "Ubicación en el mapa",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Haz click en el mapa para seleccionar la ubicación exacta",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Map(
                    places = emptyList(), // Sin marcadores, solo el mapa
                    centerLatitude = latitude,
                    centerLongitude = longitude,
                    initialZoom = 15.0,
                    hasLocationPermission = false,
                    onMarkerClick = {},
                    onMapClick = { lat, lng ->
                        latitude = lat
                        longitude = lng
                        Toast.makeText(
                            context,
                            "Ubicación actualizada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            Text(
                text = "📍 Coordenadas: ${latitude.format(6)}, ${longitude.format(6)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Divider()

            /**
             * SECCIÓN: HORARIOS
             */
            Text(
                text = "Horarios de atención",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Selector de día
            var expandedDayDropdown by remember { mutableStateOf(false) }
            val daysOfWeek = listOf(
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
            )

            ExposedDropdownMenuBox(
                expanded = expandedDayDropdown,
                onExpandedChange = { expandedDayDropdown = it }
            ) {
                OutlinedTextField(
                    value = selectedDay,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Día") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDayDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDayDropdown,
                    onDismissRequest = { expandedDayDropdown = false }
                ) {
                    daysOfWeek.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day) },
                            onClick = {
                                selectedDay = day
                                expandedDayDropdown = false
                            }
                        )
                    }
                }
            }

            // Horarios de apertura y cierre
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = openTime,
                    onValueChange = { openTime = it },
                    label = { Text("Apertura") },
                    placeholder = { Text("08:00") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = closeTime,
                    onValueChange = { closeTime = it },
                    label = { Text("Cierre") },
                    placeholder = { Text("18:00") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Botón para agregar horario
            TextButton(
                onClick = {
                    if (openTime.isNotBlank() && closeTime.isNotBlank()) {
                        try {
                            val schedule = Schedule(
                                day = selectedDay,
                                open = LocalTime.parse(openTime),
                                close = LocalTime.parse(closeTime)
                            )
                            schedulesList = schedulesList + schedule
                            Toast.makeText(
                                context,
                                "Horario agregado: $selectedDay",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Formato de hora inválido. Usa HH:mm (ej: 08:00)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            ) {
                Text("+ Agregar horario")
            }

            // Lista de horarios agregados
            if (schedulesList.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Horarios agregados:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        schedulesList.forEach { schedule ->
                            Text(
                                text = "${schedule.day}: ${schedule.open} - ${schedule.close}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Divider()

            /**
             * SECCIÓN: CONTACTO
             */
            Text(
                text = "Información de contacto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Campo: Teléfono
            InputText(
                label = "Teléfono",
                supportingText = "Número de contacto (opcional)",
                value = phone,
                onValueChange = { phone = it },
                onValidate = { false }
            )

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * BOTÓN: CREAR LUGAR
             */
            Button(
                onClick = {
                    // ==================== VALIDACIONES ====================
                    
                    if (name.isBlank()) {
                        Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (city.isBlank()) {
                        Toast.makeText(context, "La ciudad es obligatoria", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (department.isBlank()) {
                        Toast.makeText(context, "El departamento es obligatorio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
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
                        location = Location(latitude, longitude),
                        images = listOf("https://via.placeholder.com/300x200?text=${name.take(20)}"),
                        phones = if (phone.isNotBlank()) listOf(phone.trim()) else emptyList(),
                        type = selectedType,
                        schedules = schedulesList,
                        approved = false,
                        ownerId = currentUserId,
                        createdAt = System.currentTimeMillis()
                    )
                    
                    placesViewModel.addPlace(newPlace)
                    
                    Toast.makeText(
                        context,
                        "✓ Lugar creado. Pendiente de aprobación",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Crear Lugar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Función de extensión para formatear Double a String con decimales específicos
 */
private fun Double.format(decimals: Int) = "%.${decimals}f".format(this)