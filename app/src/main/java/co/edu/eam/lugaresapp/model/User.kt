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
 * @param id: String - Identificador único del usuario 
 * @param name: String - Nombre completo del usuario
 * @param username: String - Nombre de usuario para login 
 * @param role: Role - Rol del usuario  - define permisos
 * @param city: String - Ciudad de residencia del usuario
 * @param email: String - Email único del usuario 
 * @param password: String - Contraseña del usuario 
 */
data class User(
    val id: String,        // Identificador único 
    val name: String,      // Nombre completo 
    val username: String,  // Username para login 
    val role: Role,        // Rol/permisos 
    val city: String,      // Ciudad 
    val email: String,     // Email único 
    val password: String   // Contraseña 
)