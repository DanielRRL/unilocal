@file:OptIn(ExperimentalMaterial3Api::class)

package co.edu.eam.lugaresapp.ui.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.model.LocationData
import co.edu.eam.lugaresapp.model.User
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.ui.components.OperationResultHandler
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE REGISTRO DE USUARIOS
 * 
 * Permite a nuevos usuarios registrarse en la aplicación uniLocal.
 * Incluye validación de campos y selección jerárquica de ubicación (departamento → ciudad).
 * 
 * CAMPOS DEL FORMULARIO:
 * - Nombre completo (requerido)
 * - Departamento (dropdown - requerido)
 * - Ciudad (dropdown cascada filtrada por departamento - requerido)
 * - Teléfono (requerido)
 * - Email (requerido, formato válido, único)
 * - Contraseña (requerida, mínimo 5 caracteres)
 * - Confirmar contraseña (debe coincidir)
 * 
 * VALIDACIONES:
 * - Todos los campos son obligatorios
 * - Email debe tener formato válido (@, dominio)
 * - Contraseña mínimo 5 caracteres
 * - Contraseña y confirmación deben coincidir
 * - Departamento y ciudad deben estar seleccionados
 * - Email no debe estar ya registrado
 * 
 * FLUJO:
 * 1. Usuario completa el formulario
 * 2. Usuario selecciona departamento (esto habilita dropdown de ciudades)
 * 3. Usuario selecciona ciudad del departamento elegido
 * 4. Usuario hace clic en "Registrarse"
 * 5. Se validan todos los campos
 * 6. Si pasa validación, se crea el usuario en Firebase
 * 7. Usuario es redirigido a la pantalla principal
 * 
 * @param usersViewModel ViewModel que maneja la lógica de usuarios y Firebase
 * @param onNavigateToHome Callback para navegar a pantalla principal después del registro
 * @param onNavigateToLogin Callback para navegar a pantalla de login si usuario ya tiene cuenta
 */
@Composable
fun RegisterScreen(
    usersViewModel: UsersViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val reportResult by usersViewModel.reportResult.collectAsState()

    // ==================== ESTADO DEL FORMULARIO ====================
    
    // Campo: Nombre completo del usuario
    var name by remember { mutableStateOf("") }
    
    // Campo: Email único (usado también como username)
    var email by remember { mutableStateOf("") }
    
    // Campo: Número de teléfono
    var phone by remember { mutableStateOf("") }
    
    // Campo: Contraseña (mínimo 5 caracteres)
    var password by remember { mutableStateOf("") }
    
    // Campo: Confirmación de contraseña (debe coincidir con password)
    var confirmPassword by remember { mutableStateOf("") }
    
    // Dropdown: Departamento seleccionado (determina qué ciudades se muestran)
    var selectedDepartment by remember { mutableStateOf("") }
    
    // Dropdown: Ciudad seleccionada (filtrada por departamento)
    var selectedCity by remember { mutableStateOf("") }
    
    // Estado: Control de expansión del dropdown de departamentos
    var departmentExpanded by remember { mutableStateOf(false) }
    
    // Estado: Control de expansión del dropdown de ciudades
    var cityExpanded by remember { mutableStateOf(false) }
    
    // ==================== DATOS DE UBICACIÓN ====================
    
    // Lista de departamentos disponibles (ordenados alfabéticamente)
    val departments = LocationData.getDepartments()
    
    // Lista de ciudades disponibles (filtradas por departamento seleccionado)
    // Si no hay departamento seleccionado, la lista está vacía
    val cities = if (selectedDepartment.isNotEmpty()) {
        LocationData.getCitiesByDepartment(selectedDepartment)
    } else {
        emptyList()
    }
    
    // ==================== EFECTO REACTIVO: RESETEAR CIUDAD ====================
    
    // Cuando cambia el departamento, resetear la ciudad seleccionada
    // Esto previene inconsistencias (ej: tener "Medellín" seleccionado después de cambiar a "Cauca")
    LaunchedEffect(selectedDepartment) {
        selectedCity = ""
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==================== CAMPO: NOMBRE COMPLETO ====================
            
            InputText(
                label = "Nombre completo",
                supportingText = stringResource(id = R.string.txt_required_field),
                value = name,
                onValueChange = { name = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== DROPDOWN: SELECCIONAR DEPARTAMENTO ====================
            
            // ExposedDropdownMenuBox: Componente Material 3 para dropdowns
            // expanded: Controla si el menú está abierto o cerrado
            // onExpandedChange: Callback cuando el usuario abre/cierra el menú
            ExposedDropdownMenuBox(
                expanded = departmentExpanded,
                onExpandedChange = { departmentExpanded = it }
            ) {
                // Campo de texto que actúa como trigger del dropdown
                OutlinedTextField(
                    value = selectedDepartment,
                    onValueChange = {},
                    readOnly = true, // No permite escritura manual, solo selección del dropdown
                    label = { Text("Seleccione el departamento") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(), // Ancla el menú dropdown a este campo
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                
                // Menú dropdown con lista de departamentos
                ExposedDropdownMenu(
                    expanded = departmentExpanded,
                    onDismissRequest = { departmentExpanded = false }
                ) {
                    // Iterar sobre todos los departamentos disponibles
                    departments.forEach { department ->
                        DropdownMenuItem(
                            text = { Text(department) },
                            onClick = {
                                selectedDepartment = department
                                departmentExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== DROPDOWN: SELECCIONAR CIUDAD ====================
            
            // Dropdown de ciudades (habilitado solo si hay departamento seleccionado)
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { 
                    // Solo permitir abrir si hay departamento seleccionado
                    if (selectedDepartment.isNotEmpty()) {
                        cityExpanded = it
                    }
                }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione la ciudad") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                    },
                    enabled = selectedDepartment.isNotEmpty(), // Deshabilitar si no hay departamento
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                
                // Menú dropdown con lista de ciudades del departamento seleccionado
                ExposedDropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false }
                ) {
                    // Iterar sobre ciudades filtradas por departamento
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                cityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== CAMPO: TELÉFONO ====================
            
            InputText(
                label = stringResource(id = R.string.register_phone),
                supportingText = stringResource(id = R.string.txt_required_field),
                value = phone,
                onValueChange = { phone = it },
                onValidate = { it.isBlank() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== CAMPO: EMAIL ====================
            
            // Validación de formato de email usando Android Patterns
            InputText(
                label = stringResource(id = R.string.register_email),
                supportingText = stringResource(id = R.string.txt_email_error),
                value = email,
                onValueChange = { email = it },
                onValidate = { it.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(it).matches() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== CAMPO: CONTRASEÑA ====================
            
            // Validación: mínimo 5 caracteres
            InputText(
                label = stringResource(id = R.string.register_password),
                supportingText = stringResource(id = R.string.txt_password_error),
                value = password,
                onValueChange = { password = it },
                onValidate = { it.isBlank() || it.length < 5 },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ==================== CAMPO: CONFIRMAR CONTRASEÑA ====================
            
            // Validación: debe coincidir con el campo password
            InputText(
                label = stringResource(id = R.string.register_confirm_password),
                supportingText = "Las contraseñas deben coincidir",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                onValidate = { it.isBlank() || confirmPassword != password },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ==================== BOTÓN: REGISTRARSE ====================
            
            Button(
                onClick = {
                    // ==================== VALIDACIÓN COMPLETA DEL FORMULARIO ====================
                    
                    // Validar que todos los campos estén completos
                    if (name.isBlank()) {
                        Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar que departamento esté seleccionado
                    if (selectedDepartment.isBlank()) {
                        Toast.makeText(context, "Debe seleccionar un departamento", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar que ciudad esté seleccionada
                    if (selectedCity.isBlank()) {
                        Toast.makeText(context, "Debe seleccionar una ciudad", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar que teléfono esté completo
                    if (phone.isBlank()) {
                        Toast.makeText(context, "El teléfono es obligatorio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar formato de email
                    if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "Ingrese un email válido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar longitud de contraseña
                    if (password.isBlank() || password.length < 5) {
                        Toast.makeText(context, "La contraseña debe tener al menos 5 caracteres", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Validar que contraseñas coincidan
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // ==================== CREAR USUARIO EN FIREBASE ====================
                    
                    // Verificar que el email no esté duplicado
                    if (usersViewModel.existsByEmail(email)) {
                        Toast.makeText(context, "Error: El email ya está registrado", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    // Hashear la contraseña con SHA-256
                    val hashedPassword = usersViewModel.hashPassword(password)
                    
                    // Crear objeto User
                    val newUser = co.edu.eam.lugaresapp.model.User(
                        id = java.util.UUID.randomUUID().toString(),
                        name = name,
                        username = email,
                        phone = phone,
                        email = email,
                        password = hashedPassword,
                        department = selectedDepartment,
                        city = selectedCity,
                        role = co.edu.eam.lugaresapp.model.Role.USER,
                        favorites = emptyList()
                    )
                    
                    // Guardar en Firebase usando create()
                    usersViewModel.create(newUser)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = stringResource(id = R.string.register_button))
            }

            // ==================== MANEJADOR DE RESULTADOS ====================
            
            OperationResultHandler(
                result = reportResult,
                onSuccess = {
                    onNavigateToHome()
                    usersViewModel.resetOperationResult()
                },
                onFailure = {
                    usersViewModel.resetOperationResult()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ==================== BOTÓN: IR A LOGIN ====================
            
            // Permite navegar a pantalla de login si el usuario ya tiene cuenta
            TextButton(onClick = { onNavigateToLogin() }) {
                Text(text = stringResource(id = R.string.login_button))
            }

        }
    }
}