package co.edu.eam.lugaresapp.ui.navigation

import kotlinx.serialization.Serializable

/**
 * SISTEMA DE RUTAS uniLocal
 * 
 * Esta sealed class define todas las pantallas/rutas de navegación de la aplicación.
 * 
 * CONCEPTOS CLAVE:
 * - @Serializable: Permite que Kotlin Navigation Compose serialice automáticamente las rutas
 * - sealed class: Tipo restringido donde solo pueden existir las subclases definidas aquí
 * - object: Singleton - solo puede existir una instancia de cada ruta
 * 
 * BENEFICIOS:
 * - Type safety: El compilador garantiza que solo uses rutas válidas
 * - Autocompletado: El IDE sugiere automáticamente las rutas disponibles
 * - Refactoring seguro: Si cambias una ruta, el IDE actualiza todas las referencias
 * 
 * @param route: String - El identificador único de la ruta para Navigation Compose
 */
@Serializable
sealed class RouteScreen(val route: String) {

    /**
     * PANTALLA PRINCIPAL DEL USUARIO
     * Ruta: "home_user"
     * Descripción: Dashboard principal para usuarios con rol USER
     */
    @Serializable
    object HomeUser : RouteScreen("home_user")

    /**
     * PANTALLA PRINCIPAL DEL ADMINISTRADOR  
     * Ruta: "home_admin"
     * Descripción: Dashboard principal para usuarios con rol ADMIN
     */
    @Serializable
    object HomeAdmin : RouteScreen("home_admin")

    /**
     * PANTALLA DE LOGIN/AUTENTICACIÓN
     * Ruta: "login" 
     * Descripción: Pantalla inicial para autenticación de usuarios
     */
    @Serializable
    object Login : RouteScreen("login")

    /**
     * PANTALLA DE REGISTRO
     * Ruta: "register"
     * Descripción: Formulario para registrar nuevos usuarios
     */
    @Serializable
    object Register : RouteScreen("register")

    /**
     * PANTALLA DE RECUPERACIÓN DE CONTRASEÑA
     * Ruta: "password_recover"
     * Descripción: Formulario para recuperar contraseña olvidada
     */
    @Serializable
    object PasswordRecover : RouteScreen("password_recover")

    /**
     * PANTALLA DE EDICIÓN DE PERFIL
     * Ruta: "edit_profile"
     * Descripción: Formulario para que usuarios editen su información personal
     */
    @Serializable
    object EditProfile : RouteScreen("edit_profile")

    /**
     * PANTALLA DE CREACIÓN DE LUGAR
     * Ruta: "create_place"
     * Descripción: Formulario para que usuarios creen/registren nuevos lugares
     */
    @Serializable
    object CreatePlace : RouteScreen("create_place")

}