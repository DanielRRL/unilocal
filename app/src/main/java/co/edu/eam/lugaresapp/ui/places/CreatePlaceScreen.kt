@file:OptIn(ExperimentalMaterial3Api::class)

package co.edu.eam.lugaresapp.ui.places

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.model.Schedule
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

/**
 * PANTALLA DE CREACIÓN DE LUGAR - Wizard de 4 Pasos
 * 
 * PÁGINAS:
 * 1. Información General (nombre, descripción, categoría)
 * 2. Horarios (días y horas de atención)
 * 3. Ubicación (ciudad, dirección, teléfono)
 * 4. Mapa + Imagen (selección de ubicación GPS y subida de imagen)
 */
@Composable
fun CreatePlaceScreen(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    onNavigateBack: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    
    // Estados de información general (Página 1)
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PlaceType.RESTAURANT) }
    
    // Estados de horarios (Página 2)
    var schedulesList by remember { mutableStateOf<List<Schedule>>(emptyList()) }
    
    // Estados de ubicación (Página 3)
    var city by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    
    // Estados de mapa e imagen (Página 4)
    var latitude by remember { mutableDoubleStateOf(4.4687891) }
    var longitude by remember { mutableDoubleStateOf(-75.6491181) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }
    var isUploadingImage by remember { mutableStateOf(false) }
    var showMapFullScreen by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    // Configurar Cloudinary
    LaunchedEffect(Unit) {
        try {
            val config = mapOf(
                "cloud_name" to "djgiehsqq",
                "api_key" to "629612441694929",
                "api_secret" to "ek-arescFmGUO9gPFvAz3162u_4"
            )
            MediaManager.init(context, config)
        } catch (e: Exception) {
            android.util.Log.e("CreatePlace", "Error al inicializar Cloudinary: ${e.message}")
        }
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            isUploadingImage = true
            
            // Subir imagen a Cloudinary
            scope.launch {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val tempFile = File(context.cacheDir, "temp_upload_${System.currentTimeMillis()}.jpg")
                    tempFile.outputStream().use { output ->
                        inputStream?.copyTo(output)
                    }
                    
                    MediaManager.get().upload(tempFile.absolutePath)
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String) {
                                android.util.Log.d("CreatePlace", "Upload iniciado: $requestId")
                            }

                            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                                val progress = (bytes * 100 / totalBytes).toInt()
                                android.util.Log.d("CreatePlace", "Progreso: $progress%")
                            }

                            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                                imageUrl = resultData["secure_url"] as? String ?: ""
                                isUploadingImage = false
                                Toast.makeText(context, "✓ Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                                android.util.Log.d("CreatePlace", "URL de imagen: $imageUrl")
                                tempFile.delete()
                            }

                            override fun onError(requestId: String, error: ErrorInfo) {
                                isUploadingImage = false
                                Toast.makeText(context, "Error al subir imagen: ${error.description}", Toast.LENGTH_LONG).show()
                                android.util.Log.e("CreatePlace", "Error upload: ${error.description}")
                                tempFile.delete()
                            }

                            override fun onReschedule(requestId: String, error: ErrorInfo) {
                                android.util.Log.d("CreatePlace", "Upload reprogramado: ${error.description}")
                            }
                        })
                        .dispatch()
                        
                } catch (e: Exception) {
                    isUploadingImage = false
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    android.util.Log.e("CreatePlace", "Error al procesar imagen: ${e.message}", e)
                }
            }
        }
    }

    // Launcher para permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    if (showMapFullScreen) {
        // Pantalla de mapa en pantalla completa
        MapPickerScreen(
            initialLat = latitude,
            initialLng = longitude,
            onLocationSelected = { lat, lng ->
                latitude = lat
                longitude = lng
                showMapFullScreen = false
                Toast.makeText(context, "✓ Ubicación actualizada", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showMapFullScreen = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear nuevo lugar") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationButtons(
                    currentPage = currentPage,
                    totalPages = 4,
                    onPrevious = { if (currentPage > 0) currentPage-- },
                    onNext = {
                        if (currentPage < 3) {
                            currentPage++
                        } else {
                            // Validar y crear lugar
                            createPlace(
                                name = name,
                                description = description,
                                selectedType = selectedType,
                                schedulesList = schedulesList,
                                city = city,
                                department = department,
                                address = address,
                                phone = phone,
                                latitude = latitude,
                                longitude = longitude,
                                imageUrl = imageUrl,
                                sessionManager = sessionManager,
                                placesViewModel = placesViewModel,
                                context = context,
                                onSuccess = onNavigateBack
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Indicador de progreso
                ProgressIndicator(currentPage = currentPage, totalPages = 4)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                when (currentPage) {
                    0 -> Page1InfoGeneral(
                        name = name,
                        onNameChange = { name = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        selectedType = selectedType,
                        onTypeChange = { selectedType = it }
                    )
                    1 -> Page2Horarios(
                        schedulesList = schedulesList,
                        onSchedulesChange = { schedulesList = it },
                        context = context
                    )
                    2 -> Page3Ubicacion(
                        city = city,
                        onCityChange = { city = it },
                        department = department,
                        onDepartmentChange = { department = it },
                        address = address,
                        onAddressChange = { address = it },
                        phone = phone,
                        onPhoneChange = { phone = it }
                    )
                    3 -> Page4MapaImagen(
                        latitude = latitude,
                        longitude = longitude,
                        imageUri = imageUri,
                        imageUrl = imageUrl,
                        isUploadingImage = isUploadingImage,
                        onSelectLocation = { showMapFullScreen = true },
                        onSelectImage = {
                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            }
                            permissionLauncher.launch(permission)
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

/**
 * INDICADOR DE PROGRESO
 */
@Composable
fun ProgressIndicator(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .padding(horizontal = 2.dp)
                    .background(
                        color = if (index <= currentPage) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
    
    Text(
        text = when (currentPage) {
            0 -> "1. Información general"
            1 -> "2. Horarios"
            2 -> "3. Ubicación"
            else -> "4. Mapa y foto"
        },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

/**
 * PÁGINA 1: INFORMACIÓN GENERAL
 */
@Composable
fun Page1InfoGeneral(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedType: PlaceType,
    onTypeChange: (PlaceType) -> Unit
) {
    var expandedDropdown by remember { mutableStateOf(false) }
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        InputText(
            label = "Nombre del lugar",
            supportingText = "* Obligatorio",
            value = name,
            onValueChange = onNameChange,
            onValidate = { it.isBlank() }
        )
        
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
                            onTypeChange(type)
                            expandedDropdown = false
                        }
                    )
                }
            }
        }
        
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción *") },
            supportingText = { Text("Mínimo 10 caracteres") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 6
        )
    }
}

/**
 * PÁGINA 2: HORARIOS
 */
@Composable
fun Page2Horarios(
    schedulesList: List<Schedule>,
    onSchedulesChange: (List<Schedule>) -> Unit,
    context: android.content.Context
) {
    var selectedDay by remember { mutableStateOf("Lunes") }
    var openTime by remember { mutableStateOf("08:00") }
    var closeTime by remember { mutableStateOf("18:00") }
    var expandedDayDropdown by remember { mutableStateOf(false) }
    
    val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
        
        Button(
            onClick = {
                if (openTime.isNotBlank() && closeTime.isNotBlank()) {
                    val timeRegex = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
                    if (timeRegex.matches(openTime) && timeRegex.matches(closeTime)) {
                        val schedule = Schedule(
                            day = selectedDay,
                            open = openTime,
                            close = closeTime
                        )
                        onSchedulesChange(schedulesList + schedule)
                        Toast.makeText(context, "✓ Horario agregado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Formato de hora inválido. Usa HH:mm", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Agregar horario")
        }
        
        if (schedulesList.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Horarios agregados:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    schedulesList.forEach { schedule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${schedule.day}:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${schedule.open} - ${schedule.close}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * PÁGINA 3: UBICACIÓN
 */
@Composable
fun Page3Ubicacion(
    city: String,
    onCityChange: (String) -> Unit,
    department: String,
    onDepartmentChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        InputText(
            label = "Ciudad",
            supportingText = "* Obligatorio",
            value = city,
            onValueChange = onCityChange,
            onValidate = { it.isBlank() }
        )
        
        InputText(
            label = "Departamento",
            supportingText = "* Obligatorio",
            value = department,
            onValueChange = onDepartmentChange,
            onValidate = { it.isBlank() }
        )
        
        InputText(
            label = "Dirección",
            supportingText = "* Obligatorio",
            value = address,
            onValueChange = onAddressChange,
            onValidate = { it.isBlank() }
        )
        
        InputText(
            label = "Teléfono",
            supportingText = "Número de contacto (opcional)",
            value = phone,
            onValueChange = onPhoneChange,
            onValidate = { false }
        )
    }
}

/**
 * PÁGINA 4: MAPA E IMAGEN
 */
@Composable
fun Page4MapaImagen(
    latitude: Double,
    longitude: Double,
    imageUri: Uri?,
    imageUrl: String,
    isUploadingImage: Boolean,
    onSelectLocation: () -> Unit,
    onSelectImage: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Ubicación GPS",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectLocation() }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📍 ${latitude.format(6)}, ${longitude.format(6)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onSelectLocation) {
                    Text("Cambiar ubicación en el mapa")
                }
            }
        }
        
        Divider()
        
        Text(
            text = "Imagen del lugar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        if (imageUri != null || imageUrl.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = imageUri ?: imageUrl,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    if (isUploadingImage) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
        
        Button(
            onClick = onSelectImage,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUploadingImage
        ) {
            Icon(Icons.Default.Image, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (imageUrl.isEmpty()) "Seleccionar imagen" else "Cambiar imagen")
        }
    }
}

/**
 * BOTONES DE NAVEGACIÓN
 */
@Composable
fun NavigationButtons(
    currentPage: Int,
    totalPages: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentPage > 0) {
                FloatingActionButton(
                    onClick = onPrevious,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Anterior")
                }
            } else {
                Spacer(modifier = Modifier.width(56.dp))
            }
            
            FloatingActionButton(
                onClick = onNext,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = if (currentPage == totalPages - 1) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = if (currentPage == totalPages - 1) "Crear" else "Siguiente"
                )
            }
        }
    }
}

/**
 * PANTALLA DE SELECCIÓN DE UBICACIÓN EN MAPA
 */
@Composable
fun MapPickerScreen(
    initialLat: Double,
    initialLng: Double,
    onLocationSelected: (Double, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLat by remember { mutableDoubleStateOf(initialLat) }
    var selectedLng by remember { mutableDoubleStateOf(initialLng) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar ubicación") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Cancelar")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📍 ${selectedLat.format(6)}, ${selectedLng.format(6)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onLocationSelected(selectedLat, selectedLng) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirmar ubicación")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Map(
                places = emptyList(),
                centerLatitude = selectedLat,
                centerLongitude = selectedLng,
                initialZoom = 15.0,
                hasLocationPermission = false,
                onMarkerClick = {},
                onMapClick = { lat, lng ->
                    selectedLat = lat
                    selectedLng = lng
                }
            )
        }
    }
}

/**
 * FUNCIÓN AUXILIAR PARA CREAR LUGAR
 */
private fun createPlace(
    name: String,
    description: String,
    selectedType: PlaceType,
    schedulesList: List<Schedule>,
    city: String,
    department: String,
    address: String,
    phone: String,
    latitude: Double,
    longitude: Double,
    imageUrl: String,
    sessionManager: SessionManager,
    placesViewModel: PlacesViewModel,
    context: android.content.Context,
    onSuccess: () -> Unit
) {
    if (name.isBlank()) {
        Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
        return
    }
    
    if (description.length < 10) {
        Toast.makeText(context, "La descripción debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show()
        return
    }
    
    if (address.isBlank() || city.isBlank() || department.isBlank()) {
        Toast.makeText(context, "Completa todos los campos de ubicación", Toast.LENGTH_SHORT).show()
        return
    }
    
    val currentUserId = sessionManager.getUserId()
    if (currentUserId == null) {
        Toast.makeText(context, "Debes iniciar sesión para crear un lugar", Toast.LENGTH_LONG).show()
        return
    }
    
    val newPlace = Place(
        id = UUID.randomUUID().toString(),
        title = name.trim(),
        description = description.trim(),
        address = "$address, $city, $department".trim(),
        location = Location(latitude, longitude),
        images = if (imageUrl.isNotEmpty()) listOf(imageUrl) else emptyList(),
        phones = if (phone.isNotBlank()) listOf(phone.trim()) else emptyList(),
        type = selectedType,
        schedules = schedulesList,
        approved = false,
        ownerId = currentUserId,
        createdAt = System.currentTimeMillis()
    )
    
    placesViewModel.addPlace(newPlace)
    
    Toast.makeText(context, "✓ Lugar creado. Pendiente de aprobación", Toast.LENGTH_LONG).show()
    
    onSuccess()
}

/**
 * Función de extensión para formatear Double
 */
private fun Double.format(decimals: Int) = "%.${decimals}f".format(this)