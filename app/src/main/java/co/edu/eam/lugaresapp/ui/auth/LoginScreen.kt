package co.edu.eam.lugaresapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.utils.RequestResult
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.ui.components.InputText
import co.edu.eam.lugaresapp.ui.navigation.RouteScreen
import co.edu.eam.lugaresapp.ui.theme.UniLocalButton
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE LOGIN uniLocal
 * 
 * Esta es la pantalla principal de autenticación de la aplicación uniLocal.
 * Permite a los usuarios iniciar sesión con email y contraseña.
 * 
 * CARACTERÍSTICAS PRINCIPALES:
 * - Validación robusta de email y contraseña en tiempo real
 * - Mensajes de error específicos y claros
 * - Diseño responsive con scroll automático
 * - Navegación basada en roles (ADMIN/USER)
 * - Información de usuarios de prueba visible
 * - Botón con estado de carga
 * - Campos con iconos y toggle de contraseña
 * 
 * ARQUITECTURA:
 * - Sigue patrón MVVM con ViewModel para lógica de negocio
 * - Estado local con remember() para UI state
 * - Navegación controlada por NavController
 * - Validación inmediata en cambios de texto
 * 
 * @param navController: NavController - Controlador de navegación de Jetpack Navigation
 * @param usersViewModel: UsersViewModel - ViewModel que maneja la lógica de usuarios
 */
@OptIn(ExperimentalMaterial3Api::class) // Necesario para usar APIs experimentales de Material 3
@Composable
fun LoginScreen(
    navController: NavController,
    usersViewModel: UsersViewModel
) {
    /**
     * INICIALIZACIÓN DE SESSION MANAGER
     * 
     * SessionManager maneja la persistencia de sesión con SharedPreferences.
     * Se inicializa aquí para poder guardar el userId después del login exitoso.
     */
    val context = androidx.compose.ui.platform.LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    /**
     * ESTADO LOCAL DE LA PANTALLA
     * 
     * remember() + mutableStateOf() crea estado reactivo que persiste entre recomposiciones.
     * 'by' es un delegate property que simplifica el acceso (evita .value)
     * 
     * Cada variable de estado dispara recomposición automática cuando cambia.
     */
    var email by remember { mutableStateOf("") }           // Email ingresado por el usuario
    var password by remember { mutableStateOf("") }        // Contraseña ingresada por el usuario  
    var isLoading by remember { mutableStateOf(false) }    // Estado de carga del botón login
    var errorMessage by remember { mutableStateOf("") }    // Mensaje de error general
    var emailError by remember { mutableStateOf("") }      // Mensaje de error específico del email
    var passwordError by remember { mutableStateOf("") }   // Mensaje de error específico de la contraseña
    
    // Observar el usuario actual desde StateFlow
    val currentUser by usersViewModel.currentUser.collectAsState()
    val reportResult by usersViewModel.reportResult.collectAsState()
    
    /**
     * EFECTO PARA MANEJAR RESULTADO DE LOGIN
     * 
     * Observa cambios en reportResult para manejar loading, éxito y error.
     */
    LaunchedEffect(reportResult) {
        when (val result = reportResult) {
            is RequestResult.Loading -> {
                isLoading = true
                errorMessage = ""
            }
            is RequestResult.Success -> {
                isLoading = false
                // La navegación se maneja en el efecto de currentUser
            }
            is RequestResult.Failure -> {
                isLoading = false
                errorMessage = result.errorMessage
            }
            else -> {}
        }
    }
    
    /**
     * EFECTO CUANDO LOGIN ES EXITOSO
     * 
     * Observa cambios en currentUser. Cuando el login es exitoso
     * (currentUser no es null), guarda el userId y navega a la pantalla correspondiente.
     */
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            isLoading = false
            sessionManager.saveUserId(currentUser!!.id)
            
            val destination = when (currentUser!!.role.name) {
                "ADMIN" -> RouteScreen.HomeAdmin.route
                "USER" -> RouteScreen.HomeUser.route
                else -> RouteScreen.HomeUser.route
            }
            
            navController.navigate(destination) {
                popUpTo(RouteScreen.Login.route) { inclusive = true }
            }
        }
    }

    /**
     * FUNCIONES DE VALIDACIÓN
     * 
     * Estas funciones locales validan los campos de entrada y retornan
     * mensajes de error específicos o string vacío si la validación pasa.
     */
    
    /**
     * VALIDACIÓN DE EMAIL
     * 
     * Valida que el email no esté vacío y tenga formato válido.
     * Usa android.util.Patterns.EMAIL_ADDRESS para validación robusta.
     * 
     * @param email: String - El email a validar
     * @return String - Mensaje de error o string vacío si es válido
     */
    fun validateEmail(email: String): String {
        return when {
            email.isEmpty() -> "El email es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email inválido"
            else -> "" // Email válido
        }
    }

    /**
     * VALIDACIÓN DE CONTRASEÑA
     * 
     * Valida que la contraseña no esté vacía y tenga mínimo 6 caracteres.
     * 
     * @param password: String - La contraseña a validar
     * @return String - Mensaje de error o string vacío si es válida
     */
    fun validatePassword(password: String): String {
        return when {
            password.isEmpty() -> "La contraseña es requerida"
            password.length < 6 -> "La contraseña debe tener mínimo 6 caracteres"
            else -> "" // Contraseña válida
        }
    }

    /**
     * VALIDACIÓN COMPLETA DEL FORMULARIO
     * 
     * Ejecuta todas las validaciones y actualiza los estados de error.
     * 
     * @return Boolean - true si todo es válido, false si hay errores
     */
    fun validateForm(): Boolean {
        emailError = validateEmail(email)
        passwordError = validatePassword(password)
        return emailError.isEmpty() && passwordError.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo y título
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = stringResource(R.string.welcome_message),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )

        // Formulario en Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Email
                InputText(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = ""
                        errorMessage = ""
                    },
                    label = stringResource(R.string.email),
                    placeholder = "usuario@ejemplo.com",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = Icons.Default.Email,
                    isError = emailError.isNotEmpty(),
                    errorMessage = emailError
                )

                // Campo Password
                InputText(
                    value = password,
                    onValueChange = { 
                        password = it
                        passwordError = ""
                        errorMessage = ""
                    },
                    label = stringResource(R.string.password),
                    placeholder = "Mínimo 6 caracteres",
                    isPassword = true,
                    leadingIcon = Icons.Default.Lock,
                    isError = passwordError.isNotEmpty(),
                    errorMessage = passwordError
                )

                // Error general
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Login
                Button(
                    onClick = {
                        if (validateForm()) {
                            isLoading = true
                            errorMessage = ""
                            
                            // Ejecutar login - actualiza currentUser StateFlow
                            usersViewModel.login(email, password)
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UniLocalButton,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.login_button),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Links
                TextButton(
                    onClick = { navController.navigate(RouteScreen.PasswordRecover.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.no_account),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(
                        onClick = { navController.navigate(RouteScreen.Register.route) }
                    ) {
                        Text(
                            text = stringResource(R.string.register_link),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}