# UniLocal - Android Application

## Descripción del Proyecto

UniLocal es una aplicación Android nativa desarrollada en Kotlin utilizando Jetpack Compose para la interfaz de usuario. La aplicación permite la gestión de lugares y reseñas, con funcionalidades diferenciadas para usuarios regulares y administradores.

## Información Técnica

### Plataforma y Versiones

- **Lenguaje**: Kotlin
- **SDK Mínimo**: Android 9.0 (API 28)
- **SDK Objetivo**: Android 14 (API 36)
- **Versión**: 1.0
- **Build Tool**: Gradle con Kotlin DSL
- **Java Version**: 11

### Tecnologías y Frameworks

#### Core
- Jetpack Compose - Framework de UI declarativa
- Material Design 3 - Sistema de diseño
- Kotlin Coroutines - Programación asíncrona
- StateFlow - Gestión de estado reactivo

#### Jetpack Components
- Navigation Compose - Sistema de navegación
- ViewModel - Gestión de estado UI
- Lifecycle Runtime - Manejo de ciclo de vida
- Activity Compose - Integración de Compose con Activities

#### Dependencias Adicionales
- Kotlinx Serialization JSON - Serialización de datos
- Coil Compose - Carga de imágenes
- Material Icons Extended - Conjunto extendido de iconos

## Estructura del Proyecto

```
unilocal/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/co/edu/eam/lugaresapp/
│   │       │   ├── MainActivity.kt
│   │       │   ├── data/
│   │       │   │   └── SessionManager.kt
│   │       │   ├── model/
│   │       │   │   ├── Location.kt
│   │       │   │   ├── Place.kt
│   │       │   │   ├── PlaceType.kt
│   │       │   │   ├── Review.kt
│   │       │   │   ├── Role.kt
│   │       │   │   ├── Schedule.kt
│   │       │   │   └── User.kt
│   │       │   ├── ui/
│   │       │   │   ├── admin/
│   │       │   │   │   ├── HomeAdmin.kt
│   │       │   │   │   ├── bottombar/
│   │       │   │   │   │   └── BottomBarAdmin.kt
│   │       │   │   │   ├── nav/
│   │       │   │   │   │   ├── AdminScreen.kt
│   │       │   │   │   │   └── ContentAdmin.kt
│   │       │   │   │   └── screens/
│   │       │   │   │       ├── HistoryScreen.kt
│   │       │   │   │       └── PlacesListScreen.kt
│   │       │   │   ├── auth/
│   │       │   │   │   ├── LoginScreen.kt
│   │       │   │   │   ├── PasswordRecoverScreen.kt
│   │       │   │   │   └── RegisterScreen.kt
│   │       │   │   ├── components/
│   │       │   │   │   ├── DropdownMenu.kt
│   │       │   │   │   └── InputText.kt
│   │       │   │   ├── navigation/
│   │       │   │   │   ├── Navigation.kt
│   │       │   │   │   └── RouteScreen.kt
│   │       │   │   ├── places/
│   │       │   │   │   └── CreatePlaceScreen.kt
│   │       │   │   ├── theme/
│   │       │   │   │   ├── Color.kt
│   │       │   │   │   ├── Theme.kt
│   │       │   │   │   └── Type.kt
│   │       │   │   └── user/
│   │       │   │       ├── HomeUser.kt
│   │       │   │       ├── bottombar/
│   │       │   │       │   └── BottomBarUser.kt
│   │       │   │       ├── nav/
│   │       │   │       │   ├── ContentUser.kt
│   │       │   │       │   └── UserScreen.kt
│   │       │   │       └── screens/
│   │       │   │           ├── EditProfileScreen.kt
│   │       │   │           ├── MapScreen.kt
│   │       │   │           ├── PlaceDetailScreen.kt
│   │       │   │           ├── PlacesScreen.kt
│   │       │   │           ├── ProfileScreen.kt
│   │       │   │           └── SearchScreen.kt
│   │       │   └── viewmodel/
│   │       │       ├── PlacesViewModel.kt
│   │       │       ├── RewiewsViewModel.kt
│   │       │       └── UsersViewModel.kt
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   ├── mipmap-*/
│   │       │   ├── values/
│   │       │   └── xml/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── local.properties
```

## Arquitectura de la Aplicación

### Patrón Arquitectónico

La aplicación sigue el patrón **MVVM (Model-View-ViewModel)** recomendado por Google para aplicaciones Android:

- **Model**: Clases de datos (data classes) ubicadas en el paquete `model/`
- **View**: Composables UI ubicados en el paquete `ui/`
- **ViewModel**: Lógica de negocio y gestión de estado en el paquete `viewmodel/`

### Capas de la Aplicación

#### 1. Capa de Datos (Data Layer)

**Ubicación**: `co.edu.eam.lugaresapp.data`

Responsable de la persistencia y gestión de datos.

**Componentes**:
- `SessionManager.kt`: Gestión de sesión de usuario mediante SharedPreferences

#### 2. Capa de Modelo (Model Layer)

**Ubicación**: `co.edu.eam.lugaresapp.model`

Define las estructuras de datos de la aplicación.

**Modelos**:
- `User`: Información de usuario (id, name, username, role, city, email, password)
- `Role`: Enumeración de roles (ADMIN, USER)
- `Place`: Información de lugares
- `PlaceType`: Tipos de lugares
- `Review`: Reseñas de lugares
- `Location`: Coordenadas geográficas
- `Schedule`: Horarios de lugares

#### 3. Capa de ViewModel

**Ubicación**: `co.edu.eam.lugaresapp.viewmodel`

Gestiona la lógica de negocio y el estado de la aplicación.

**ViewModels**:
- `UsersViewModel`: Gestión de usuarios, autenticación y registro
- `PlacesViewModel`: Gestión de lugares
- `RewiewsViewModel`: Gestión de reseñas

#### 4. Capa de Presentación (UI Layer)

**Ubicación**: `co.edu.eam.lugaresapp.ui`

Contiene todos los componentes visuales de la aplicación.

**Estructura**:

##### Autenticación (`ui/auth/`)
- `LoginScreen`: Pantalla de inicio de sesión
- `RegisterScreen`: Pantalla de registro de nuevos usuarios
- `PasswordRecoverScreen`: Pantalla de recuperación de contraseña

##### Navegación (`ui/navigation/`)
- `Navigation`: Configuración principal de navegación
- `RouteScreen`: Definición de rutas de la aplicación

##### Componentes Compartidos (`ui/components/`)
- `InputText`: Campo de texto reutilizable
- `DropdownMenu`: Menú desplegable personalizado

##### Interfaz de Usuario (`ui/user/`)
- `HomeUser`: Pantalla principal para usuarios regulares
- `BottomBarUser`: Barra de navegación inferior
- Pantallas específicas: Places, Search, Profile, Map, PlaceDetail, EditProfile

##### Interfaz de Administrador (`ui/admin/`)
- `HomeAdmin`: Pantalla principal para administradores
- `BottomBarAdmin`: Barra de navegación inferior de admin
- Pantallas específicas: PlacesList, History

##### Gestión de Lugares (`ui/places/`)
- `CreatePlaceScreen`: Pantalla para crear nuevos lugares

##### Tema (`ui/theme/`)
- `Color`: Definición de colores de la aplicación
- `Theme`: Configuración del tema Material 3
- `Type`: Tipografía personalizada

## Implementaciones Recientes

### Step 1: SessionManager y Auto-Login

#### Archivos Creados

**1. SessionManager.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/data/SessionManager.kt`

Clase responsable de la gestión de sesión de usuario mediante SharedPreferences.

**Métodos Públicos**:
```kotlin
fun saveUserId(userId: String)  // Guarda el ID del usuario en sesión
fun getUserId(): String?         // Obtiene el ID del usuario activo (null si no hay sesión)
fun clear()                      // Limpia la sesión del usuario (logout)
```

**Configuración**:
- SharedPreferences name: `"unilocal_prefs"`
- Key para userId: `"current_user_id"`

**Características**:
- Persistencia simple y ligera
- Operaciones sincrónicas con `.apply()` para escritura asíncrona
- Thread-safe por naturaleza de SharedPreferences
- Acceso en modo privado (MODE_PRIVATE)

#### Archivos Modificados

**1. Navigation.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/ui/navigation/Navigation.kt`

**Cambios realizados**:

##### Imports añadidos:
```kotlin
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Role
```

##### Funcionalidad de Auto-Login:

Se implementó la detección automática de sesión activa mediante `LaunchedEffect`:

```kotlin
val context = LocalContext.current
val sessionManager = SessionManager(context)

LaunchedEffect(Unit) {
    val currentUserId = sessionManager.getUserId()
    if (currentUserId != null) {
        val user = usersViewModel.findById(currentUserId)
        if (user != null) {
            val destination = when (user.role) {
                Role.ADMIN -> RouteScreen.HomeAdmin.route
                Role.USER -> RouteScreen.HomeUser.route
            }
            navController.navigate(destination) {
                popUpTo(RouteScreen.Login.route) { inclusive = true }
            }
        } else {
            sessionManager.clear()
        }
    }
}
```

**Comportamiento**:
1. Se ejecuta una sola vez al inicializar la navegación (clave: `Unit`)
2. Obtiene el userId guardado en SharedPreferences
3. Si existe sesión válida, busca el usuario en el ViewModel
4. Navega automáticamente a HomeAdmin o HomeUser según el rol
5. Limpia el backstack para prevenir navegación incorrecta
6. Si el usuario no existe, limpia la sesión automáticamente

---

### Step 2: Extensión de Modelos de Datos

Se extendieron los modelos de datos principales para soportar funcionalidades de moderación, propiedad de lugares y favoritos, manteniendo compatibilidad retroactiva mediante valores por defecto.

#### Archivos Modificados

**1. Place.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/model/Place.kt`

**Campos Añadidos**:

```kotlin
val approved: Boolean = false              // Moderación: indica si el lugar ha sido aprobado
val ownerId: String? = null                // ID del usuario propietario del lugar
val createdAt: Long = System.currentTimeMillis()  // Timestamp de creación
```

**Propósito**:
- `approved`: Permite implementar un sistema de moderación donde los administradores deben aprobar lugares antes de que sean visibles públicamente
- `ownerId`: Vincula un lugar con su propietario, permitiendo funcionalidades como edición exclusiva y respuesta a reseñas
- `createdAt`: Facilita ordenamiento por fecha de creación y auditoría

**Compatibilidad**: Los tres campos tienen valores por defecto, por lo que cualquier código existente que cree instancias de `Place` sin estos campos seguirá funcionando sin modificaciones.

**2. User.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/model/User.kt`

**Campo Añadido**:

```kotlin
val favorites: List<String> = emptyList()  // IDs de lugares favoritos del usuario
```

**Propósito**:
- Permite a los usuarios marcar lugares como favoritos
- Facilita la creación de una sección "Mis Favoritos" en la interfaz
- Los IDs se almacenan como lista de Strings (referencias a Place.id)

**Compatibilidad**: El campo tiene una lista vacía por defecto, manteniendo compatibilidad con código existente.

**3. Review.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/model/Review.kt`

**Campo Añadido**:

```kotlin
val ownerResponse: String? = null  // Respuesta del propietario a la reseña
```

**Propósito**:
- Permite a los propietarios de lugares responder a las reseñas de usuarios
- Mejora la interacción entre propietarios y clientes
- Es opcional (nullable) ya que no todas las reseñas tendrán respuesta

**Compatibilidad**: Al ser nullable con valor por defecto `null`, no afecta código existente.

#### Beneficios de la Implementación

**Seguridad y Control**:
- Sistema de moderación para prevenir contenido inapropiado
- Vinculación clara de propiedad de lugares

**Mejora de UX**:
- Usuarios pueden guardar lugares favoritos
- Propietarios pueden responder a feedback
- Mejor organización temporal de contenido

**Escalabilidad**:
- Base para implementar notificaciones (nuevos lugares, respuestas a reseñas)
- Soporte para estadísticas y reportes
- Preparación para sistema de permisos más granular

---

### Step 3: CRUD de Lugares y Sistema de Moderación

Se implementó un sistema completo de CRUD para lugares y un sistema de moderación con auditoría de acciones.

#### Archivos Creados

**1. ModerationRecord.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/model/ModerationRecord.kt`

Modelo de datos para registrar acciones de moderación realizadas sobre lugares.

**Estructura**:
```kotlin
data class ModerationRecord(
    val id: String,              // ID único del registro
    val placeId: String,         // ID del lugar moderado
    val moderatorId: String,     // ID del moderador
    val action: String,          // Tipo de acción (APPROVE, REJECT)
    val timestamp: Long,         // Timestamp de la acción
    val reason: String? = null   // Razón opcional (útil para rechazos)
)
```

**Propósito**:
- Auditoría completa de acciones de moderación
- Trazabilidad de quién aprobó/rechazó qué lugar y cuándo
- Historial para reportes y análisis

#### Archivos Modificados

**1. PlacesViewModel.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/viewmodel/PlacesViewModel.kt`

Se extendió significativamente el ViewModel con funcionalidades CRUD y de moderación.

**Estado Agregado**:
```kotlin
private val _moderationRecords = MutableStateFlow<List<ModerationRecord>>(emptyList())
val moderationRecords: StateFlow<List<ModerationRecord>> = _moderationRecords.asStateFlow()
```

**Funciones CRUD Implementadas**:

1. **addPlace(place: Place)**
   - Añade un nuevo lugar a la lista
   - Mantiene el estado de aprobación del lugar
   - Uso: `placesViewModel.addPlace(newPlace)`

2. **deletePlace(placeId: String)**
   - Elimina un lugar por ID usando filter
   - Mantiene inmutabilidad de la lista
   - Uso: `placesViewModel.deletePlace("place123")`

3. **create(place: Place)** (Legacy)
   - Función mantenida por compatibilidad
   - Internamente llama a addPlace
   - Permite código existente continuar funcionando

**Funciones de Moderación Implementadas**:

1. **getPendingPlaces(): List<Place>**
   - Retorna lugares no aprobados (approved = false)
   - Útil para pantallas de moderación de administradores
   - Uso: `val pending = placesViewModel.getPendingPlaces()`

2. **approvePlace(placeId: String, moderatorId: String)**
   - Marca lugar como aprobado (approved = true)
   - Crea registro de moderación con action="APPROVE"
   - Actualiza StateFlow automáticamente
   - Uso: `placesViewModel.approvePlace("place123", "admin1")`

3. **rejectPlace(placeId: String, moderatorId: String, reason: String?)**
   - Marca lugar como rechazado (approved = false)
   - Crea registro con action="REJECT" y razón opcional
   - Permite especificar motivo del rechazo
   - Uso: `placesViewModel.rejectPlace("place123", "admin1", "Contenido inapropiado")`

4. **getApprovedPlaces(): List<Place>**
   - Retorna solo lugares aprobados
   - Útil para mostrar contenido público a usuarios
   - Uso: `val approved = placesViewModel.getApprovedPlaces()`

**Función Privada de Auditoría**:

```kotlin
private fun addModerationRecord(
    placeId: String,
    moderatorId: String,
    action: String,
    reason: String?
)
```
- Genera ID único usando UUID
- Captura timestamp automáticamente con System.currentTimeMillis()
- Añade registro a _moderationRecords StateFlow

**Datos de Prueba Actualizados**:
- Lugar 1 (Restaurante El Paisa): approved = true (pre-aprobado)
- Lugares 2-6 (Bares de prueba): approved = false (pendientes de moderación)

#### Principios de Diseño Aplicados

**Inmutabilidad**:
- Todas las operaciones usan `copy()` y reasignación
- No se modifican objetos existentes directamente
- Pattern: `list.map { if (condition) item.copy(...) else item }`

**Reactividad**:
- StateFlow notifica cambios automáticamente
- UI se actualiza cuando cambian places o moderationRecords
- No se requieren callbacks manuales

**Separación de Responsabilidades**:
- CRUD: Operaciones básicas de datos
- Moderación: Lógica de negocio específica
- Búsqueda: Funciones de filtrado y consulta
- Auditoría: Registro automático de acciones

**Sin Efectos Secundarios**:
- Todo permanece en memoria (ViewModel)
- No hay I/O o llamadas a red en este step
- Preparado para integración futura con Repository

#### Casos de Uso Habilitados

**Para Administradores**:
1. Ver lugares pendientes de aprobación
2. Aprobar lugares con registro de auditoría
3. Rechazar lugares especificando razón
4. Ver historial completo de moderación
5. Eliminar lugares inapropiados

**Para Usuarios**:
1. Crear nuevos lugares (que entran en cola de moderación)
2. Ver solo lugares aprobados
3. Sistema transparente: no ven lugares sin aprobar

**Para Auditoría**:
1. Historial completo de acciones de moderación
2. Identificación de moderador responsable
3. Timestamps para análisis temporal
4. Razones de rechazo documentadas

#### Ejemplos de Uso

**Ejemplo 1: Crear y Aprobar un Lugar**
```kotlin
// En una pantalla de administrador
@Composable
fun ModerationScreen(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel
) {
    val pendingPlaces by placesViewModel.getPendingPlaces().collectAsState()
    val currentUser = usersViewModel.currentUser // Asume admin logueado
    
    LazyColumn {
        items(pendingPlaces) { place ->
            PlaceCard(place) {
                Row {
                    Button(onClick = {
                        placesViewModel.approvePlace(place.id, currentUser.id)
                    }) {
                        Text("Aprobar")
                    }
                    
                    Button(onClick = {
                        placesViewModel.rejectPlace(
                            placeId = place.id,
                            moderatorId = currentUser.id,
                            reason = "Información incompleta"
                        )
                    }) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }
}
```

**Ejemplo 2: Crear Nuevo Lugar**
```kotlin
// En CreatePlaceScreen
fun onCreatePlace() {
    val newPlace = Place(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description,
        address = address,
        location = location,
        images = imageUrls,
        phones = phones,
        type = selectedType,
        schedules = schedules,
        approved = false,  // Pendiente de moderación
        ownerId = currentUserId,
        createdAt = System.currentTimeMillis()
    )
    
    placesViewModel.addPlace(newPlace)
    // El lugar entra en cola de moderación automáticamente
}
```

**Ejemplo 3: Ver Historial de Moderación**
```kotlin
@Composable
fun ModerationHistoryScreen(placesViewModel: PlacesViewModel) {
    val records by placesViewModel.moderationRecords.collectAsState()
    
    LazyColumn {
        items(records.sortedByDescending { it.timestamp }) { record ->
            Card {
                Column {
                    Text("Lugar: ${record.placeId}")
                    Text("Acción: ${record.action}")
                    Text("Moderador: ${record.moderatorId}")
                    Text("Fecha: ${formatTimestamp(record.timestamp)}")
                    record.reason?.let { Text("Razón: $it") }
                }
            }
        }
    }
}
```

**Ejemplo 4: Filtrar Lugares para Usuarios**
```kotlin
@Composable
fun PlacesListScreen(placesViewModel: PlacesViewModel) {
    // Los usuarios regulares solo ven lugares aprobados
    val approvedPlaces by placesViewModel.getApprovedPlaces().collectAsState()
    
    LazyColumn {
        items(approvedPlaces) { place ->
            PlaceCard(place, onClick = { /* navegar a detalle */ })
        }
    }
}
```

---

### Step 4: Sistema de Reseñas y Valoraciones

Se implementó un ViewModel completo para la gestión de reseñas, comentarios y valoraciones de lugares, con funcionalidades de respuesta de propietarios.

#### Archivos Modificados

**1. RewiewsViewModel.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/viewmodel/RewiewsViewModel.kt`

Se transformó de una clase vacía a un ViewModel completo con gestión de reseñas.

**Estado Implementado**:
```kotlin
private val _reviews = MutableStateFlow<List<Review>>(emptyList())
val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()
```

**Funciones CRUD Implementadas**:

1. **addReview(review: Review)**
   - Añade una nueva reseña a la lista
   - Notifica cambios automáticamente vía StateFlow
   - Uso: `rewiewsViewModel.addReview(newReview)`

2. **deleteReview(reviewId: String)**
   - Elimina una reseña por ID
   - Útil para moderación de contenido
   - Mantiene inmutabilidad usando filter
   - Uso: `rewiewsViewModel.deleteReview("review123")`

**Funciones de Interacción**:

1. **findByPlaceId(placeId: String): List<Review>**
   - Retorna todas las reseñas de un lugar específico
   - Útil para pantalla de detalle de lugar
   - No modifica estado interno
   - Uso: `val reviews = rewiewsViewModel.findByPlaceId("place1")`

2. **replyToReview(reviewId: String, response: String)**
   - Permite a propietarios responder reseñas
   - Actualiza campo ownerResponse usando copy()
   - Mantiene inmutabilidad total
   - Uso: `rewiewsViewModel.replyToReview("review1", "Gracias!")`

**Funciones de Consulta Avanzada**:

1. **findById(reviewId: String): Review?**
   - Busca reseña específica por ID

2. **findByUserId(userId: String): List<Review>**
   - Retorna historial de reseñas de un usuario

3. **getAverageRating(placeId: String): Double**
   - Calcula promedio de calificación de un lugar
   - Retorna 0.0 si no hay reseñas

4. **getReviewCount(placeId: String): Int**
   - Cuenta número total de reseñas de un lugar

5. **getReviewsWithResponse(placeId: String? = null): List<Review>**
   - Filtra reseñas que tienen respuesta del propietario
   - Útil para análisis de engagement

6. **getPendingResponses(placeId: String): List<Review>**
   - Retorna reseñas sin respuesta del propietario
   - Útil para gestión de respuestas pendientes

7. **getRecentReviews(placeId: String, limit: Int = 10): List<Review>**
   - Retorna reseñas más recientes ordenadas por fecha
   - Límite configurable (por defecto 10)

**Datos de Prueba**:
```kotlin
- Review 1: Usuario "2" → Lugar "1" (Restaurante El Paisa)
  * Rating: 5, con respuesta del propietario
- Review 2: Usuario "2" → Lugar "1"
  * Rating: 4, sin respuesta
- Review 3: Usuario "2" → Lugar "2" (Bar test 1)
  * Rating: 3, sin respuesta
```

#### Principios de Diseño Aplicados

**Inmutabilidad**:
- Uso exclusivo de `copy()` para modificaciones
- Reasignación completa de listas
- No hay mutación directa de objetos

**Reactividad**:
- StateFlow notifica cambios automáticamente
- UI se actualiza sin intervención manual
- Patrón observer integrado

**Separación de Responsabilidades**:
- CRUD: Operaciones básicas (add, delete)
- Interacción: Respuestas y filtrado por lugar
- Análisis: Promedios, conteos, estadísticas
- Consulta: Búsquedas y filtros avanzados

**Extensibilidad**:
- Funciones auxiliares para casos de uso comunes
- API clara y bien documentada
- Preparado para integración con Repository

#### Casos de Uso Habilitados

**Para Usuarios Regulares**:
1. Agregar reseñas y valoraciones a lugares
2. Ver reseñas de otros usuarios
3. Ver respuestas de propietarios
4. Consultar historial propio de reseñas

**Para Propietarios de Lugares**:
1. Ver todas las reseñas de sus lugares
2. Responder a reseñas de clientes
3. Ver reseñas pendientes de respuesta
4. Análisis de satisfacción (promedio de rating)

**Para Administradores**:
1. Eliminar reseñas inapropiadas
2. Moderación de contenido
3. Ver estadísticas globales
4. Análisis de engagement

**Para Análisis y Reportes**:
1. Calcular promedios de calificación
2. Contar reseñas por lugar
3. Medir tasa de respuesta de propietarios
4. Identificar reseñas recientes

#### Ejemplos de Uso

**Ejemplo 1: Agregar Nueva Reseña**
```kotlin
@Composable
fun AddReviewScreen(
    placeId: String,
    userId: String,
    rewiewsViewModel: RewiewsViewModel
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    
    Button(onClick = {
        val newReview = Review(
            id = UUID.randomUUID().toString(),
            userID = userId,
            placeID = placeId,
            rating = rating,
            comment = comment,
            date = LocalDateTime.now()
        )
        rewiewsViewModel.addReview(newReview)
        // Navegar atrás o mostrar confirmación
    }) {
        Text("Publicar Reseña")
    }
}
```

**Ejemplo 2: Mostrar Reseñas de un Lugar**
```kotlin
@Composable
fun PlaceReviewsSection(
    placeId: String,
    rewiewsViewModel: RewiewsViewModel
) {
    val placeReviews = rewiewsViewModel.findByPlaceId(placeId)
    val averageRating = rewiewsViewModel.getAverageRating(placeId)
    val reviewCount = rewiewsViewModel.getReviewCount(placeId)
    
    Column {
        Text("Calificación: ${String.format("%.1f", averageRating)} ($reviewCount reseñas)")
        
        LazyColumn {
            items(placeReviews.sortedByDescending { it.date }) { review ->
                ReviewCard(review)
            }
        }
    }
}
```

**Ejemplo 3: Propietario Responde a Reseña**
```kotlin
@Composable
fun OwnerResponseDialog(
    review: Review,
    rewiewsViewModel: RewiewsViewModel
) {
    var response by remember { mutableStateOf("") }
    
    Dialog(onDismissRequest = { /* cerrar */ }) {
        Column {
            Text("Responder a: ${review.comment}")
            TextField(
                value = response,
                onValueChange = { response = it },
                placeholder = { Text("Escribe tu respuesta...") }
            )
            Button(onClick = {
                rewiewsViewModel.replyToReview(review.id, response)
                // Cerrar diálogo
            }) {
                Text("Enviar Respuesta")
            }
        }
    }
}
```

**Ejemplo 4: Panel de Gestión para Propietarios**
```kotlin
@Composable
fun OwnerReviewManagementScreen(
    placeId: String,
    rewiewsViewModel: RewiewsViewModel
) {
    val pendingResponses = rewiewsViewModel.getPendingResponses(placeId)
    val recentReviews = rewiewsViewModel.getRecentReviews(placeId, limit = 20)
    val averageRating = rewiewsViewModel.getAverageRating(placeId)
    
    Column {
        // Estadísticas
        Card {
            Text("Calificación Promedio: ${String.format("%.1f", averageRating)}")
            Text("Respuestas Pendientes: ${pendingResponses.size}")
        }
        
        // Reseñas pendientes
        if (pendingResponses.isNotEmpty()) {
            Text("Reseñas sin Responder", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(pendingResponses) { review ->
                    ReviewCardWithReplyButton(review, rewiewsViewModel)
                }
            }
        }
    }
}
```

**Ejemplo 5: Moderación de Reseñas (Admin)**
```kotlin
@Composable
fun AdminReviewModerationScreen(
    rewiewsViewModel: RewiewsViewModel
) {
    val allReviews by rewiewsViewModel.reviews.collectAsState()
    
    LazyColumn {
        items(allReviews.sortedByDescending { it.date }) { review ->
            ReviewCard(review) {
                Button(onClick = {
                    if (/* revisar si es inapropiada */) {
                        rewiewsViewModel.deleteReview(review.id)
                    }
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}
```

##### Funcionalidad de Auto-Login:

Se implementó la detección automática de sesión activa mediante `LaunchedEffect`:

```kotlin
val context = LocalContext.current
val sessionManager = SessionManager(context)

LaunchedEffect(Unit) {
    val currentUserId = sessionManager.getUserId()
    if (currentUserId != null) {
        val user = usersViewModel.findById(currentUserId)
        if (user != null) {
            val destination = when (user.role) {
                Role.ADMIN -> RouteScreen.HomeAdmin.route
                Role.USER -> RouteScreen.HomeUser.route
            }
            navController.navigate(destination) {
                popUpTo(RouteScreen.Login.route) { inclusive = true }
            }
        } else {
            sessionManager.clear()
        }
    }
}
```

**Comportamiento**:
1. Se ejecuta una sola vez al inicializar la navegación (clave: `Unit`)
2. Obtiene el userId guardado en SharedPreferences
3. Si existe sesión válida, busca el usuario en el ViewModel
4. Navega automáticamente a HomeAdmin o HomeUser según el rol
5. Limpia el backstack para prevenir navegación incorrecta
6. Si el usuario no existe, limpia la sesión automáticamente

---

### Step 5: Gestión de Favoritos en UsersViewModel

Se implementó la funcionalidad completa de favoritos, permitiendo a los usuarios marcar y desmarcar lugares como favoritos, con persistencia en el estado del ViewModel.

#### Archivos Modificados

**1. UsersViewModel.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/viewmodel/UsersViewModel.kt`

Se agregaron dos funciones públicas para la gestión de favoritos.

**Funciones Implementadas**:

1. **toggleFavorite(userId: String, placeId: String)**
   - Alterna el estado de un lugar en la lista de favoritos
   - Si el lugar ya está en favoritos, lo remueve
   - Si no está, lo añade
   - Mantiene inmutabilidad mediante `copy()` y reasignación completa
   
   **Funcionamiento Interno**:
   ```kotlin
   fun toggleFavorite(userId: String, placeId: String) {
       _users.value = _users.value.map { user ->
           if (user.id == userId) {
               val favs = user.favorites.toMutableList()
               if (favs.contains(placeId)) {
                   favs.remove(placeId)
               } else {
                   favs.add(placeId)
               }
               user.copy(favorites = favs)
           } else {
               user
           }
       }
   }
   ```
   
   **Características**:
   - Operación atómica: reasigna `_users.value` completamente
   - No modifica objetos existentes directamente
   - StateFlow notifica cambios automáticamente a la UI
   - Solo modifica el usuario específico, mantiene otros sin cambios

2. **getFavorites(userId: String): List<String>**
   - Retorna la lista de IDs de lugares favoritos de un usuario
   - Retorna lista vacía si el usuario no existe
   - Operación de solo lectura, no modifica estado
   
   **Funcionamiento Interno**:
   ```kotlin
   fun getFavorites(userId: String): List<String> {
       return _users.value.find { it.id == userId }?.favorites ?: emptyList()
   }
   ```
   
   **Características**:
   - Operación segura con null-safety (`?.` y `?:`)
   - No genera excepciones si el usuario no existe
   - Útil para consultas y validaciones

#### Principios de Diseño Aplicados

**Inmutabilidad Total**:
- Uso de `toMutableList()` para crear copia temporal
- Aplicación de `copy()` para crear nueva instancia de User
- Reasignación completa de `_users.value`
- Patrón: `_users.value = _users.value.map { ... }`

**Atomicidad**:
- Una sola asignación a `_users.value`
- StateFlow garantiza que los observadores reciban el estado completo
- No hay estados intermedios visibles

**Encapsulación**:
- `_users` es privado (MutableStateFlow)
- `users` es público de solo lectura (StateFlow)
- Modificaciones solo a través de funciones públicas del ViewModel

**Eficiencia**:
- `map()` recorre la lista una sola vez
- Solo crea nuevas instancias del usuario modificado
- Otros usuarios se mantienen por referencia (sin copia)

#### Casos de Uso Habilitados

**Para Usuarios Regulares**:
1. Marcar lugares como favoritos desde PlaceDetailScreen
2. Desmarcar lugares de favoritos con el mismo botón (toggle)
3. Ver sección "Mis Favoritos" en HomeUser
4. Acceso rápido a lugares guardados

**Para la UI**:
1. Actualización automática del ícono de favorito (corazón lleno/vacío)
2. Sincronización entre múltiples pantallas
3. Contador de favoritos en perfil
4. Lista filtrada de lugares favoritos

**Para Análisis**:
1. Identificar lugares más guardados como favoritos
2. Personalización de recomendaciones
3. Estadísticas de engagement de usuario
4. Identificar lugares populares sin reseñas

#### Ejemplos de Uso

**Ejemplo 1: Marcar/Desmarcar Favorito desde PlaceDetailScreen**
```kotlin
@Composable
fun PlaceDetailScreen(
    placeId: String,
    usersViewModel: UsersViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val currentUserId = sessionManager.getUserId()
    
    // Obtener lista de favoritos del usuario actual
    val favorites = currentUserId?.let { 
        usersViewModel.getFavorites(it) 
    } ?: emptyList()
    
    val isFavorite = favorites.contains(placeId)
    
    Column {
        // Información del lugar...
        
        // Botón de favorito
        IconButton(
            onClick = {
                currentUserId?.let { userId ->
                    usersViewModel.toggleFavorite(userId, placeId)
                }
            }
        ) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }
}
```

**Ejemplo 2: Pantalla de Favoritos del Usuario**
```kotlin
@Composable
fun FavoritesScreen(
    usersViewModel: UsersViewModel,
    placesViewModel: PlacesViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val currentUserId = sessionManager.getUserId()
    
    val favoriteIds = currentUserId?.let { 
        usersViewModel.getFavorites(it) 
    } ?: emptyList()
    
    val allPlaces by placesViewModel.places.collectAsState()
    val favoritePlaces = allPlaces.filter { place ->
        favoriteIds.contains(place.id)
    }
    
    Column {
        Text(
            text = "Mis Favoritos (${favoritePlaces.size})",
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (favoritePlaces.isEmpty()) {
            Text("No tienes lugares favoritos aún")
        } else {
            LazyColumn {
                items(favoritePlaces) { place ->
                    PlaceCard(
                        place = place,
                        onClick = { 
                            navController.navigate("place/${place.id}") 
                        },
                        onFavoriteClick = {
                            currentUserId?.let { userId ->
                                usersViewModel.toggleFavorite(userId, place.id)
                            }
                        },
                        isFavorite = true
                    )
                }
            }
        }
    }
}
```

**Ejemplo 3: Contador de Favoritos en Perfil**
```kotlin
@Composable
fun ProfileScreen(usersViewModel: UsersViewModel) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val currentUserId = sessionManager.getUserId()
    
    val user = currentUserId?.let { 
        usersViewModel.findById(it) 
    }
    
    val favoritesCount = currentUserId?.let { 
        usersViewModel.getFavorites(it).size 
    } ?: 0
    
    Column {
        Text("Perfil de ${user?.name}")
        
        Row {
            StatItem(
                label = "Lugares Favoritos",
                value = favoritesCount.toString()
            )
            // Otras estadísticas...
        }
    }
}
```

#### Integración con SessionManager

Las funciones de favoritos se integran perfectamente con el sistema de sesión:

```kotlin
// Patrón recomendado para todas las pantallas que usan favoritos
@Composable
fun AnyScreen(usersViewModel: UsersViewModel) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val currentUserId = sessionManager.getUserId()
    
    // Siempre verificar que hay sesión activa antes de usar favoritos
    currentUserId?.let { userId ->
        val favorites = usersViewModel.getFavorites(userId)
        // Usar favorites...
    }
}
```

#### Validación y Pruebas

**Comportamiento Esperado**:

1. **Toggle Exitoso**:
   - Primera llamada: añade placeId a favorites
   - Segunda llamada: remueve placeId de favorites
   - StateFlow notifica cambio inmediatamente

2. **Seguridad Null**:
   - getFavorites con userId inexistente retorna lista vacía
   - toggleFavorite con userId inexistente no genera error

3. **Inmutabilidad**:
   - No se modifican objetos User existentes
   - Cada cambio crea nueva instancia con copy()

4. **Atomicidad**:
   - Un solo update al StateFlow por operación
   - No hay estados intermedios observables

#### Consideraciones de Diseño

**Persistencia**:
- Actualmente en memoria (ViewModel)
- Para producción: sincronizar con backend/base de datos
- Considerar implementar Repository pattern

**Sincronización**:
- Múltiples pantallas se actualizan automáticamente
- StateFlow garantiza consistencia

**Escalabilidad**:
- Operación O(n) donde n = número de usuarios
- Para grandes volúmenes, considerar índices o caché

**UX**:
- Toggle inmediato sin loading
- Feedback visual instantáneo
- No requiere confirmación (reversible)

---

### Step 6: Formulario de Creación de Lugares con Validaciones

Se implementó la funcionalidad completa del formulario de creación de lugares, incluyendo validaciones, integración con sesión activa y asignación automática de propietario.

#### Archivos Modificados

**1. CreatePlaceScreen.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/ui/places/CreatePlaceScreen.kt`

Se transformó de un formulario básico a una implementación completa con validaciones y lógica de negocio.

**Cambios Realizados**:

1. **Nueva Firma de Función**:
   ```kotlin
   @Composable
   fun CreatePlaceScreen(
       placesViewModel: PlacesViewModel,  // ViewModel inyectado
       onNavigateBack: () -> Unit
   )
   ```

2. **Campos del Formulario Completos**:
   - Nombre del lugar (obligatorio)
   - Descripción (mínimo 10 caracteres)
   - Dirección (obligatorio)
   - Teléfono (opcional)
   - Categoría (dropdown con PlaceType)

3. **Integración con SessionManager**:
   ```kotlin
   val context = LocalContext.current
   val sessionManager = remember { SessionManager(context) }
   ```

4. **Sistema de Validaciones**:
   - Validación de campos obligatorios
   - Validación de longitud mínima de descripción
   - Verificación de sesión activa antes de crear
   - Feedback mediante Toast para cada error

5. **Lógica de Creación de Lugar**:
   ```kotlin
   val newPlace = Place(
       id = UUID.randomUUID().toString(),
       title = name.trim(),
       description = description.trim(),
       address = address.trim(),
       location = Location(0.0, 0.0), // Temporal
       images = listOf("https://via.placeholder.com/300x200?text=Lugar"),
       phones = if (phone.isNotBlank()) listOf(phone.trim()) else emptyList(),
       type = selectedType,
       schedules = emptyList(),
       approved = false, // Requiere moderación
       ownerId = currentUserId, // Usuario propietario
       createdAt = System.currentTimeMillis()
   )
   
   placesViewModel.addPlace(newPlace)
   ```

**Características Implementadas**:

- **Dropdown Material 3**: ExposedDropdownMenuBox para selección de categoría
- **Scroll Vertical**: Soporte para pantallas pequeñas
- **Estados Reactivos**: Todos los campos usan `remember { mutableStateOf() }`
- **Validación de Sesión**: Previene creación sin login
- **Mensajes de Error**: Toast informativos para cada validación
- **Mensaje de Éxito**: Confirma creación y estado de moderación
- **Navegación Automática**: Vuelve atrás tras creación exitosa

**2. Navigation.kt**

**Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/ui/navigation/Navigation.kt`

Se agregó la inyección del PlacesViewModel para disponibilidad global.

**Cambios Realizados**:

1. **Import Agregado**:
   ```kotlin
   import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
   ```

2. **Inicialización del ViewModel**:
   ```kotlin
   val usersViewModel: UsersViewModel = viewModel()
   val placesViewModel: PlacesViewModel = viewModel() // Nuevo
   ```

3. **Inyección en Ruta**:
   ```kotlin
   composable(RouteScreen.CreatePlace.route) {
       CreatePlaceScreen(
           placesViewModel = placesViewModel, // Inyectado
           onNavigateBack = { navController.popBackStack() }
       )
   }
   ```

#### Flujo de Creación de Lugar

**Paso 1: Usuario Accede al Formulario**
```
Usuario logueado → Click en "Crear Lugar" → CreatePlaceScreen se muestra
```

**Paso 2: Llenar Formulario**
```
1. Ingresar nombre del lugar
2. Escribir descripción (mín. 10 caracteres)
3. Ingresar dirección
4. [Opcional] Agregar teléfono
5. Seleccionar categoría del dropdown
```

**Paso 3: Validaciones al Hacer Click en "Crear"**
```
if (name.isBlank())
    └─> Toast: "El nombre del lugar es obligatorio"
        └─> RETURN (no crea)

if (description.length < 10)
    └─> Toast: "La descripción debe tener al menos 10 caracteres"
        └─> RETURN (no crea)

if (address.isBlank())
    └─> Toast: "La dirección es obligatoria"
        └─> RETURN (no crea)

val currentUserId = sessionManager.getUserId()
if (currentUserId == null)
    └─> Toast: "Debes iniciar sesión para crear un lugar"
        └─> RETURN (no crea)
```

**Paso 4: Creación Exitosa**
```
Crear objeto Place con:
- ID único (UUID)
- Datos del formulario (trimmed)
- ownerId = currentUserId
- approved = false
- createdAt = timestamp actual

placesViewModel.addPlace(newPlace)
    └─> StateFlow actualizado
        └─> UI se actualiza automáticamente

Toast: "Lugar creado. Pendiente de aprobación"
onNavigateBack()
```

#### Validaciones Implementadas

**Validación 1: Nombre Obligatorio**
```kotlin
if (name.isBlank()) {
    Toast.makeText(context, "El nombre del lugar es obligatorio", Toast.LENGTH_SHORT).show()
    return@Button
}
```

**Validación 2: Descripción Mínima**
```kotlin
if (description.length < 10) {
    Toast.makeText(context, "La descripción debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show()
    return@Button
}
```

**Validación 3: Dirección Obligatoria**
```kotlin
if (address.isBlank()) {
    Toast.makeText(context, "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
    return@Button
}
```

**Validación 4: Sesión Activa**
```kotlin
val currentUserId = sessionManager.getUserId()
if (currentUserId == null) {
    Toast.makeText(context, "Debes iniciar sesión para crear un lugar", Toast.LENGTH_LONG).show()
    return@Button
}
```

#### Casos de Uso Habilitados

**Para Usuarios Regulares**:
1. Crear lugares desde la app
2. Lugares entran automáticamente en moderación (approved=false)
3. Sistema vincula lugar con su usuario (ownerId)
4. Feedback claro sobre estado de moderación

**Para Administradores**:
1. Pueden ver lugares pendientes (usando PlacesViewModel.getPendingPlaces())
2. Aprobar/rechazar lugares creados por usuarios
3. Auditoría completa de creación (ownerId + createdAt)

**Para el Sistema**:
1. Trazabilidad completa de autoría de lugares
2. Sistema de moderación funcional
3. Prevención de spam (validaciones)
4. Datos mínimos pero completos para funcionalidad básica

#### Datos Temporales/Placeholder

Para mantener la fase 2 simple, algunos campos usan valores placeholder:

**Location (Coordenadas)**:
```kotlin
location = Location(0.0, 0.0) // Se actualizará cuando se implemente el mapa
```

**Images (Imágenes)**:
```kotlin
images = listOf("https://via.placeholder.com/300x200?text=Lugar") // Placeholder hasta implementar upload
```

**Schedules (Horarios)**:
```kotlin
schedules = emptyList() // Se implementará en versiones futuras
```

Estos valores permiten que el sistema funcione completamente mientras se desarrollan features avanzadas.

#### Ejemplos de Uso

**Ejemplo 1: Usuario Crea un Restaurante**
```kotlin
// Usuario está en CreatePlaceScreen

// 1. Llenar formulario:
name = "Restaurante El Buen Sabor"
description = "Comida típica colombiana con ambiente familiar"
address = "Calle 14 # 15-20, Armenia"
phone = "3201234567"
selectedType = PlaceType.RESTAURANT

// 2. Click en "Crear"
// Sistema valida → Todo OK
// Sistema verifica sesión → userId = "2" (Daniel)

// 3. Lugar creado:
Place(
    id = "550e8400-e29b-41d4-a716-446655440000",
    title = "Restaurante El Buen Sabor",
    description = "Comida típica colombiana con ambiente familiar",
    address = "Calle 14 # 15-20, Armenia",
    location = Location(0.0, 0.0),
    images = ["https://via.placeholder.com/300x200?text=Lugar"],
    phones = ["3201234567"],
    type = RESTAURANT,
    schedules = [],
    approved = false, // ← Pendiente de moderación
    ownerId = "2", // ← Vinculado a Daniel
    createdAt = 1696780800000
)

// 4. Toast: "Lugar creado. Pendiente de aprobación"
// 5. Navega de regreso automáticamente
```

**Ejemplo 2: Validación de Sesión Falla**
```kotlin
// Usuario no logueado intenta crear lugar

// SessionManager.getUserId() retorna null
// Sistema previene creación

Toast.makeText(
    context,
    "Debes iniciar sesión para crear un lugar",
    Toast.LENGTH_LONG
).show()

// No se crea el lugar
// Usuario permanece en CreatePlaceScreen
```

**Ejemplo 3: Validación de Descripción Corta**
```kotlin
// Usuario llena formulario pero descripción muy corta

name = "Bar La 14"
description = "Buen bar" // ← Solo 8 caracteres
address = "Calle 14"

// Click en "Crear"
// Validación falla

Toast.makeText(
    context,
    "La descripción debe tener al menos 10 caracteres",
    Toast.LENGTH_SHORT
).show()

// No se crea el lugar
// Usuario puede corregir
```

**Ejemplo 4: Administrador Ve Lugar Creado**
```kotlin
@Composable
fun ModerationScreen(placesViewModel: PlacesViewModel) {
    val pendingPlaces = placesViewModel.getPendingPlaces()
    
    // pendingPlaces incluye el lugar recién creado por Daniel
    // Administrador puede ver:
    // - Título: "Restaurante El Buen Sabor"
    // - Propietario: ownerId = "2"
    // - Fecha: createdAt = timestamp
    
    LazyColumn {
        items(pendingPlaces) { place ->
            PlaceCard(
                place = place,
                onApprove = { 
                    placesViewModel.approvePlace(place.id, adminId) 
                },
                onReject = { reason ->
                    placesViewModel.rejectPlace(place.id, adminId, reason)
                }
            )
        }
    }
}
```

#### Integración con Sistema de Moderación

El flujo completo desde creación hasta aprobación:

```
1. Usuario crea lugar
   ├─> approved = false
   ├─> ownerId = currentUserId
   └─> createdAt = timestamp

2. Lugar aparece en PlacesViewModel.places
   └─> StateFlow notifica

3. Administrador consulta lugares pendientes
   └─> placesViewModel.getPendingPlaces()
       └─> Retorna lugar con approved = false

4. Administrador aprueba lugar
   └─> placesViewModel.approvePlace(placeId, adminId)
       ├─> approved = true
       └─> ModerationRecord creado

5. Lugar aparece en listados públicos
   └─> placesViewModel.getApprovedPlaces()
       └─> Usuarios regulares pueden verlo
```

#### Consideraciones de Diseño

**UX Mejorada**:
- Scroll vertical para pantallas pequeñas
- Validaciones con mensajes claros
- Feedback inmediato con Toast
- Navegación automática tras éxito
- Indicadores de campos obligatorios (*)

**Seguridad**:
- Verificación de sesión antes de crear
- Trim automático de campos de texto
- Validación de longitud mínima
- Prevención de lugares anónimos (requiere ownerId)

**Persistencia**:
- Datos guardados en memoria (PlacesViewModel)
- StateFlow garantiza sincronización
- Preparado para integración con backend/BD

**Escalabilidad**:
- Estructura preparada para agregar más campos
- Fácil añadir validaciones adicionales
- Placeholder para features futuras (mapas, fotos)

**Mantenibilidad**:
- Código documentado con KDoc
- Separación clara de validaciones
- Lógica de negocio aislada en onClick
- Facilita testing unitario

---

## Flujo de Autenticación

### Primer Acceso (Sin Sesión)

```
1. Usuario abre la aplicación
   └─> sessionManager.getUserId() retorna null
       └─> Muestra LoginScreen
           └─> Usuario ingresa credenciales
               └─> usersViewModel.login(email, password)
                   ├─> Si es válido:
                   │   └─> sessionManager.saveUserId(user.id)
                   │       └─> Navega a HomeAdmin o HomeUser
                   └─> Si es inválido:
                       └─> Muestra mensaje de error
```

### Acceso Subsecuente (Con Sesión Activa)

```
1. Usuario abre la aplicación
   └─> LaunchedEffect detecta sesión
       └─> sessionManager.getUserId() retorna userId
           └─> usersViewModel.findById(userId)
               ├─> Usuario encontrado:
               │   └─> Navega directamente a Home (skip Login)
               └─> Usuario no encontrado:
                   └─> sessionManager.clear()
                       └─> Muestra LoginScreen
```

### Cierre de Sesión (Logout)

```
1. Usuario hace click en "Cerrar Sesión"
   └─> sessionManager.clear()
       └─> Navega a LoginScreen
           └─> Limpia todo el backstack (popUpTo(0))
```

## Guía de Integración

### Integración en LoginScreen

Para implementar el guardado de sesión después del login:

```kotlin
@Composable
fun LoginScreen(
    navController: NavController,
    usersViewModel: UsersViewModel
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    
    // Lógica de login
    Button(onClick = {
        val user = usersViewModel.login(email, password)
        
        if (user != null) {
            // Guardar sesión
            sessionManager.saveUserId(user.id)
            
            // Navegar según rol
            val destination = when (user.role) {
                Role.ADMIN -> RouteScreen.HomeAdmin.route
                Role.USER -> RouteScreen.HomeUser.route
            }
            
            navController.navigate(destination) {
                popUpTo(RouteScreen.Login.route) { inclusive = true }
            }
        }
    }) {
        Text("Iniciar Sesión")
    }
}
```

### Integración de Logout

Para implementar el cierre de sesión en cualquier pantalla:

```kotlin
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    
    Button(onClick = {
        sessionManager.clear()
        navController.navigate(RouteScreen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }) {
        Text("Cerrar Sesión")
    }
}
```

### Integración en RegisterScreen

Para guardar sesión automáticamente después del registro:

```kotlin
Button(onClick = {
    val success = usersViewModel.createUser(name, lastname, email, phone, password)
    
    if (success) {
        val newUser = usersViewModel.findByEmail(email)
        if (newUser != null) {
            sessionManager.saveUserId(newUser.id)
            navController.navigate(RouteScreen.HomeUser.route) {
                popUpTo(RouteScreen.Register.route) { inclusive = true }
            }
        }
    }
}) {
    Text("Registrarse")
}
```

## Sistema de Navegación

### Rutas Definidas

La aplicación utiliza un sistema de navegación type-safe mediante sealed classes:

**Rutas de Autenticación**:
- `RouteScreen.Login` - Pantalla de inicio de sesión
- `RouteScreen.Register` - Pantalla de registro
- `RouteScreen.PasswordRecover` - Recuperación de contraseña

**Rutas de Usuario**:
- `RouteScreen.HomeUser` - Dashboard de usuario regular
- `RouteScreen.EditProfile` - Edición de perfil

**Rutas de Administrador**:
- `RouteScreen.HomeAdmin` - Dashboard de administrador

**Rutas de Lugares**:
- `RouteScreen.CreatePlace` - Creación de lugar

### Gestión del Backstack

La aplicación implementa gestión inteligente del backstack para prevenir navegación incorrecta:

```kotlin
// Limpiar backstack al navegar a Home después de login
navController.navigate(destination) {
    popUpTo(RouteScreen.Login.route) { inclusive = true }
}

// Limpiar todo el backstack al hacer logout
navController.navigate(RouteScreen.Login.route) {
    popUpTo(0) { inclusive = true }
}
```

## Datos de Prueba

### Usuarios Predefinidos

La aplicación incluye dos usuarios de prueba en `UsersViewModel`:

**Usuario Administrador**:
- Email: `admin@email.com`
- Password: `123456`
- Role: `ADMIN`
- ID: `"1"`

**Usuario Regular**:
- Email: `daniel@email.com`
- Password: `123456`
- Role: `USER`
- ID: `"2"`

### Reseñas Predefinidas

La aplicación incluye tres reseñas de prueba en `RewiewsViewModel`:

**Reseña 1**:
- Lugar: Restaurante El Paisa (ID: "1")
- Usuario: Daniel (ID: "2")
- Rating: 5 estrellas
- Comentario: "Excelente comida y servicio. Muy recomendado!"
- Con respuesta del propietario

**Reseña 2**:
- Lugar: Restaurante El Paisa (ID: "1")
- Usuario: Daniel (ID: "2")
- Rating: 4 estrellas
- Comentario: "Buena comida pero un poco caro."
- Sin respuesta del propietario

**Reseña 3**:
- Lugar: Bar test 1 (ID: "2")
- Usuario: Daniel (ID: "2")
- Rating: 3 estrellas
- Comentario: "Ambiente agradable pero demoran mucho."
- Sin respuesta del propietario

## Consideraciones de Seguridad

### Seguridad Actual (Desarrollo)

La implementación actual utiliza prácticas simplificadas para propósitos de desarrollo:

- Almacenamiento de userId en SharedPreferences sin cifrado
- Contraseñas en texto plano en memoria (no persistidas en SharedPreferences)
- Validación básica de credenciales

### Recomendaciones para Producción

Para un entorno de producción, se recomienda implementar:

1. **EncryptedSharedPreferences**: Utilizar la biblioteca de seguridad de Android para cifrar datos sensibles
   ```kotlin
   val masterKey = MasterKey.Builder(context)
       .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
       .build()
   
   val encryptedPrefs = EncryptedSharedPreferences.create(
       context,
       "secure_prefs",
       masterKey,
       EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
       EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
   )
   ```

2. **Tokens JWT**: Reemplazar userId directo con tokens de autenticación
3. **Hashing de Contraseñas**: Implementar BCrypt o Argon2 para hashear contraseñas
4. **Timeout de Sesión**: Implementar expiración automática de sesión
5. **Validación en Backend**: Validar sesión contra servidor en cada operación crítica
6. **Certificados SSL Pinning**: Para comunicaciones seguras con backend
7. **Biometría**: Añadir autenticación biométrica opcional

## Testing

### Casos de Prueba Definidos

#### Test Case 1: Primer Login
```
Precondiciones: Primera instalación de la app
Pasos:
1. Abrir la aplicación
2. Verificar que muestra LoginScreen
3. Ingresar credenciales: admin@email.com / 123456
4. Click en "Iniciar Sesión"

Resultado Esperado:
- Navega a HomeAdmin
- sessionManager guarda userId="1"
- No se puede volver a Login con botón atrás
```

#### Test Case 2: Auto-Login
```
Precondiciones: Usuario ha iniciado sesión previamente
Pasos:
1. Cerrar la aplicación completamente
2. Volver a abrir la aplicación

Resultado Esperado:
- NO muestra LoginScreen
- Navega directamente a HomeAdmin o HomeUser
- El proceso es instantáneo (< 500ms)
```

#### Test Case 3: Logout
```
Precondiciones: Usuario con sesión activa
Pasos:
1. Estar en HomeAdmin o HomeUser
2. Click en "Cerrar Sesión"

Resultado Esperado:
- Navega a LoginScreen
- sessionManager.getUserId() retorna null
- No se puede volver atrás con botón back
- Próximo inicio requiere credenciales
```

#### Test Case 4: Cambio de Usuario
```
Precondiciones: Aplicación instalada
Pasos:
1. Login con admin@email.com
2. Cerrar app y volver a abrir
3. Hacer Logout
4. Login con daniel@email.com
5. Cerrar app y volver a abrir

Resultado Esperado:
- Paso 2: Va a HomeAdmin automáticamente
- Paso 5: Va a HomeUser automáticamente
- Cada sesión mantiene el usuario correcto
```

#### Test Case 5: Sesión Inválida
```
Precondiciones: userId guardado de usuario inexistente
Pasos:
1. Modificar SharedPreferences con userId no válido
2. Abrir la aplicación

Resultado Esperado:
- sessionManager.clear() se ejecuta automáticamente
- Muestra LoginScreen
- No genera crash ni error visible
```

### Casos de Prueba CRUD y Moderación

#### Test Case 6: Agregar Lugar
```
Precondiciones: PlacesViewModel inicializado
Pasos:
1. Obtener conteo inicial: val initial = placesViewModel.places.value.size
2. Crear nuevo lugar con approved = false
3. Llamar placesViewModel.addPlace(newPlace)
4. Observar places StateFlow

Resultado Esperado:
- places.value.size == initial + 1
- Nuevo lugar aparece en la lista
- getPendingPlaces() incluye el nuevo lugar
- UI se actualiza automáticamente (StateFlow)
```

#### Test Case 7: Aprobar Lugar
```
Precondiciones: Existe lugar con approved = false (id: "2")
Pasos:
1. Obtener lugares pendientes: val pending = getPendingPlaces()
2. Verificar que "2" está en pending
3. Llamar approvePlace("2", "admin1")
4. Verificar getPendingPlaces() nuevamente
5. Verificar moderationRecords

Resultado Esperado:
- Lugar "2" ya no aparece en getPendingPlaces()
- Lugar "2" tiene approved = true
- moderationRecords contiene nuevo registro con action="APPROVE"
- Registro tiene moderatorId="admin1" y timestamp válido
```

#### Test Case 8: Rechazar Lugar con Razón
```
Precondiciones: Existe lugar pendiente (id: "3")
Pasos:
1. Llamar rejectPlace("3", "admin1", "Contenido inapropiado")
2. Verificar estado del lugar
3. Buscar registro en moderationRecords

Resultado Esperado:
- Lugar "3" tiene approved = false
- moderationRecords contiene registro con:
  * action = "REJECT"
  * moderatorId = "admin1"
  * reason = "Contenido inapropiado"
  * timestamp válido
```

#### Test Case 9: Eliminar Lugar
```
Precondiciones: PlacesViewModel con 6 lugares
Pasos:
1. Contar lugares: val count = places.value.size (debería ser 6)
2. Llamar deletePlace("4")
3. Verificar places StateFlow
4. Intentar findById("4")

Resultado Esperado:
- places.value.size == count - 1
- findById("4") retorna null
- UI se actualiza automáticamente
```

#### Test Case 10: Filtrar Lugares Aprobados
```
Precondiciones: Lugares de prueba cargados (1 aprobado, 5 pendientes)
Pasos:
1. Llamar getApprovedPlaces()
2. Verificar cada lugar en la lista resultante

Resultado Esperado:
- getApprovedPlaces().size == 1
- Lugar retornado es "Restaurante El Paisa" (id: "1")
- Todos los lugares tienen approved = true
```

### Casos de Prueba de Reseñas

#### Test Case 11: Agregar Reseña
```
Precondiciones: RewiewsViewModel inicializado con 3 reseñas
Pasos:
1. Obtener conteo inicial de reseñas del lugar "1": 
   val initial = rewiewsViewModel.findByPlaceId("1").size
2. Crear nueva reseña para lugar "1"
3. Llamar rewiewsViewModel.addReview(newReview)
4. Verificar findByPlaceId("1")

Resultado Esperado:
- findByPlaceId("1").size == initial + 1
- Nueva reseña aparece en la lista filtrada
- reviews StateFlow se actualiza
- UI se recompone automáticamente
```

#### Test Case 12: Responder a Reseña
```
Precondiciones: Existe reseña sin respuesta (id: "review2")
Pasos:
1. Verificar review2.ownerResponse == null
2. Llamar replyToReview("review2", "Gracias por tu feedback!")
3. Buscar reseña actualizada: findById("review2")
4. Verificar en findByPlaceId("1")

Resultado Esperado:
- review2.ownerResponse == "Gracias por tu feedback!"
- Cambio se refleja en todas las consultas
- StateFlow notifica cambio
- Otras reseñas permanecen sin cambios
```

#### Test Case 13: Calcular Promedio de Rating
```
Precondiciones: Lugar "1" tiene 2 reseñas (rating 5 y 4)
Pasos:
1. Llamar getAverageRating("1")
2. Agregar nueva reseña con rating 3
3. Llamar getAverageRating("1") nuevamente

Resultado Esperado:
- Primer llamado retorna 4.5
- Segundo llamado retorna 4.0 ((5+4+3)/3)
- Cálculo es correcto y dinámico
```

#### Test Case 14: Filtrar Reseñas Pendientes de Respuesta
```
Precondiciones: Lugar "1" tiene reseñas con y sin respuesta
Pasos:
1. Llamar getPendingResponses("1")
2. Verificar cada reseña en la lista

Resultado Esperado:
- Lista contiene solo review2 (sin respuesta)
- review1 (con respuesta) no aparece
- Filtro funciona correctamente
```

#### Test Case 15: Eliminar Reseña
```
Precondiciones: RewiewsViewModel con 3 reseñas
Pasos:
1. Contar reseñas: val count = reviews.value.size (debería ser 3)
2. Llamar deleteReview("review3")
3. Verificar reviews StateFlow
4. Intentar findById("review3")

Resultado Esperado:
- reviews.value.size == 2
- findById("review3") retorna null
- findByPlaceId("2") retorna lista vacía
- UI se actualiza automáticamente
```

#### Test Case 16: Obtener Reseñas Recientes
```
Precondiciones: Múltiples reseñas con diferentes fechas
Pasos:
1. Llamar getRecentReviews("1", limit = 2)
2. Verificar orden de las reseñas retornadas

Resultado Esperado:
- Retorna máximo 2 reseñas
- Ordenadas de más reciente a más antigua
- Solo del lugar especificado ("1")
```

#### Test Case 17: Contar Reseñas por Lugar
```
Precondiciones: Datos de prueba cargados
Pasos:
1. Llamar getReviewCount("1") 
2. Llamar getReviewCount("2")
3. Llamar getReviewCount("999") (lugar sin reseñas)

Resultado Esperado:
- getReviewCount("1") == 2
- getReviewCount("2") == 1
- getReviewCount("999") == 0
```

#### Test Case 18: Marcar Lugar como Favorito
```
Precondiciones: Usuario con ID "2" sin favoritos
Pasos:
1. Verificar getFavorites("2").isEmpty() == true
2. Llamar toggleFavorite("2", "place1")
3. Verificar getFavorites("2")

Resultado Esperado:
- getFavorites("2") contiene "place1"
- getFavorites("2").size == 1
- UI muestra ícono de favorito lleno
- StateFlow notifica cambio
```

#### Test Case 19: Desmarcar Favorito (Toggle Off)
```
Precondiciones: Usuario "2" tiene "place1" en favoritos
Pasos:
1. Verificar getFavorites("2").contains("place1") == true
2. Llamar toggleFavorite("2", "place1")
3. Verificar getFavorites("2")

Resultado Esperado:
- getFavorites("2") NO contiene "place1"
- getFavorites("2").isEmpty() == true
- UI muestra ícono de favorito vacío
- StateFlow notifica cambio
```

#### Test Case 20: Múltiples Favoritos
```
Precondiciones: Usuario "2" sin favoritos
Pasos:
1. Llamar toggleFavorite("2", "place1")
2. Llamar toggleFavorite("2", "place2")
3. Llamar toggleFavorite("2", "place3")
4. Verificar getFavorites("2")

Resultado Esperado:
- getFavorites("2").size == 3
- Contiene "place1", "place2", "place3"
- Todos los lugares se pueden desmarcar individualmente
- Orden de inserción se mantiene
```

#### Test Case 21: Favoritos de Usuario Inexistente
```
Precondiciones: Usuario con ID "999" no existe
Pasos:
1. Llamar getFavorites("999")
2. Llamar toggleFavorite("999", "place1")
3. Verificar getFavorites("999")

Resultado Esperado:
- Primera llamada retorna lista vacía (emptyList())
- toggleFavorite no genera excepción
- Segunda llamada retorna lista vacía
- No se crea usuario automáticamente
```

#### Test Case 22: Sincronización de Favoritos entre Pantallas
```
Precondiciones: Usuario "2" logueado, dos pantallas observando getFavorites
Pasos:
1. PlaceDetailScreen muestra ícono de favorito vacío
2. FavoritesScreen muestra lista vacía
3. Usuario hace click en favorito en PlaceDetailScreen
4. Verificar ambas pantallas se actualizan

Resultado Esperado:
- PlaceDetailScreen muestra ícono lleno inmediatamente
- FavoritesScreen muestra el lugar en la lista
- Ambas pantallas reaccionan al cambio de StateFlow
- No hay delay en actualización
```

#### Test Case 23: Crear Lugar con Validaciones Exitosas
```
Precondiciones: Usuario "2" (Daniel) logueado
Pasos:
1. Navegar a CreatePlaceScreen
2. Llenar formulario:
   - Nombre: "Café Central"
   - Descripción: "Excelente café con ambiente acogedor" (>10 chars)
   - Dirección: "Calle 10 # 12-34"
   - Teléfono: "3101234567"
   - Categoría: RESTAURANT
3. Click en "Crear"
4. Verificar placesViewModel.places

Resultado Esperado:
- Toast: "Lugar creado. Pendiente de aprobación"
- Navega de regreso automáticamente
- Lugar existe en placesViewModel.places
- place.ownerId == "2"
- place.approved == false
- place.title == "Café Central"
- place.id != null (UUID generado)
```

#### Test Case 24: Validación de Nombre Obligatorio
```
Precondiciones: Usuario logueado en CreatePlaceScreen
Pasos:
1. Dejar campo "Nombre" vacío
2. Llenar otros campos correctamente
3. Click en "Crear"

Resultado Esperado:
- Toast: "El nombre del lugar es obligatorio"
- No se crea el lugar
- Permanece en CreatePlaceScreen
- placesViewModel.places.size no aumenta
```

#### Test Case 25: Validación de Descripción Mínima
```
Precondiciones: Usuario logueado en CreatePlaceScreen
Pasos:
1. Llenar nombre: "Bar"
2. Descripción: "Bueno" (solo 5 caracteres)
3. Llenar dirección
4. Click en "Crear"

Resultado Esperado:
- Toast: "La descripción debe tener al menos 10 caracteres"
- No se crea el lugar
- Usuario puede corregir la descripción
- placesViewModel.places.size no aumenta
```

#### Test Case 26: Validación de Sesión Activa
```
Precondiciones: Usuario SIN login (sesión cerrada)
Pasos:
1. Navegar a CreatePlaceScreen (por algún bug hipotético)
2. Llenar formulario correctamente
3. Click en "Crear"
4. sessionManager.getUserId() retorna null

Resultado Esperado:
- Toast: "Debes iniciar sesión para crear un lugar"
- No se crea el lugar
- No hay crash
- Sistema previene creación sin owner
```

#### Test Case 27: Verificar Asignación de ownerId
```
Precondiciones: Usuario "2" (Daniel) logueado
Pasos:
1. Crear lugar "Restaurante Test"
2. Obtener lugar de placesViewModel.places
3. Verificar campo ownerId

Resultado Esperado:
- place.ownerId == "2"
- place.ownerId != null
- Lugar vinculado correctamente al usuario
- Administrador puede ver quién lo creó
```

#### Test Case 28: Lugar Entra en Moderación Automáticamente
```
Precondiciones: Usuario regular crea lugar
Pasos:
1. Crear lugar "Bar Test"
2. Llamar placesViewModel.getPendingPlaces()
3. Llamar placesViewModel.getApprovedPlaces()

Resultado Esperado:
- getPendingPlaces() incluye "Bar Test"
- getApprovedPlaces() NO incluye "Bar Test"
- place.approved == false
- Lugar requiere aprobación de administrador
```

#### Test Case 29: Dropdown de Categorías Funciona
```
Precondiciones: Usuario en CreatePlaceScreen
Pasos:
1. Click en dropdown de categoría
2. Verificar opciones disponibles
3. Seleccionar HOTEL
4. Verificar selección

Resultado Esperado:
- Dropdown muestra todos los PlaceType
- RESTAURANT, BAR, HOTEL, PARK, SHOPPING, OTHER
- Al seleccionar HOTEL, dropdown se cierra
- Campo muestra "HOTEL"
- selectedType == PlaceType.HOTEL
```

#### Test Case 30: Teléfono es Opcional
```
Precondiciones: Usuario logueado en CreatePlaceScreen
Pasos:
1. Llenar campos obligatorios correctamente
2. Dejar campo "Teléfono" vacío
3. Click en "Crear"

Resultado Esperado:
- Lugar se crea exitosamente
- place.phones == emptyList()
- No muestra error de validación
- Toast de éxito se muestra
```

## Compilación y Ejecución

### Requisitos

- Android Studio Hedgehog o superior
- JDK 11
- Android SDK API 28 o superior
- Gradle 8.0 o superior

### Comandos Gradle

#### Compilar Debug
```bash
./gradlew assembleDebug
```

#### Compilar Release
```bash
./gradlew assembleRelease
```

#### Instalar en Dispositivo
```bash
./gradlew installDebug
```

#### Limpiar Proyecto
```bash
./gradlew clean
```

#### Ejecutar Tests
```bash
./gradlew test
```

## Configuración del Proyecto

### build.gradle.kts (Project Level)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

### build.gradle.kts (App Level)

```kotlin
android {
    namespace = "co.edu.eam.lugaresapp"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "co.edu.eam.lugaresapp"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
    
    buildFeatures {
        compose = true
    }
}
```

## Estado del Proyecto

**Versión Actual**: 1.0  
**Última Actualización**: 8 de octubre de 2025  
**Estado**: En Desarrollo Activo

### Funcionalidades Implementadas

- Arquitectura MVVM completa
- Sistema de navegación con Jetpack Navigation Compose
- Gestión de sesión con SharedPreferences (SessionManager)
- Auto-login en inicio de aplicación
- Pantallas de autenticación (Login, Register, Password Recovery)
- Interfaces diferenciadas para Usuario y Administrador
- ViewModels para gestión de usuarios, lugares y reseñas
- Componentes UI reutilizables
- Tema Material 3 personalizado
- Modelos de datos extendidos con soporte para:
  - Moderación de lugares (campo `approved`)
  - Propiedad de lugares (campo `ownerId`)
  - Sistema de favoritos (campo `favorites` en User)
  - Respuestas de propietarios a reseñas (campo `ownerResponse`)
  - Timestamps de creación (campo `createdAt`)
  - Registros de auditoría de moderación (ModerationRecord)
- Sistema CRUD completo de lugares:
  - Crear lugares (addPlace)
  - Eliminar lugares (deletePlace)
  - Buscar por ID, tipo y nombre
  - Filtrar lugares aprobados/pendientes
- Sistema de moderación funcional:
  - Aprobar lugares con registro de auditoría
  - Rechazar lugares con razón opcional
  - Historial completo de acciones de moderación
  - StateFlow reactivo para moderationRecords
- Sistema completo de reseñas y valoraciones:
  - Agregar y eliminar reseñas (addReview, deleteReview)
  - Responder a reseñas como propietario (replyToReview)
  - Filtrar reseñas por lugar (findByPlaceId)
  - Calcular promedios de rating (getAverageRating)
  - Contar reseñas por lugar (getReviewCount)
  - Filtrar reseñas con/sin respuesta
  - Obtener reseñas recientes ordenadas
  - Historial de reseñas por usuario
  - StateFlow reactivo para reviews
- Sistema de gestión de favoritos:
  - Marcar/desmarcar lugares como favoritos (toggleFavorite)
  - Obtener lista de favoritos del usuario (getFavorites)
  - Persistencia en memoria mediante StateFlow
  - Actualización reactiva en UI
- Formulario completo de creación de lugares:
  - Validación de campos obligatorios (nombre, descripción, dirección)
  - Validación de longitud mínima de descripción (10 caracteres)
  - Verificación de sesión activa antes de crear
  - Dropdown Material 3 para selección de categoría
  - Asignación automática de ownerId desde SessionManager
  - Lugares creados entran en estado approved=false (moderación)
  - Feedback visual con Toast para validaciones y éxito
  - Navegación automática tras creación exitosa
  - Integración completa con PlacesViewModel

### Funcionalidades Pendientes

- Implementación completa de LoginScreen con SessionManager
- Implementación de funcionalidad de Logout
- UI para pantalla de moderación de administradores
- UI para visualización y gestión de reseñas
- UI para respuestas de propietarios a reseñas
- UI para sección "Mis Favoritos"
- UI para botones de favorito en PlaceDetailScreen
- Validación de propiedad de lugar antes de permitir respuestas
- Implementación de mapas para seleccionar ubicación real
- Sistema de carga de imágenes (reemplazar placeholder)
- Formulario de horarios de atención
- Integración con backend/API REST
- Persistencia de datos con Room Database
- Funcionalidad de mapas con Google Maps
- Carga y gestión de imágenes
- Sistema de notificaciones
- Validación de email y recuperación de contraseña
- Tests unitarios e instrumentados
- Dashboard de estadísticas de moderación y reseñas

## Contacto y Contribución

**Institución**: EAM (Escuela de Administración y Mercadotecnia)  
**Package**: co.edu.eam.lugaresapp

## Licencia

Este proyecto es de uso académico para la institución EAM.

---

**Documento generado**: 8 de octubre de 2025  
**Versión del documento**: 1.0
