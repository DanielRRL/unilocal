package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import co.edu.eam.lugaresapp.model.Role
import co.edu.eam.lugaresapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * VIEWMODEL DE USUARIOS uniLocal
 * 
 * Este ViewModel maneja toda la lógica de negocio relacionada con usuarios.
 * Sigue el patrón MVVM (Model-View-ViewModel) recomendado por Google.
 * 
 * RESPONSABILIDADES:
 * - Gestión del estado de usuarios
 * - Lógica de autenticación (login)
 * - Creación de nuevos usuarios
 * - Exposición de datos a la UI mediante StateFlow
 * 
 * CONCEPTOS CLAVE:
 * - ViewModel: Sobrevive a cambios de configuración (rotación de pantalla)
 * - StateFlow: Stream reactivo que emite el estado actual y cambios
 * - MutableStateFlow: Versión mutable de StateFlow para modificar desde el ViewModel
 * - asStateFlow(): Convierte MutableStateFlow en StateFlow inmutable para la UI
 * 
 * PATRÓN ARQUITECTÓNICO:
 * UI (Composables) -> ViewModel -> Model (Data Classes)
 * Los Composables observan el StateFlow y se recomponen automáticamente cuando cambia
 */
class UsersViewModel: ViewModel(){

    /**
     * ESTADO PRIVADO DE USUARIOS
     * 
     * _users es el estado mutable privado que solo el ViewModel puede modificar.
     * MutableStateFlow<List<User>> mantiene la lista de usuarios y notifica cambios.
     * 
     * INICIALIZACIÓN: emptyList<User>() - Lista vacía de usuarios inicialmente
     */
    private val _users = MutableStateFlow(emptyList<User>())
    
    /**
     * ESTADO PÚBLICO PARA LA UI
     * 
     * users es la versión inmutable que los Composables pueden observar.
     * asStateFlow() convierte MutableStateFlow en StateFlow de solo lectura.
     * 
     * Los Composables que observen este StateFlow se recompondrán automáticamente
     * cuando la lista de usuarios cambie.
     */
    val users: StateFlow<List<User>> = _users.asStateFlow()

    /**
     * INICIALIZADOR DEL VIEWMODEL
     * 
     * init {} se ejecuta cuando se crea una instancia del ViewModel.
     * Aquí cargamos los datos iniciales de usuarios.
     */
    init {
        loadUsers()
    }

    /**
     * CARGA DE USUARIOS INICIALES
     * 
     * Esta función simula la carga de usuarios desde una base de datos o API.
     * En una app real, aquí haríamos llamadas a un repositorio o servicio web.
     * 
     * USUARIOS DE PRUEBA:
     * - admin@email.com / 123456 (ADMIN)
     * - carlos@email.com / 123456 (USER)
     */
    fun loadUsers(){
        _users.value = listOf(
            User(
                id = "1",
                name = "Admin",
                username = "admin", 
                role = Role.ADMIN,          // Usuario administrador
                city = "Armenia",
                email = "admin@email.com",
                password = "123456"        
            ),
            User(
                id = "2",
                name = "Daniel",
                username = "danielf",
                role = Role.USER,           // Usuario normal
                city = "Armenia", 
                email = "daniel@email.com",
                password = "123456"         
            )
        )
    }

    /**
     * CREACIÓN DE NUEVO USUARIO
     * 
     * Esta función añade un nuevo usuario a la lista existente.
     * 
     * FUNCIONAMIENTO:
     * - Toma la lista actual (_users.value)
     * - Le añade el nuevo usuario usando el operador + 
     * - Actualiza el StateFlow con la nueva lista
     * - Esto dispara automáticamente recomposición en la UI
     * 
     * @param user: User - El nuevo usuario a añadir
     */
    fun create(user: User){
        _users.value = _users.value + user // Concatena el nuevo usuario a la lista existente
    }

    /**
     * BÚSQUEDA DE USUARIO POR ID
     * 
     * Busca un usuario específico en la lista usando su ID único.
     * 
     * @param id: String - ID único del usuario
     * @return User? - El usuario encontrado o null si no existe
     */
    fun findById(id: String): User?{
        return _users.value.find { it.id == id } // find() retorna el primer elemento que cumple la condición
    }

    /**
     * BÚSQUEDA DE USUARIO POR EMAIL
     * 
     * Busca un usuario específico en la lista usando su email.
     * Útil para validar si un email ya está registrado.
     * 
     * @param email: String - Email del usuario
     * @return User? - El usuario encontrado o null si no existe
     */
    fun findByEmail(email: String): User?{
        return _users.value.find { it.email == email }
    }

    /**
     * FUNCIÓN DE LOGIN/AUTENTICACIÓN
     * 
     * Valida las credenciales del usuario (email y contraseña).
     * Esta es la función principal de autenticación de la aplicación.
     * 
     * FUNCIONAMIENTO:
     * - Busca un usuario que tenga el email Y la contraseña correctos
     * - Retorna el usuario si las credenciales son válidas
     * - Retorna null si las credenciales son incorrectas
     * 
     * @param email: String - Email del usuario
     * @param password: String - Contraseña del usuario
     * @return User? - Usuario autenticado o null si las credenciales son incorrectas
     */
    fun login(email: String, password: String): User?{
        return _users.value.find { it.email == email && it.password == password }
    }

    /**
     * CREACIÓN DE USUARIO DESDE FORMULARIO DE REGISTRO
     * 
     * Esta función maneja la lógica de registro de nuevos usuarios.
     * Incluye validación de email duplicado y generación de ID único.
     * 
     * VALIDACIONES:
     * - Verifica que el email no esté ya registrado
     * - Genera un ID único usando UUID
     * - Asigna rol USER por defecto
     * - Añade el usuario a la lista
     * 
     * @param name: String - Nombre del usuario
     * @param lastname: String - Apellido del usuario  
     * @param email: String - Email del usuario (debe ser único)
     * @param phone: String - Teléfono del usuario
     * @param password: String - Contraseña del usuario
     * @return Boolean - true si el registro fue exitoso, false si el email ya existe
     */
    fun createUser(name: String, lastname: String, email: String, phone: String, password: String): Boolean {
        // Verificar si el email ya está registrado
        if (_users.value.any { it.email == email }) {
            return false // Email duplicado, registro fallido
        }
        
        // Crear nuevo usuario con datos del formulario
        val newUser = User(
            id = java.util.UUID.randomUUID().toString(), // Genera ID único usando UUID
            name = "$name $lastname",        // Combina nombre y apellido  
            username = email,               // Usa email como username por simplicidad
            role = Role.USER,               // Todos los nuevos usuarios son USER (no ADMIN)
            city = "Armenia",               // Ciudad por defecto  
            email = email,                  // Email único del usuario
            password = password             // En producción esto se hashearía
        )
        
        // Añadir el nuevo usuario a la lista
        _users.value = _users.value + newUser
        return true // Registro exitoso
    }

    // ==================== GESTIÓN DE FAVORITOS ====================

    /**
     * TOGGLE DE FAVORITO
     * 
     * Alterna el estado de un lugar en la lista de favoritos de un usuario.
     * Si el lugar ya está en favoritos, lo remueve. Si no está, lo añade.
     * 
     * FUNCIONAMIENTO:
     * - Busca el usuario por ID
     * - Crea una lista mutable de sus favoritos
     * - Si el placeId existe, lo remueve
     * - Si no existe, lo añade
     * - Actualiza el usuario con la nueva lista de favoritos
     * - Reasigna el StateFlow completo para mantener inmutabilidad
     * 
     * PATRÓN DE INMUTABILIDAD:
     * - No modifica objetos directamente
     * - Usa copy() para crear nuevas instancias
     * - Reasigna _users.value completamente
     * - Esto garantiza que StateFlow detecte el cambio y notifique a la UI
     * 
     * @param userId: String - ID del usuario que marca/desmarca favorito
     * @param placeId: String - ID del lugar a marcar/desmarcar como favorito
     * 
     * EJEMPLO DE USO:
     * ```
     * // Usuario marca lugar como favorito
     * usersViewModel.toggleFavorite("2", "place123")
     * 
     * // Usuario desmarca el mismo lugar (segundo click)
     * usersViewModel.toggleFavorite("2", "place123")
     * ```
     */
    fun toggleFavorite(userId: String, placeId: String) {
        _users.value = _users.value.map { user ->
            if (user.id == userId) {
                // Crear lista mutable de favoritos para modificarla
                val favs = user.favorites.toMutableList()
                
                // Toggle: remover si existe, añadir si no existe
                if (favs.contains(placeId)) {
                    favs.remove(placeId)
                } else {
                    favs.add(placeId)
                }
                
                // Retornar copia del usuario con lista actualizada
                user.copy(favorites = favs)
            } else {
                // Mantener otros usuarios sin cambios
                user
            }
        }
    }

    /**
     * OBTENER LISTA DE FAVORITOS
     * 
     * Retorna la lista de IDs de lugares favoritos de un usuario específico.
     * 
     * FUNCIONAMIENTO:
     * - Busca el usuario por ID
     * - Si existe, retorna su lista de favoritos
     * - Si no existe, retorna lista vacía
     * 
     * Esta función es útil para:
     * - Mostrar sección "Mis Favoritos" en la UI
     * - Verificar si un lugar específico está en favoritos
     * - Obtener conteo de favoritos del usuario
     * 
     * @param userId: String - ID del usuario
     * @return List<String> - Lista de IDs de lugares favoritos (vacía si usuario no existe)
     * 
     * EJEMPLO DE USO:
     * ```
     * val favoriteIds = usersViewModel.getFavorites("2")
     * val favoritePlaces = favoriteIds.map { id -> 
     *     placesViewModel.findById(id) 
     * }
     * 
     * // Verificar si un lugar está en favoritos
     * val isFavorite = usersViewModel.getFavorites("2").contains("place123")
     * ```
     */
    fun getFavorites(userId: String): List<String> {
        return _users.value.find { it.id == userId }?.favorites ?: emptyList()
    }

    // ==================== ACTUALIZACIÓN DE PERFIL ====================

    /**
     * ACTUALIZAR DATOS DE USUARIO
     * 
     * Permite actualizar los datos editables de un usuario existente.
     * NO permite modificar email ni contraseña (campos sensibles).
     * 
     * FUNCIONAMIENTO:
     * - Busca el usuario por ID
     * - Actualiza solo los campos permitidos: name, username, city
     * - Mantiene sin cambios: id, email, password, role, favorites
     * - Usa copy() para crear una nueva instancia inmutable
     * - Actualiza el StateFlow completo para notificar cambios a la UI
     * 
     * CAMPOS EDITABLES:
     * - name: Nombre completo del usuario
     * - username: Nombre de usuario único
     * - city: Ciudad de residencia
     * 
     * CAMPOS NO EDITABLES (razones de seguridad):
     * - email: Identificador único, cambio requeriría re-autenticación
     * - password: Cambio requiere validación especial y flujo seguro
     * - role: Solo administradores pueden modificar roles
     * - id: Inmutable por diseño
     * - favorites: Se gestiona con toggleFavorite()
     * 
     * @param userId: String - ID del usuario a actualizar
     * @param name: String - Nuevo nombre completo
     * @param username: String - Nuevo username
     * @param city: String - Nueva ciudad
     * 
     * EJEMPLO DE USO:
     * ```
     * usersViewModel.updateUser(
     *     userId = "2",
     *     name = "Daniel Fernando",
     *     username = "danifernando",
     *     city = "Bogotá"
     * )
     * ```
     */
    fun updateUser(
        userId: String,
        name: String,
        username: String,
        city: String
    ) {
        _users.value = _users.value.map { user ->
            if (user.id == userId) {
                // Actualizar solo campos permitidos
                user.copy(
                    name = name,
                    username = username,
                    city = city
                    // email, password, role, id, favorites se mantienen sin cambios
                )
            } else {
                // Mantener otros usuarios sin cambios
                user
            }
        }
    }

    /**
     * VERIFICAR SI EMAIL EXISTE
     * 
     * Comprueba si un email ya está registrado en el sistema.
     * Útil para validaciones en el formulario de registro.
     * 
     * FUNCIONAMIENTO:
     * - Compara emails ignorando mayúsculas/minúsculas
     * - Retorna true si encuentra coincidencia
     * - Retorna false si el email está disponible
     * 
     * @param email: String - Email a verificar
     * @return Boolean - true si el email ya existe, false si está disponible
     * 
     * EJEMPLO DE USO:
     * ```
     * if (usersViewModel.existsByEmail("nuevo@email.com")) {
     *     // Mostrar error: "Este email ya está registrado"
     * } else {
     *     // Continuar con el registro
     * }
     * ```
     */
    fun existsByEmail(email: String): Boolean {
        return _users.value.any { it.email.equals(email, ignoreCase = true) }
    }

}