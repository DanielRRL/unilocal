package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.eam.lugaresapp.model.Role
import co.edu.eam.lugaresapp.model.User
import co.edu.eam.lugaresapp.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    private val _reportResult = MutableStateFlow<RequestResult?>(null)
    val reportResult: StateFlow<RequestResult?> = _reportResult.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()


    val db = Firebase.firestore
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * INICIALIZADOR DEL VIEWMODEL
     * 
     * init {} se ejecuta cuando se crea una instancia del ViewModel.
     * Aquí cargamos los datos iniciales y creamos el usuario administrador si no existe.
     */
    init {
        loadUsers()
        initializeAdminUser()
    }

    // ==================== CARGA DE DATOS ====================

    /**
     * CARGA DE USUARIOS DESDE FIREBASE FIRESTORE
     * 
     * Esta función carga todos los usuarios existentes desde Firebase al iniciar la app.
     * 
     * FUNCIONAMIENTO:
     * - Se ejecuta en una coroutine (viewModelScope) para operación asíncrona
     * - Consulta la colección "users" en Firestore
     * - Usa addSnapshotListener para escuchar cambios en tiempo real
     * - Convierte los documentos de Firebase a objetos User usando toObject()
     * - Actualiza el StateFlow _users con la lista obtenida
     * - Maneja errores con try-catch y logging
     * 
     * SNAPSHOT LISTENER:
     * - Escucha cambios en la colección en tiempo real
     * - Se ejecuta automáticamente cuando hay cambios en Firebase
     * - Primera ejecución: carga datos iniciales
     * - Siguientes ejecuciones: actualiza cuando otros dispositivos modifican datos
     * 
     * MAPEO AUTOMÁTICO:
     * - Firebase usa toObject<User>() para mapear documentos a data classes
     * - Requiere que User tenga constructor sin argumentos (valores por defecto)
     * - Los nombres de campos en Firebase deben coincidir con las propiedades de User
     * 
     * LOGGING:
     * - Log.d() para éxito: muestra cantidad de usuarios cargados
     * - Log.e() para errores: muestra mensaje de error con stack trace
     */
    private fun loadUsers(){
        viewModelScope.launch {
            db.collection("users")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        android.util.Log.e("UsersViewModel", "Error: ${error.message}")
                        return@addSnapshotListener
                    }
                    
                    if (snapshot != null) {
                        val usersList = snapshot.documents.mapNotNull { 
                            doc -> doc.toObject(User::class.java)
                        }
                        _users.value = usersList
                    }
                }
        }
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

    /**
     * CREACIÓN DE NUEVO USUARIO EN FIREBASE
     * 
     * Esta función guarda un nuevo usuario en Firebase Firestore de forma asíncrona.
     * 
     * FUNCIONAMIENTO:
     * - Se ejecuta en una coroutine (viewModelScope) para no bloquear el hilo principal
     * - Actualiza _reportResult con el estado de la operación (Loading/Success/Failure)
     * - Usa runCatching para capturar y manejar errores de forma elegante
     * - Llama a createFirebase() que realiza la operación de guardado
     * 
     * ESTADOS DE _reportResult:
     * - RequestResult.Loading: Operación en progreso
     * - RequestResult.Success: Usuario guardado exitosamente en Firebase
     * - RequestResult.Failure: Error al guardar (contiene mensaje de error)
     * 
     * @param user User - El usuario a guardar en Firebase Firestore
     * 
     * NOTA: Esta función es asíncrona y no bloquea la ejecución
     */
    fun create(user: User){
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            
            try {
                createFirebase(user)
                _users.value = _users.value + user
                _reportResult.value = RequestResult.Success("Usuario guardado")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al guardar usuario")
                android.util.Log.e("UsersViewModel", "Error al guardar usuario: ${e.message}", e)
            }
        }
    }
    
    /**
     * GUARDADO DE USUARIO EN FIREBASE AUTH Y FIRESTORE
     * 
     * Función suspendida que crea el usuario en Firebase Auth y guarda sus datos en Firestore.
     * 
     * FUNCIONAMIENTO:
     * 1. Crea usuario en Firebase Auth con email y contraseña (Auth maneja el hashing)
     * 2. Obtiene el UID generado por Firebase Auth
     * 3. Guarda los datos del usuario en Firestore usando el UID como ID de documento
     * 4. NO guarda la contraseña en Firestore (solo existe en Firebase Auth)
     * 
     * SEGURIDAD:
     * - Firebase Auth maneja automáticamente el hashing de contraseñas
     * - La contraseña NUNCA se almacena en Firestore
     * - Solo el UID y datos de perfil se guardan en Firestore
     * 
     * @param user User - El usuario a crear (con contraseña en texto plano)
     * @throws Exception si hay error de conexión, email duplicado o permisos
     */
    private suspend fun createFirebase(user: User){
        // Crear usuario en Firebase Auth (Auth maneja el hashing automáticamente)
        val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        val uid = authResult.user?.uid ?: throw Exception("Error al obtener el UID del usuario creado")
        
        // Guardar datos del usuario en Firestore (SIN la contraseña)
        val userCopy = user.copy(
            id = uid,
            password = "" // NO guardar contraseña en Firestore
        )
        
        db.collection("users")
            .document(uid)
            .set(userCopy)
            .await()
    }

    /**
     * INICIALIZACIÓN AUTOMÁTICA DEL USUARIO ADMINISTRADOR
     * 
     * Crea el usuario administrador predeterminado si no existe en Firebase.
     * Se ejecuta automáticamente al inicializar el ViewModel.
     * 
     * CREDENCIALES DEL ADMIN:
     * - Email: admin@unilocal.com
     * - Password: Admin2025!
     * - Role: ADMIN
     * 
     * FUNCIONAMIENTO:
     * - Verifica si existe admin@unilocal.com en Firebase Auth
     * - Si no existe: crea el usuario en Auth y guarda datos en Firestore
     * - Si ya existe: no hace nada (idempotente)
     * - Maneja errores silenciosamente para no interrumpir la app
     */
    private fun initializeAdminUser() {
        viewModelScope.launch {
            try {
                // Intentar autenticar con credenciales de admin
                auth.signInWithEmailAndPassword("admin@unilocal.com", "Admin2025!").await()
                android.util.Log.d("UsersViewModel", "✅ Usuario administrador ya existe")
                
                // Cerrar sesión inmediatamente (solo verificamos existencia)
                auth.signOut()
                
            } catch (e: Exception) {
                // Si falla autenticación, el usuario no existe - crearlo
                try {
                    val adminUser = User(
                        id = "", // Se asignará automáticamente por createFirebase
                        name = "Administrador UniLocal",
                        username = "admin",
                        phone = "3001234567",
                        email = "admin@unilocal.com",
                        password = "Admin2025!",
                        department = "Quindío",
                        city = "Armenia",
                        role = Role.ADMIN,
                        favorites = emptyList()
                    )
                    
                    createFirebase(adminUser)
                    android.util.Log.d("UsersViewModel", "✅ Usuario administrador creado exitosamente")
                    
                    // Cerrar sesión para que el usuario pueda hacer login manualmente
                    auth.signOut()
                    
                } catch (createError: Exception) {
                    android.util.Log.e("UsersViewModel", "❌ Error al crear usuario administrador: ${createError.message}", createError)
                }
            }
        }
    }

    /**
     * BÚSQUEDA DE USUARIO POR ID EN FIREBASE
     * 
     * Busca un usuario específico en Firebase usando su ID único.
     * Actualiza _currentUser con el resultado y _reportResult con el estado.
     * 
     * FUNCIONAMIENTO:
     * - Ejecuta consulta asíncrona a Firebase
     * - Actualiza _reportResult (Loading/Success/Failure)
     * - Actualiza _currentUser con el usuario encontrado
     * - Maneja errores con logging apropiado
     * 
     * @param id String - ID único del usuario a buscar
     */
    fun findById(id: String) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            
            try {
                findByIdFirebase(id)
                _reportResult.value = RequestResult.Success("Usuario encontrado")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al buscar usuario")
                android.util.Log.e("UsersViewModel", "Error al buscar usuario: ${e.message}", e)
            }
        }
    }

    /**
     * CONSULTA FIREBASE PARA BUSCAR USUARIO POR ID
     * 
     * Función suspendida que consulta Firestore para obtener un usuario por ID.
     * 
     * @param id String - ID del documento en Firestore
     * @throws Exception si hay error de conexión o el documento no existe
     */
    private suspend fun findByIdFirebase(id: String) {
        val snapshot = db.collection("users")
            .document(id)
            .get()
            .await()

        val user = snapshot.toObject(User::class.java)?.apply {
            this.id = snapshot.id
        }

        _currentUser.value = user
    }

    /**
     * BUSCAR USUARIO EN LISTA LOCAL (HELPER)
     * 
     * Busca un usuario en la lista local cargada (no consulta Firebase).
     * Útil para búsquedas rápidas en UI sin hacer consultas adicionales.
     * 
     * @param id String - ID del usuario a buscar
     * @return User? - Usuario encontrado o null
     */
    fun getUserFromList(id: String): User? {
        return _users.value.find { it.id == id }
    }

    /**
     * FUNCIÓN DE LOGIN/AUTENTICACIÓN CON FIREBASE AUTH
     * 
     * Valida las credenciales del usuario contra Firebase Auth.
     * Esta es la función principal de autenticación de la aplicación.
     * 
     * FUNCIONAMIENTO:
     * 1. Autentica con Firebase Auth usando email y contraseña
     * 2. Firebase Auth valida las credenciales automáticamente
     * 3. Obtiene el UID del usuario autenticado
     * 4. Carga los datos del usuario desde Firestore
     * 5. Actualiza _currentUser con los datos
     * 
     * SEGURIDAD:
     * - Firebase Auth maneja automáticamente el hashing y validación
     * - Las contraseñas nunca se transmiten en texto plano
     * - Firebase Auth usa bcrypt para almacenar contraseñas
     * 
     * @param email String - Email del usuario
     * @param password String - Contraseña del usuario (en texto plano - Auth la hashea)
     * 
     * EJEMPLO DE USO:
     * ```
     * usersViewModel.login("daniel@email.com", "miPassword123")
     * // Observar reportResult para saber si fue exitoso
     * // Observar currentUser para obtener datos del usuario autenticado
     * ```
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            
            try {
                loginFirebase(email, password)
                
                if (_currentUser.value != null) {
                    _reportResult.value = RequestResult.Success("Inicio de sesión exitoso")
                } else {
                    _reportResult.value = RequestResult.Failure(errorMessage = "Error al cargar datos del usuario")
                }
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = "Email o contraseña incorrectos")
                android.util.Log.e("UsersViewModel", "Error al iniciar sesión: ${e.message}", e)
            }
        }
    }

    /**
     * AUTENTICACIÓN CON FIREBASE AUTH Y CARGA DE DATOS
     * 
     * Función suspendida que autentica al usuario con Firebase Auth
     * y carga sus datos desde Firestore.
     * 
     * FUNCIONAMIENTO:
     * 1. Autentica con Firebase Auth (valida email y contraseña)
     * 2. Obtiene el UID del usuario autenticado
     * 3. Carga los datos completos del usuario desde Firestore
     * 
     * @param email String - Email del usuario
     * @param password String - Contraseña en texto plano (Auth la hashea)
     * @throws Exception si las credenciales son incorrectas o hay error de conexión
     */
    private suspend fun loginFirebase(email: String, password: String) {
        // Autenticar con Firebase Auth
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario")
        
        // Cargar datos del usuario desde Firestore
        findByIdFirebase(uid)
    }

    // ==================== GESTIÓN DE FAVORITOS ====================

    /**
     * TOGGLE DE FAVORITO CON PERSISTENCIA EN FIREBASE
     * 
     * Alterna el estado de un lugar en la lista de favoritos de un usuario.
     * Si el lugar ya está en favoritos, lo remueve. Si no está, lo añade.
     * Guarda los cambios en Firebase automáticamente.
     * 
     * FUNCIONAMIENTO:
     * - Busca el usuario en la lista local
     * - Crea una lista mutable de sus favoritos
     * - Toggle: remueve si existe, añade si no existe
     * - Actualiza el usuario localmente (StateFlow)
     * - Sincroniza cambios con Firebase
     * - Maneja errores con logging apropiado
     * 
     * @param userId String - ID del usuario que marca/desmarca favorito
     * @param placeId String - ID del lugar a marcar/desmarcar como favorito
     */
    fun toggleFavorite(userId: String, placeId: String) {
        viewModelScope.launch {
            try {
                // Buscar usuario y actualizar localmente
                val updatedUser = _users.value.find { it.id == userId }?.let { user ->
                    val favs = user.favorites.toMutableList()
                    
                    // Toggle: remover si existe, añadir si no existe
                    if (favs.contains(placeId)) {
                        favs.remove(placeId)
                    } else {
                        favs.add(placeId)
                    }
                    
                    user.copy(favorites = favs)
                }
                
                if (updatedUser != null) {
                    // Actualizar lista local inmediatamente
                    _users.value = _users.value.map { user ->
                        if (user.id == userId) updatedUser else user
                    }
                    
                    // Sincronizar con Firebase
                    updateFavoritesFirebase(userId, updatedUser.favorites)
                }
            } catch (e: Exception) {
                android.util.Log.e("UsersViewModel", "Error al actualizar favoritos: ${e.message}", e)
            }
        }
    }

    /**
     * ACTUALIZAR FAVORITOS EN FIREBASE
     * 
     * Función suspendida que actualiza la lista de favoritos en Firestore.
     * 
     * @param userId String - ID del usuario
     * @param favorites List<String> - Nueva lista de favoritos
     * @throws Exception si hay error de conexión o permisos
     */
    private suspend fun updateFavoritesFirebase(userId: String, favorites: List<String>) {
        db.collection("users")
            .document(userId)
            .update("favorites", favorites)
            .await()
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
     * ACTUALIZAR DATOS DE USUARIO CON PERSISTENCIA EN FIREBASE
     * 
     * Permite actualizar los datos editables de un usuario existente.
     * NO permite modificar email ni contraseña (campos sensibles).
     * Guarda los cambios en Firebase automáticamente.
     * 
     * FUNCIONAMIENTO:
     * - Busca el usuario por ID
     * - Actualiza solo campos permitidos: name, username, phone, department, city
     * - Actualiza el StateFlow local inmediatamente
     * - Sincroniza cambios con Firebase
     * - Actualiza _reportResult con el estado de la operación
     * 
     * CAMPOS EDITABLES:
     * - name: Nombre completo del usuario
     * - username: Nombre de usuario único
     * - phone: Número de teléfono
     * - department: Departamento de residencia
     * - city: Ciudad de residencia
     * 
     * CAMPOS NO EDITABLES (razones de seguridad):
     * - email: Identificador único, cambio requeriría re-autenticación
     * - password: Cambio requiere validación especial y flujo seguro
     * - role: Solo administradores pueden modificar roles
     * - id: Inmutable por diseño
     * - favorites: Se gestiona con toggleFavorite()
     * 
     * @param userId String - ID del usuario a actualizar
     * @param name String - Nuevo nombre completo
     * @param username String - Nuevo username
     * @param phone String - Nuevo número de teléfono
     * @param department String - Nuevo departamento de residencia
     * @param city String - Nueva ciudad de residencia
     */
    fun updateUser(
        userId: String,
        name: String,
        username: String,
        phone: String,
        department: String,
        city: String
    ) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            
            try {
                // Actualizar localmente
                val updatedUser = _users.value.find { it.id == userId }?.copy(
                    name = name,
                    username = username,
                    phone = phone,
                    department = department,
                    city = city
                )
                
                if (updatedUser != null) {
                    // Actualizar lista local inmediatamente
                    _users.value = _users.value.map { user ->
                        if (user.id == userId) updatedUser else user
                    }
                    
                    // Sincronizar con Firebase
                    updateUserFirebase(userId, name, username, phone, department, city)
                    
                    _reportResult.value = RequestResult.Success("Perfil actualizado correctamente")
                } else {
                    _reportResult.value = RequestResult.Failure(errorMessage = "Usuario no encontrado")
                }
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al actualizar perfil")
                android.util.Log.e("UsersViewModel", "Error al actualizar usuario: ${e.message}", e)
            }
        }
    }

    /**
     * ACTUALIZAR USUARIO EN FIREBASE
     * 
     * Función suspendida que actualiza los campos editables en Firestore.
     * 
     * @param userId String - ID del usuario
     * @param name String - Nuevo nombre
     * @param username String - Nuevo username
     * @param phone String - Nuevo teléfono
     * @param department String - Nuevo departamento
     * @param city String - Nueva ciudad
     * @throws Exception si hay error de conexión o permisos
     */
    private suspend fun updateUserFirebase(
        userId: String,
        name: String,
        username: String,
        phone: String,
        department: String,
        city: String
    ) {
        db.collection("users")
            .document(userId)
            .update(
                mapOf(
                    "name" to name,
                    "username" to username,
                    "phone" to phone,
                    "department" to department,
                    "city" to city
                )
            )
            .await()
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

    /**
     * RESETEAR RESULTADO DE OPERACIÓN
     * 
     * Limpia el estado de _reportResult para permitir nuevas operaciones.
     * Útil para resetear la UI después de mostrar éxito o error.
     */
    fun resetOperationResult() {
        _reportResult.value = null
    }

    /**
     * Limpia el usuario actual (para logout).
     * 
     * Esta función resetea el currentUser a null, lo cual es necesario
     * durante el cierre de sesión para evitar auto-login.
     */
    fun clearCurrentUser() {
        _currentUser.value = null
    }

}