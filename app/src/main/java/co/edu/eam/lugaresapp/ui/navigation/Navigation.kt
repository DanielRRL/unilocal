package co.edu.eam.lugaresapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Role
import co.edu.eam.lugaresapp.ui.auth.LoginScreen
import co.edu.eam.lugaresapp.ui.auth.RegisterScreen
import co.edu.eam.lugaresapp.ui.auth.PasswordRecoverScreen
import co.edu.eam.lugaresapp.ui.user.HomeUser
import co.edu.eam.lugaresapp.ui.admin.HomeAdmin
import co.edu.eam.lugaresapp.ui.user.screens.EditProfileScreen
import co.edu.eam.lugaresapp.ui.places.CreatePlaceScreen
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * CONTROLADOR PRINCIPAL DE NAVEGACIÓN uniLocal
 * 
 * Este archivo maneja toda la navegación de la aplicación usando Jetpack Navigation Compose.
 * 
 * CONCEPTOS CLAVE:
 * - Navigation Compose: Sistema de navegación oficial de Google para Jetpack Compose
 * - NavController: Controla la navegación entre pantallas
 * - NavHost: Contenedor que aloja todas las rutas/pantallas
 * - composable(): Define cada pantalla como un composable navegable
 * 
 * ARQUITECTURA:
 * - Single Activity: Toda la app vive en una Activity, las "pantallas" son Composables
 * - ViewModel scope: Los ViewModels se mantienen durante toda la navegación
 * - Type-safe navigation: Usando sealed classes para rutas seguras
 */

/**
 * FUNCIÓN PRINCIPAL DE NAVEGACIÓN
 * 
 * AppNavigation es el punto de entrada del sistema de navegación de uniLocal.
 * Esta función @Composable configura y maneja toda la navegación de la aplicación.
 * 
 * @param modifier: Modifier - Modificadores de Compose para personalizar el layout
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    /**
     * INICIALIZACIÓN DEL CONTROLADOR DE NAVEGACIÓN
     * 
     * rememberNavController() crea y recuerda una instancia de NavController.
     * Este objeto controla la navegación entre pantallas (push, pop, replace, etc.)
     */
    val navController = rememberNavController()
    
    /**
     * INICIALIZACIÓN DEL VIEWMODEL
     * 
     * viewModel() es una función de Compose que:
     * - Crea una instancia del ViewModel si no existe
     * - Reutiliza la instancia existente en recomposiciones
     * - Mantiene el ViewModel vivo durante toda la navegación
     * - Se destruye automáticamente cuando el Composable se destruye
     */
    val usersViewModel: UsersViewModel = viewModel()
    val placesViewModel: PlacesViewModel = viewModel()

    /**
     * INICIALIZACIÓN DEL SESSION MANAGER
     * 
     * SessionManager maneja la persistencia de sesión usando SharedPreferences.
     * Permite guardar/leer el userId y mantener al usuario logueado.
     */
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    /**
     * AUTO-LOGIN: DETECCIÓN DE SESIÓN ACTIVA
     * 
     * LaunchedEffect se ejecuta una sola vez al inicializar la pantalla (Unit como key).
     * Comprueba si hay un userId guardado en SharedPreferences y, si existe,
     * navega automáticamente a la pantalla correspondiente según el rol del usuario.
     * 
     * FLUJO:
     * 1. Obtener userId de SessionManager
     * 2. Si existe, buscar el usuario en el ViewModel
     * 3. Navegar a HomeAdmin o HomeUser según el rol
     * 4. Limpiar el backstack para evitar volver al Login con el botón atrás
     */
    LaunchedEffect(Unit) {
        val currentUserId = sessionManager.getUserId()
        if (currentUserId != null) {
            val user = usersViewModel.findById(currentUserId)
            if (user != null) {
                // Navegar a la pantalla correspondiente según el rol
                val destination = when (user.role) {
                    Role.ADMIN -> RouteScreen.HomeAdmin.route
                    Role.USER -> RouteScreen.HomeUser.route
                }
                
                // Navegar y limpiar el backstack para evitar volver al Login
                navController.navigate(destination) {
                    popUpTo(RouteScreen.Login.route) { inclusive = true }
                }
            } else {
                // Si el usuario no existe, limpiar la sesión
                sessionManager.clear()
            }
        }
    }

    /**
     * SUPERFICIE PRINCIPAL CON TEMA
     * 
     * Surface es un composable fundamental de Material 3 que:
     * - Aplica el color de fondo del tema automáticamente
     * - Proporciona elevation (sombra) si se especifica
     * - Asegura que el contenido tenga el color correcto según el tema
     */
    Surface(
        modifier = modifier.fillMaxSize(), // Ocupa toda la pantalla disponible
        color = MaterialTheme.colorScheme.background // Color de fondo del tema uniLocal
    ) {
        /**
         * CONFIGURACIÓN DEL HOST DE NAVEGACIÓN
         * 
         * NavHost es el contenedor principal que aloja todas las rutas de la aplicación.
         * Es como un "switch/when" gigante que renderiza la pantalla correcta según la ruta actual.
         * 
         * Parámetros importantes:
         * - navController: El controlador que maneja la navegación
         * - startDestination: La ruta inicial (Login en nuestro caso)
         * - modifier: Modificadores de layout
         */
        NavHost(
            navController = navController,
            startDestination = RouteScreen.Login.route, // Pantalla inicial: Login
            modifier = Modifier.fillMaxSize()
        ) {

            /**
             * RUTA: PANTALLA DE LOGIN
             * 
             * composable() define una ruta navegable.
             * Cada llamada a composable() es como un "case" en un switch.
             */
            composable(RouteScreen.Login.route) {
                LoginScreen(
                    navController = navController,
                    usersViewModel = usersViewModel
                )
            }

            /**
             * RUTA: PANTALLA DE REGISTRO
             */
            composable(RouteScreen.Register.route) {
                RegisterScreen(
                    usersViewModel = usersViewModel,
                    onNavigateToHome = { navController.navigate(RouteScreen.HomeUser.route) },
                    onNavigateToLogin = { navController.navigate(RouteScreen.Login.route) }
                )
            }

            /**
             * RUTA: PANTALLA DE RECUPERACIÓN DE CONTRASEÑA
             */
            composable(RouteScreen.PasswordRecover.route) {
                PasswordRecoverScreen(
                    onNavigateBack = { navController.popBackStack() } // popBackStack() navega hacia atrás
                )
            }

            /**
             * RUTA: DASHBOARD DE USUARIO
             */
            composable(RouteScreen.HomeUser.route) {
                HomeUser()
            }

            /**
             * RUTA: DASHBOARD DE ADMINISTRADOR
             */
            composable(RouteScreen.HomeAdmin.route) {
                HomeAdmin()
            }

            /**
             * RUTA: EDICIÓN DE PERFIL
             */
            composable(RouteScreen.EditProfile.route) {
                EditProfileScreen(
                    usersViewModel = usersViewModel, 
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /**
             * RUTA: CREACIÓN DE LUGAR
             */
            composable(RouteScreen.CreatePlace.route) {
                CreatePlaceScreen(
                    placesViewModel = placesViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}