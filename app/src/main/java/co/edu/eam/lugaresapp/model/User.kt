package co.edu.eam.lugaresapp.model

/**
 * MODELO DE DATOS: USUARIO uniLocal
 * 
 * Esta data class representa un usuario en el sistema uniLocal.
 * 
 * CONCEPTOS CLAVE:
 * - data class: Clase especial de Kotlin que automáticamente genera:
 *   * equals() y hashCode() para comparación
 *   * toString() para representación en string  
 *   * copy() para crear copias con cambios
 *   * componentN() functions para destructuring
 * 
 * - val: Propiedades inmutables (solo lectura)
 * - Todas las propiedades son requeridas (no opcionales)
 * 
 * ESTRUCTURA DE USUARIO:
 * @param id Identificador único del usuario
 * @param name Nombre completo del usuario
 * @param username Nombre de usuario para login
 * @param role Rol del usuario - define permisos
 * @param city Ciudad de residencia del usuario
 * @param email Email único del usuario
 * @param password Contraseña del usuario
 * @param favorites Lista de IDs de lugares marcados como favoritos por el usuario
 */
data class User(
    val id: String,
    val name: String,
    val username: String,
    val role: Role,
    val city: String,
    val email: String,
    val password: String,
    val favorites: List<String> = emptyList()
)