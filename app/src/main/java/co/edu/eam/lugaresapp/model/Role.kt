package co.edu.eam.lugaresapp.model

/**
 * ENUMERACIÓN DE ROLES uniLocal
 * 
 * Esta enum class define los diferentes tipos de usuarios en el sistema uniLocal.
 * 
 * CONCEPTOS CLAVE:
 * - enum class: Tipo especial que representa un conjunto fijo de constantes
 * - Type-safe: El compilador garantiza que solo se usen valores válidos
 * - Cada valor es una instancia singleton de la enum class
 * 
 * ROLES DISPONIBLES:
 * - ADMIN: Administrador con permisos completos
 *   * Puede gestionar lugares y usuarios
 *   * Acceso al dashboard administrativo
 *   * Funcionalidades de moderación
 * 
 * - USER: Usuario regular con permisos limitados  
 *   * Puede crear y gestionar sus propios lugares
 *   * Acceso al dashboard de usuario
 *   * Funcionalidades básicas de la app
 * 
 * USO EN LA APLICACIÓN:
 * - Determina qué pantalla mostrar después del login (HomeAdmin vs HomeUser)
 * - Controla el acceso a funcionalidades específicas
 * - Define los permisos y capacidades del usuario
 */
enum class Role {
    ADMIN,  // Administrador del sistema
    USER    // Usuario regular
}