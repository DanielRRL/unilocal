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
- Gestión de sesión con SharedPreferences
- Auto-login en inicio de aplicación
- Pantallas de autenticación (Login, Register, Password Recovery)
- Interfaces diferenciadas para Usuario y Administrador
- ViewModels para gestión de usuarios, lugares y reseñas
- Componentes UI reutilizables
- Tema Material 3 personalizado

### Funcionalidades Pendientes

- Implementación completa de LoginScreen con SessionManager
- Implementación de funcionalidad de Logout
- Integración con backend/API REST
- Persistencia de datos con Room Database
- Funcionalidad de mapas con Google Maps
- Carga y gestión de imágenes
- Sistema de notificaciones
- Validación de email y recuperación de contraseña
- Tests unitarios e instrumentados

## Contacto y Contribución

**Institución**: EAM (Escuela de Administración y Mercadotecnia)  
**Package**: co.edu.eam.lugaresapp

## Licencia

Este proyecto es de uso académico para la institución EAM.

---

**Documento generado**: 8 de octubre de 2025  
**Versión del documento**: 1.0
