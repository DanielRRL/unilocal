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
 * FIREBASE FIRESTORE COMPATIBILITY:
 * - Constructor sin argumentos requerido para deserialización automática
 * - Valores por defecto permiten que Firebase cree instancias vacías
 * - Firebase mapea automáticamente los campos del documento a las propiedades
 * 
 * ESTRUCTURA DE USUARIO:
 * @param id Identificador único del usuario (UUID generado automáticamente)
 * @param name Nombre completo del usuario
 * @param username Nombre de usuario para login (único)
 * @param phone Número de teléfono del usuario
 * @param email Email único del usuario (usado también para login)
 * @param password Contraseña del usuario (hasheada con SHA-256)
 * @param department Departamento/Estado donde reside el usuario
 * @param city Ciudad de residencia del usuario (dentro del departamento)
 * @param role Rol del usuario - define permisos (USER o ADMIN)
 * @param favorites Lista de IDs de lugares marcados como favoritos por el usuario
 */
data class User(
    var id: String = "",
    val name: String = "",
    val username: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val department: String = "",
    val city: String = "",
    val role: Role = Role.USER,
    val favorites: List<String> = emptyList()
)