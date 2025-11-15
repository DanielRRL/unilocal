@file:OptIn(ExperimentalMaterial3Api::class)

package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.LocationData
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE PERFIL DE USUARIO
 * 
 * Permite al usuario ver y editar su información personal.
 * Incluye modo de edición con validación y selección jerárquica de ubicación.
 * 
 * CAMPOS EDITABLES:
 * - Nombre completo
 * - Username (apellido)
 * - Teléfono
 * - Departamento (dropdown)
 * - Ciudad (dropdown cascada filtrada por departamento)
 * 
 * CAMPOS NO EDITABLES:
 * - Email (mostrado pero no editable)
 * - Contraseña (requiere flujo especial de cambio de contraseña)
 * 
 * FUNCIONALIDADES:
 * - Ver información del perfil
 * - Activar modo edición con botón de editar (icono lápiz)
 * - Guardar cambios con botón de check
 * - Cerrar sesión con confirmación
 * - Validación de sesión activa
 * 
 * @param usersViewModel ViewModel que maneja la lógica de usuarios
 * @param onLogout Callback para manejar el cierre de sesión
 */
@Composable
fun ProfileScreen(
    usersViewModel: UsersViewModel,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()
    
    // ==================== ESTADO DE LA PANTALLA ====================
    
    // Control de modo edición
    var isEditMode by remember { mutableStateOf(false) }
    
    // Control de diálogo de confirmación de logout
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // ==================== VALIDACIÓN DE SESIÓN ====================
    
    // Si no hay sesión activa, mostrar mensaje
    if (currentUserId == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay sesión activa",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }
    
    // Obtener datos del usuario actual desde el ViewModel
    val currentUser by usersViewModel.currentUser.collectAsState()
    
    // Cargar usuario si no está cargado
    LaunchedEffect(currentUserId) {
        if (currentUser == null || currentUser?.id != currentUserId) {
            usersViewModel.findById(currentUserId)
        }
    }
    
    // Si usuario no existe en base de datos, mostrar mensaje de carga
    val user = currentUser
    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    // ==================== ESTADO DE CAMPOS EDITABLES ====================
    
    // Estados para campos de texto
    var editedName by remember { mutableStateOf(user.name) }
    var editedUsername by remember { mutableStateOf(user.username) }
    var editedPhone by remember { mutableStateOf(user.phone) }
    
    // Estados para dropdowns de ubicación
    var editedDepartment by remember { mutableStateOf(user.department) }
    var editedCity by remember { mutableStateOf(user.city) }
    
    // Control de expansión de dropdowns
    var departmentExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }
    
    // ==================== DATOS DE UBICACIÓN ====================
    
    val departments = LocationData.getDepartments()
    val cities = if (editedDepartment.isNotEmpty()) {
        LocationData.getCitiesByDepartment(editedDepartment)
    } else {
        emptyList()
    }
    
    // Resetear ciudad cuando cambia departamento (prevenir inconsistencias)
    LaunchedEffect(editedDepartment) {
        if (editedDepartment != user.department) {
            editedCity = ""
        }
    }
    
    // ==================== UI ====================
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ==================== BOTÓN EDITAR/GUARDAR ====================
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    if (isEditMode) {
                        // Modo guardar: actualizar datos del usuario
                        usersViewModel.updateUser(
                            userId = currentUserId,
                            name = editedName,
                            username = editedUsername,
                            phone = editedPhone,
                            department = editedDepartment,
                            city = editedCity
                        )
                    }
                    // Toggle modo edición
                    isEditMode = !isEditMode
                }
            ) {
                Icon(
                    imageVector = if (isEditMode) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = if (isEditMode) "Guardar" else "Editar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        // ==================== AVATAR ====================
        
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // ==================== CAMPO: NOMBRE ====================
        
        OutlinedTextField(
            value = editedName,
            onValueChange = { editedName = it },
            label = { Text("Nombre") },
            enabled = isEditMode,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // ==================== CAMPO: USERNAME/APELLIDO ====================
        
        OutlinedTextField(
            value = editedUsername,
            onValueChange = { editedUsername = it },
            label = { Text("Apellido") },
            enabled = isEditMode,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // ==================== CAMPO: TELÉFONO ====================
        
        OutlinedTextField(
            value = editedPhone,
            onValueChange = { editedPhone = it },
            label = { Text("Teléfono") },
            enabled = isEditMode,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        // ==================== DROPDOWN: DEPARTAMENTO ====================
        
        ExposedDropdownMenuBox(
            expanded = departmentExpanded && isEditMode,
            onExpandedChange = { 
                if (isEditMode) {
                    departmentExpanded = it
                }
            }
        ) {
            OutlinedTextField(
                value = editedDepartment,
                onValueChange = {},
                readOnly = true,
                label = { Text("Departamento") },
                enabled = isEditMode,
                trailingIcon = {
                    if (isEditMode) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            ExposedDropdownMenu(
                expanded = departmentExpanded && isEditMode,
                onDismissRequest = { departmentExpanded = false }
            ) {
                departments.forEach { department ->
                    DropdownMenuItem(
                        text = { Text(department) },
                        onClick = {
                            editedDepartment = department
                            departmentExpanded = false
                        }
                    )
                }
            }
        }
        
        // ==================== DROPDOWN: CIUDAD ====================
        
        ExposedDropdownMenuBox(
            expanded = cityExpanded && isEditMode,
            onExpandedChange = { 
                if (isEditMode && editedDepartment.isNotEmpty()) {
                    cityExpanded = it
                }
            }
        ) {
            OutlinedTextField(
                value = editedCity,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ciudad") },
                enabled = isEditMode && editedDepartment.isNotEmpty(),
                trailingIcon = {
                    if (isEditMode && editedDepartment.isNotEmpty()) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            ExposedDropdownMenu(
                expanded = cityExpanded && isEditMode,
                onDismissRequest = { cityExpanded = false }
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(city) },
                        onClick = {
                            editedCity = city
                            cityExpanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // ==================== BOTÓN: CAMBIAR CONTRASEÑA ====================
        
        TextButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cambiar Contraseña",
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))
        
        // ==================== BOTÓN: CERRAR SESIÓN ====================
        
        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = "Cerrar Sesión",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
    
    // ==================== DIÁLOGO DE CONFIRMACIÓN DE LOGOUT ====================
    
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        sessionManager.clear()
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sí, cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
