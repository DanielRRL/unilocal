@file:OptIn(ExperimentalMaterial3Api::class)

package co.edu.eam.lugaresapp.ui.user.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.LocationData
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE EDICIÓN DE PERFIL
 * 
 * Permite al usuario actualizar sus datos personales.
 * 
 * CARACTERÍSTICAS:
 * - Precarga los datos actuales del usuario desde SessionManager
 * - Permite editar: nombre, username, ciudad
 * - NO permite editar: email (campo deshabilitado)
 * - NO permite editar: contraseña (no aparece en pantalla)
 * - Validaciones de campos obligatorios
 * - Feedback visual mediante Toast
 * 
 * FLUJO:
 * 1. Carga datos actuales del usuario logueado
 * 2. Usuario modifica campos editables
 * 3. Sistema valida campos obligatorios
 * 4. Si OK: actualiza usuario en UsersViewModel
 * 5. Navega de regreso tras actualización exitosa
 * 
 * @param usersViewModel ViewModel para gestión de usuarios
 * @param onNavigateBack Callback de navegación hacia atrás
 */
@Composable
fun EditProfileScreen(
    usersViewModel: UsersViewModel,
    onNavigateBack: () -> Unit
) {
    // Gestión de sesión
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()
    
    // Obtener usuario actual
    val currentUser = currentUserId?.let { usersViewModel.findById(it) }
    
    // Estados del formulario (inicializados con datos actuales)
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var username by remember { mutableStateOf(currentUser?.username ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var department by remember { mutableStateOf(currentUser?.department ?: "") }
    var city by remember { mutableStateOf(currentUser?.city ?: "") }
    val email = currentUser?.email ?: "" // Email NO editable
    
    // Estados para dropdowns
    var departmentExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }
    
    // Datos de ubicación
    val departments = LocationData.getDepartments()
    val cities = if (department.isNotEmpty()) {
        LocationData.getCitiesByDepartment(department)
    } else {
        emptyList()
    }
    
    // Resetear ciudad cuando cambia departamento
    LaunchedEffect(department) {
        if (department != currentUser?.department) {
            city = ""
        }
    }

    // Si no hay usuario logueado, mostrar mensaje
    if (currentUser == null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error: No se pudo cargar el perfil",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) {
                    Text("Volver")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Título informativo
                Text(
                    text = "Actualiza tus datos personales",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Campo: Nombre completo
                InputText(
                    label = stringResource(id = R.string.profile_name),
                    supportingText = "* Obligatorio",
                    value = name,
                    onValueChange = { name = it },
                    onValidate = { it.isBlank() }
                )

                // Campo: Username
                InputText(
                    label = "Username",
                    supportingText = "* Obligatorio",
                    value = username,
                    onValueChange = { username = it },
                    onValidate = { it.isBlank() }
                )

                // Campo: Teléfono
                InputText(
                    label = "Teléfono",
                    supportingText = "* Obligatorio",
                    value = phone,
                    onValueChange = { phone = it },
                    onValidate = { it.isBlank() }
                )

                // Dropdown: Departamento
                ExposedDropdownMenuBox(
                    expanded = departmentExpanded,
                    onExpandedChange = { departmentExpanded = it }
                ) {
                    OutlinedTextField(
                        value = department,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Departamento *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = departmentExpanded,
                        onDismissRequest = { departmentExpanded = false }
                    ) {
                        departments.forEach { dept ->
                            DropdownMenuItem(
                                text = { Text(dept) },
                                onClick = {
                                    department = dept
                                    departmentExpanded = false
                                }
                            )
                        }
                    }
                }

                // Dropdown: Ciudad
                ExposedDropdownMenuBox(
                    expanded = cityExpanded,
                    onExpandedChange = { 
                        if (department.isNotEmpty()) {
                            cityExpanded = it
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ciudad *") },
                        enabled = department.isNotEmpty(),
                        trailingIcon = {
                            if (department.isNotEmpty()) {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false }
                    ) {
                        cities.forEach { cityItem ->
                            DropdownMenuItem(
                                text = { Text(cityItem) },
                                onClick = {
                                    city = cityItem
                                    cityExpanded = false
                                }
                            )
                        }
                    }
                }

                // Campo: Email (NO EDITABLE)
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Email (no editable)") },
                    enabled = false, // Campo deshabilitado
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                )

                // Nota informativa
                Text(
                    text = "Nota: El email y la contraseña no se pueden modificar",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón: Guardar Cambios
                Button(
                    onClick = {
                        // ==================== VALIDACIONES ====================
                        
                        if (name.isBlank()) {
                            Toast.makeText(
                                context,
                                "El nombre es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        if (username.isBlank()) {
                            Toast.makeText(
                                context,
                                "El username es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        if (phone.isBlank()) {
                            Toast.makeText(
                                context,
                                "El teléfono es obligatorio",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        if (department.isBlank()) {
                            Toast.makeText(
                                context,
                                "Debe seleccionar un departamento",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        if (city.isBlank()) {
                            Toast.makeText(
                                context,
                                "Debe seleccionar una ciudad",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        
                        // ==================== ACTUALIZAR USUARIO ====================
                        
                        usersViewModel.updateUser(
                            userId = currentUser.id,
                            name = name.trim(),
                            username = username.trim(),
                            phone = phone.trim(),
                            department = department,
                            city = city
                        )
                        
                        // Feedback exitoso
                        Toast.makeText(
                            context,
                            "Perfil actualizado exitosamente",
                            Toast.LENGTH_SHORT
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
                    Text(text = stringResource(id = R.string.profile_update_button))
                }

                // Botón: Cancelar
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}