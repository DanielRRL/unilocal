# ✅ Manual QA Checklist - Fase 2 (Funcionalidad en Memoria)

**Proyecto:** UniLocal - App Android de Lugares  
**Fase:** 2 - Lógica en Memoria (sin persistencia externa)  
**Fecha:** Octubre 2025  
**Objetivo:** Validar toda la funcionalidad implementada en memoria antes de integrar base de datos.

---

## 📋 Índice de Pruebas

1. [Inicio de App y Auto-login](#1-inicio-de-app-y-auto-login)
2. [Registro de Usuario](#2-registro-de-usuario)
3. [Login de Usuario](#3-login-de-usuario)
4. [Creación de Lugar](#4-creación-de-lugar)
5. [Detalle de Lugar](#5-detalle-de-lugar)
6. [Agregar Review](#6-agregar-review)
7. [Sistema de Favoritos](#7-sistema-de-favoritos)
8. [Admin: Moderación de Lugares](#8-admin-moderación-de-lugares)
9. [Admin: Historial de Moderación](#9-admin-historial-de-moderación)
10. [Eliminar Lugar (Propietario)](#10-eliminar-lugar-propietario)
11. [Cerrar Sesión](#11-cerrar-sesión)
12. [Navegación y ViewModels Compartidos](#12-navegación-y-viewmodels-compartidos)

---

## 🎯 Criterios Generales de Aceptación

Antes de empezar las pruebas, verificar:

- ✅ **No hay crashes por NPE:** No se usa `!!` en el código UI
- ✅ **Reactividad:** Cambios en ViewModels se reflejan inmediatamente en UI
- ✅ **ViewModels compartidos:** Misma instancia en todas las pantallas
- ✅ **Sin persistencia externa:** Todo funciona en memoria (StateFlow)
- ✅ **Null safety:** Manejo correcto de nulls con safe calls (`?.`)
- ✅ **No duplicación:** No hay múltiples instancias de ViewModels

---

## 1️⃣ Inicio de App y Auto-login

### Objetivo
Verificar que la app detecta sesiones existentes y navega automáticamente.

### Datos de Prueba
- Usuario precargado: `admin@test.com` / `admin123` (Admin)
- Usuario precargado: `user@test.com` / `user123` (User)

### Pasos de Prueba

#### Test 1.1: Primera vez sin sesión
**Pasos:**
1. Instalar la app por primera vez
2. Iniciar la app

**Resultado Esperado:**
- ✅ La app muestra la pantalla de **Login**
- ✅ No hay auto-navegación
- ✅ No hay crashes

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 1.2: Auto-login con sesión guardada (Usuario)
**Pasos:**
1. Hacer login con `user@test.com` / `user123`
2. Cerrar la app (no cerrar sesión)
3. Volver a abrir la app

**Resultado Esperado:**
- ✅ La app navega automáticamente a **HomeUser**
- ✅ No muestra pantalla de Login
- ✅ `SessionManager.getUserId()` retorna el ID correcto

**Verificación en código:**
```kotlin
// Navigation.kt - LaunchedEffect
LaunchedEffect(Unit) {
    val currentUserId = sessionManager.getUserId()
    // Debe retornar userId y navegar a HomeUser
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 1.3: Auto-login con sesión guardada (Admin)
**Pasos:**
1. Hacer login con `admin@test.com` / `admin123`
2. Cerrar la app (no cerrar sesión)
3. Volver a abrir la app

**Resultado Esperado:**
- ✅ La app navega automáticamente a **HomeAdmin**
- ✅ No muestra pantalla de Login
- ✅ Navegación basada en `user.role == Role.ADMIN`

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 1.4: Sesión inválida (usuario eliminado)
**Pasos:**
1. Hacer login con cualquier usuario
2. Simular que el usuario fue eliminado de `usersViewModel.users`
3. Reiniciar la app

**Resultado Esperado:**
- ✅ La app detecta que el usuario no existe
- ✅ Llama a `sessionManager.clear()`
- ✅ Muestra pantalla de Login
- ✅ No hay crash

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 2️⃣ Registro de Usuario

### Objetivo
Verificar que un nuevo usuario puede registrarse y su información se almacena en memoria.

### Pasos de Prueba

#### Test 2.1: Registro exitoso
**Pasos:**
1. Desde Login, hacer clic en "Registrarse"
2. Llenar el formulario:
   - **Nombre:** "Juan Pérez"
   - **Email:** "juan.perez@test.com"
   - **Password:** "juan123"
   - **Confirmar Password:** "juan123"
   - **Ciudad:** "Pereira"
3. Hacer clic en "Registrarse"

**Resultado Esperado:**
- ✅ Usuario creado con `id` UUID generado automáticamente
- ✅ `role = Role.USER` asignado por defecto
- ✅ Usuario aparece en `usersViewModel.users`
- ✅ `sessionManager.saveUserId()` guarda el ID
- ✅ Navegación automática a **HomeUser**

**Verificación en código:**
```kotlin
// RegisterScreen.kt
usersViewModel.createUser(newUser)
sessionManager.saveUserId(newUser.id)
navController.navigate(RouteScreen.HomeUser.route)
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 2.2: Validación de campos vacíos
**Pasos:**
1. Desde Login, ir a "Registrarse"
2. Dejar todos los campos vacíos
3. Hacer clic en "Registrarse"

**Resultado Esperado:**
- ✅ Se muestra error: "Por favor completa todos los campos"
- ✅ No se crea el usuario
- ✅ No navega

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 2.3: Validación de passwords no coinciden
**Pasos:**
1. Llenar el formulario:
   - Password: "pass123"
   - Confirmar: "pass456"
2. Hacer clic en "Registrarse"

**Resultado Esperado:**
- ✅ Se muestra error: "Las contraseñas no coinciden"
- ✅ No se crea el usuario

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 2.4: Email duplicado
**Pasos:**
1. Registrar usuario con "test@test.com"
2. Cerrar sesión
3. Intentar registrar otro usuario con "test@test.com"

**Resultado Esperado:**
- ✅ Se muestra error: "El email ya está registrado"
- ✅ No se crea usuario duplicado

**Verificación en código:**
```kotlin
// UsersViewModel.kt - createUser()
if (_users.value.any { it.email == user.email }) {
    // Mostrar error
    return
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 3️⃣ Login de Usuario

### Objetivo
Verificar autenticación y navegación basada en rol.

### Pasos de Prueba

#### Test 3.1: Login exitoso como Usuario
**Pasos:**
1. Desde Login, ingresar:
   - Email: "user@test.com"
   - Password: "user123"
2. Hacer clic en "Iniciar Sesión"

**Resultado Esperado:**
- ✅ `usersViewModel.login()` retorna el usuario
- ✅ `sessionManager.saveUserId()` guarda el ID
- ✅ Navega a **HomeUser**
- ✅ `getUserId()` retorna el ID correcto

**Verificación en código:**
```kotlin
// LoginScreen.kt
val user = usersViewModel.login(email, password)
if (user != null) {
    sessionManager.saveUserId(user.id)
    navController.navigate(destination)
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 3.2: Login exitoso como Admin
**Pasos:**
1. Desde Login, ingresar:
   - Email: "admin@test.com"
   - Password: "admin123"
2. Hacer clic en "Iniciar Sesión"

**Resultado Esperado:**
- ✅ Navega a **HomeAdmin**
- ✅ Se muestra UI de moderación (PlacesList, History)

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 3.3: Login con credenciales incorrectas
**Pasos:**
1. Ingresar email/password inválidos
2. Hacer clic en "Iniciar Sesión"

**Resultado Esperado:**
- ✅ Se muestra error: "Credenciales incorrectas"
- ✅ No navega
- ✅ No guarda sesión

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 3.4: Login con campos vacíos
**Pasos:**
1. Dejar email y/o password vacíos
2. Hacer clic en "Iniciar Sesión"

**Resultado Esperado:**
- ✅ Se muestra error: "Por favor completa todos los campos"
- ✅ No intenta autenticar

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 4️⃣ Creación de Lugar

### Objetivo
Verificar que un usuario puede crear un lugar y que se almacena correctamente en memoria.

### Pasos de Prueba

#### Test 4.1: Crear lugar exitosamente
**Pasos:**
1. Login como usuario (no admin)
2. Navegar a **CreatePlace**
3. Llenar el formulario:
   - **Nombre:** "Café del Parque"
   - **Descripción:** "Hermoso café con vista al parque central de la ciudad"
   - **Dirección:** "Calle 10 #5-20"
   - **Teléfono:** "3001234567"
   - **Tipo:** "Restaurante"
4. Hacer clic en "Crear Lugar"

**Resultado Esperado:**
- ✅ Lugar creado con:
  - `id`: UUID generado
  - `ownerId`: `sessionManager.getUserId()`
  - `approved`: `false` (pendiente de aprobación)
  - `location`: `Location(0.0, 0.0)` (temporal)
  - `images`: Lista vacía o placeholder
  - `schedules`: Lista vacía
  - `createdAt`: Timestamp actual
- ✅ Lugar agregado a `placesViewModel.places`
- ✅ Navega atrás automáticamente
- ✅ No hay crash

**Verificación en código:**
```kotlin
// CreatePlaceScreen.kt
val newPlace = Place(
    id = UUID.randomUUID().toString(),
    ownerId = currentUserId,
    approved = false,
    location = Location(0.0, 0.0), // Temporal
    // ... otros campos
)
placesViewModel.addPlace(newPlace)
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 4.2: Validación de nombre vacío
**Pasos:**
1. Dejar campo "Nombre" vacío
2. Hacer clic en "Crear Lugar"

**Resultado Esperado:**
- ✅ Se muestra error: "El nombre no puede estar vacío"
- ✅ No se crea el lugar

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 4.3: Validación de descripción mínima
**Pasos:**
1. Ingresar descripción con menos de 10 caracteres: "Café"
2. Hacer clic en "Crear Lugar"

**Resultado Esperado:**
- ✅ Se muestra error: "La descripción debe tener al menos 10 caracteres"
- ✅ No se crea el lugar

**Verificación en código:**
```kotlin
if (description.length < 10) {
    errorMessage = "La descripción debe tener al menos 10 caracteres"
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 4.4: Validación de dirección vacía
**Pasos:**
1. Dejar campo "Dirección" vacío
2. Hacer clic en "Crear Lugar"

**Resultado Esperado:**
- ✅ Se muestra error: "La dirección no puede estar vacía"
- ✅ No se crea el lugar

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 4.5: Validación de sesión
**Pasos:**
1. Simular que `sessionManager.getUserId()` retorna `null`
2. Intentar crear lugar

**Resultado Esperado:**
- ✅ Se muestra error: "No se pudo obtener el usuario actual"
- ✅ No se crea el lugar

**Verificación en código:**
```kotlin
val currentUserId = sessionManager.getUserId()
if (currentUserId == null) {
    errorMessage = "No se pudo obtener el usuario actual"
    return
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 4.6: Verificar lugar en lista pendientes
**Pasos:**
1. Crear un lugar como usuario
2. Login como admin
3. Ir a **PlacesList** (pantalla de moderación)

**Resultado Esperado:**
- ✅ El lugar creado aparece en la lista de pendientes
- ✅ Muestra badge `approved = false`
- ✅ Se pueden ver botones de "Aprobar" y "Rechazar"

**Verificación en código:**
```kotlin
// PlacesViewModel.kt - getPendingPlaces()
fun getPendingPlaces(): StateFlow<List<Place>> {
    return _places.map { places ->
        places.filter { !it.approved }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 5️⃣ Detalle de Lugar

### Objetivo
Verificar que la pantalla de detalle muestra toda la información correctamente y maneja casos edge.

### Pasos de Prueba

#### Test 5.1: Ver detalle de lugar válido
**Pasos:**
1. Login como usuario
2. Ir a **PlacesScreen**
3. Hacer clic en un lugar de la lista
4. Observar **PlaceDetailScreen**

**Resultado Esperado:**
- ✅ Se muestra toda la información:
  - Nombre del lugar
  - Galería de imágenes (LazyRow horizontal)
  - Descripción completa
  - Dirección
  - Teléfonos
  - Estado: "Abierto" o "Cerrado" (calculado con `calculateScheduleStatus()`)
  - Rating promedio (estrellas)
  - Lista de reviews
- ✅ No hay crash
- ✅ Scroll funciona correctamente

**Verificación en código:**
```kotlin
// PlaceDetailScreen.kt
val place = placesViewModel.findById(id).collectAsState().value

if (place == null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Lugar no encontrado")
    }
    return
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 5.2: Detalle con ID inválido (no existe)
**Pasos:**
1. Navegar a PlaceDetail con un ID que no existe
2. Observar la pantalla

**Resultado Esperado:**
- ✅ Se muestra mensaje: "Lugar no encontrado"
- ✅ **NO hay crash** (no se usa `place!!`)
- ✅ No se muestra contenido del lugar

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 5.3: Galería de imágenes
**Pasos:**
1. Ver detalle de un lugar con múltiples imágenes
2. Hacer scroll horizontal en la galería

**Resultado Esperado:**
- ✅ LazyRow muestra todas las imágenes
- ✅ Scroll horizontal funciona
- ✅ Imágenes cargan con Coil/AsyncImage
- ✅ Si no hay imágenes, muestra placeholder

**Verificación en código:**
```kotlin
// PlaceDetailScreen.kt
LazyRow {
    items(place.images) { imageUrl ->
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del lugar",
            // ...
        )
    }
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 5.4: Estado Abierto/Cerrado
**Pasos:**
1. Ver detalle de un lugar con horarios definidos
2. Verificar el estado mostrado según la hora actual

**Resultado Esperado:**
- ✅ Si la hora actual está dentro del horario: muestra "Abierto 🟢"
- ✅ Si la hora actual está fuera del horario: muestra "Cerrado 🔴"
- ✅ Si no hay horarios: muestra "Horario no disponible"

**Verificación en código:**
```kotlin
// PlaceDetailScreen.kt
fun calculateScheduleStatus(schedules: List<Schedule>): String {
    if (schedules.isEmpty()) return "Horario no disponible"
    
    val now = LocalTime.now()
    val dayOfWeek = LocalDate.now().dayOfWeek.value
    
    val todaySchedule = schedules.find { it.day == dayOfWeek }
        ?: return "Cerrado"
    
    val openTime = LocalTime.parse(todaySchedule.openTime)
    val closeTime = LocalTime.parse(todaySchedule.closeTime)
    
    return if (now.isAfter(openTime) && now.isBefore(closeTime)) {
        "Abierto"
    } else {
        "Cerrado"
    }
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 5.5: Teléfonos múltiples
**Pasos:**
1. Ver detalle de un lugar con múltiples teléfonos
2. Verificar que todos se muestran

**Resultado Esperado:**
- ✅ Cada teléfono se muestra en una línea separada
- ✅ Formato correcto
- ✅ Si no hay teléfonos, muestra "No disponible"

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 6️⃣ Agregar Review

### Objetivo
Verificar que un usuario puede agregar una review y que se muestra inmediatamente.

### Pasos de Prueba

#### Test 6.1: Agregar review exitosamente
**Pasos:**
1. Login como usuario
2. Ir a detalle de un lugar
3. Scroll hasta la sección de reviews
4. Ingresar:
   - **Rating:** 5 estrellas
   - **Comentario:** "Excelente lugar, muy recomendado"
5. Hacer clic en "Enviar Review"

**Resultado Esperado:**
- ✅ Review creada con:
  - `id`: UUID generado
  - `userId`: `sessionManager.getUserId()`
  - `placeId`: ID del lugar actual
  - `rating`: 5
  - `comment`: "Excelente lugar, muy recomendado"
  - `timestamp`: Fecha/hora actual
- ✅ Review agregada a `reviewsViewModel.reviews`
- ✅ Review aparece **inmediatamente** en la lista (reactividad)
- ✅ Rating promedio se actualiza
- ✅ Formulario se limpia

**Verificación en código:**
```kotlin
// PlaceDetailScreen.kt
val newReview = Review(
    id = UUID.randomUUID().toString(),
    userId = currentUserId,
    placeId = id,
    rating = selectedRating,
    comment = comment,
    timestamp = System.currentTimeMillis()
)
reviewsViewModel.addReview(newReview)
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 6.2: Validación de rating
**Pasos:**
1. Intentar enviar review sin seleccionar rating (0 estrellas)
2. Hacer clic en "Enviar Review"

**Resultado Esperado:**
- ✅ Se muestra error: "Selecciona una calificación"
- ✅ No se crea la review

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 6.3: Validación de comentario vacío
**Pasos:**
1. Seleccionar rating pero dejar comentario vacío
2. Hacer clic en "Enviar Review"

**Resultado Esperado:**
- ✅ Se muestra error: "El comentario no puede estar vacío"
- ✅ No se crea la review

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 6.4: Múltiples reviews del mismo usuario
**Pasos:**
1. Agregar una review
2. Agregar otra review del mismo usuario al mismo lugar

**Resultado Esperado:**
- ✅ Ambas reviews se crean correctamente
- ✅ Ambas aparecen en la lista
- ✅ (Opcional: Implementar lógica para permitir solo 1 review por usuario)

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 6.5: Rating promedio se actualiza
**Pasos:**
1. Ver rating promedio de un lugar (ej: 4.0)
2. Agregar review con rating 5
3. Observar rating promedio actualizado

**Resultado Esperado:**
- ✅ Rating promedio se recalcula automáticamente
- ✅ Se muestra el nuevo promedio
- ✅ Número de reviews se incrementa

**Verificación en código:**
```kotlin
// RewiewsViewModel.kt
fun getAverageRating(placeId: String): StateFlow<Double> {
    return findByPlaceId(placeId).map { reviews ->
        if (reviews.isEmpty()) 0.0
        else reviews.map { it.rating }.average()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 7️⃣ Sistema de Favoritos

### Objetivo
Verificar que un usuario puede marcar/desmarcar lugares como favoritos.

### Pasos de Prueba

#### Test 7.1: Marcar lugar como favorito
**Pasos:**
1. Login como usuario
2. Ir a detalle de un lugar
3. Hacer clic en el botón de favorito (corazón vacío)

**Resultado Esperado:**
- ✅ Icono cambia a corazón lleno ❤️
- ✅ `placeId` se agrega a `user.favorites`
- ✅ Cambio se refleja inmediatamente en UI (reactividad)
- ✅ `usersViewModel.users` se actualiza

**Verificación en código:**
```kotlin
// UsersViewModel.kt - toggleFavorite()
fun toggleFavorite(userId: String, placeId: String) {
    _users.value = _users.value.map { user ->
        if (user.id == userId) {
            val updatedFavorites = if (user.favorites.contains(placeId)) {
                user.favorites - placeId
            } else {
                user.favorites + placeId
            }
            user.copy(favorites = updatedFavorites)
        } else {
            user
        }
    }
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 7.2: Desmarcar lugar como favorito
**Pasos:**
1. Marcar lugar como favorito (corazón lleno)
2. Hacer clic nuevamente en el botón de favorito

**Resultado Esperado:**
- ✅ Icono cambia a corazón vacío 🤍
- ✅ `placeId` se elimina de `user.favorites`
- ✅ Cambio se refleja inmediatamente

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 7.3: Favoritos persisten en sesión
**Pasos:**
1. Marcar 3 lugares como favoritos
2. Navegar a otra pantalla
3. Volver a los detalles de esos lugares

**Resultado Esperado:**
- ✅ Los 3 lugares siguen marcados como favoritos
- ✅ Estado se mantiene durante toda la sesión

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 7.4: Favoritos se pierden al cerrar sesión
**Pasos:**
1. Marcar lugares como favoritos
2. Cerrar sesión
3. Login con otro usuario
4. Ver los mismos lugares

**Resultado Esperado:**
- ✅ Los lugares NO están marcados como favoritos para el nuevo usuario
- ✅ Cada usuario tiene su propia lista de favoritos

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 8️⃣ Admin: Moderación de Lugares

### Objetivo
Verificar que un admin puede aprobar/rechazar lugares pendientes.

### Pasos de Prueba

#### Test 8.1: Ver lista de lugares pendientes
**Pasos:**
1. Login como usuario regular
2. Crear 3 lugares
3. Cerrar sesión
4. Login como admin (`admin@test.com` / `admin123`)
5. Ir a **PlacesList** (pantalla de moderación)

**Resultado Esperado:**
- ✅ Se muestran los 3 lugares creados
- ✅ Cada lugar muestra:
  - Nombre
  - Descripción
  - Imagen (o placeholder)
  - Badge "Pendiente"
  - Botones "Aprobar" y "Rechazar"
- ✅ No se muestran lugares ya aprobados

**Verificación en código:**
```kotlin
// PlacesViewModel.kt
fun getPendingPlaces(): StateFlow<List<Place>> {
    return _places.map { places ->
        places.filter { !it.approved }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 8.2: Aprobar lugar
**Pasos:**
1. En PlacesList, hacer clic en "Aprobar" de un lugar
2. Observar cambios

**Resultado Esperado:**
- ✅ `place.approved` cambia a `true`
- ✅ Lugar desaparece de la lista de pendientes **inmediatamente**
- ✅ Se crea registro en `moderationRecords`:
  - `action`: "approved"
  - `placeId`: ID del lugar
  - `placeName`: Nombre del lugar
  - `moderatorId`: `sessionManager.getUserId()`
  - `timestamp`: Fecha/hora actual
  - `reason`: `null`
- ✅ Lugar ahora aparece en HomeUser para usuarios regulares

**Verificación en código:**
```kotlin
// PlacesViewModel.kt - approvePlace()
fun approvePlace(placeId: String, moderatorId: String) {
    _places.value = _places.value.map { place ->
        if (place.id == placeId) {
            place.copy(approved = true)
        } else {
            place
        }
    }
    
    val place = _places.value.find { it.id == placeId }
    if (place != null) {
        val record = ModerationRecord(
            id = UUID.randomUUID().toString(),
            action = "approved",
            placeId = placeId,
            placeName = place.name,
            moderatorId = moderatorId,
            timestamp = System.currentTimeMillis(),
            reason = null
        )
        _moderationRecords.value += record
    }
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 8.3: Rechazar lugar con razón
**Pasos:**
1. En PlacesList, hacer clic en "Rechazar" de un lugar
2. En el diálogo que aparece, ingresar razón: "Información incompleta"
3. Confirmar

**Resultado Esperado:**
- ✅ Se abre diálogo con campo de texto para razón
- ✅ `place.approved` sigue siendo `false` (o se marca como rechazado)
- ✅ Lugar desaparece de la lista de pendientes
- ✅ Se crea registro en `moderationRecords`:
  - `action`: "rejected"
  - `reason`: "Información incompleta"
- ✅ Lugar NO aparece en HomeUser

**Verificación en código:**
```kotlin
// PlacesViewModel.kt - rejectPlace()
fun rejectPlace(placeId: String, moderatorId: String, reason: String?) {
    // Actualizar place o marcarlo como rechazado
    _places.value = _places.value.filter { it.id != placeId }
    
    val place = _places.value.find { it.id == placeId }
    val record = ModerationRecord(
        id = UUID.randomUUID().toString(),
        action = "rejected",
        placeId = placeId,
        placeName = place?.name ?: "Desconocido",
        moderatorId = moderatorId,
        timestamp = System.currentTimeMillis(),
        reason = reason
    )
    _moderationRecords.value += record
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 8.4: Rechazar lugar sin razón (opcional)
**Pasos:**
1. Hacer clic en "Rechazar"
2. Dejar campo de razón vacío
3. Confirmar

**Resultado Esperado:**
- ✅ Lugar se rechaza correctamente
- ✅ Registro tiene `reason = null` o `reason = ""`

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 8.5: Validar que solo admin puede moderar
**Pasos:**
1. Login como usuario regular
2. Intentar navegar manualmente a PlacesList (si es posible)

**Resultado Esperado:**
- ✅ Usuario regular NO puede acceder a PlacesList
- ✅ Solo está disponible en HomeAdmin
- ✅ No hay forma de que un usuario regular modere lugares

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 9️⃣ Admin: Historial de Moderación

### Objetivo
Verificar que se muestra el historial completo de acciones de moderación.

### Pasos de Prueba

#### Test 9.1: Ver historial de moderación
**Pasos:**
1. Como admin, aprobar 2 lugares
2. Rechazar 1 lugar con razón
3. Ir a **HistoryScreen**

**Resultado Esperado:**
- ✅ Se muestran 3 registros en orden cronológico (más reciente primero)
- ✅ Cada registro muestra:
  - Acción: "Aprobado" (verde) o "Rechazado" (rojo)
  - Nombre del lugar
  - ID del lugar
  - ID del moderador
  - Fecha/hora formateada: "dd/MM/yyyy HH:mm"
  - Razón (solo si fue rechazado)
  - Icono: ✓ (aprobado) o ✗ (rechazado)

**Verificación en código:**
```kotlin
// HistoryScreen.kt
val moderationRecords = placesViewModel.moderationRecords.collectAsState().value
val sortedRecords = moderationRecords.sortedByDescending { it.timestamp }

// Formato de fecha
val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
val formattedDate = sdf.format(Date(record.timestamp))
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 9.2: Colores según acción
**Pasos:**
1. Ver historial con acciones aprobadas y rechazadas

**Resultado Esperado:**
- ✅ Registros aprobados: fondo verde claro, texto verde
- ✅ Registros rechazados: fondo rojo claro, texto rojo
- ✅ Diferencia visual clara entre ambos tipos

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 9.3: Historial vacío
**Pasos:**
1. Login como admin sin haber moderado nada
2. Ir a HistoryScreen

**Resultado Esperado:**
- ✅ Se muestra mensaje: "No hay registros de moderación"
- ✅ No hay crash

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 9.4: Razón de rechazo
**Pasos:**
1. Rechazar un lugar con razón: "Contenido inapropiado"
2. Ver el registro en HistoryScreen

**Resultado Esperado:**
- ✅ Se muestra campo "Razón: Contenido inapropiado"
- ✅ Solo aparece para registros rechazados
- ✅ Registros aprobados NO muestran campo de razón

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 🔟 Eliminar Lugar (Propietario)

### Objetivo
Verificar que un propietario puede eliminar sus propios lugares.

### Pasos de Prueba

#### Test 10.1: Eliminar lugar propio
**Pasos:**
1. Login como usuario
2. Crear un lugar
3. Ir a HomeUser o PlacesScreen
4. Buscar el lugar creado
5. Hacer clic en opción "Eliminar" (si está implementada)

**Resultado Esperado:**
- ✅ Solo el propietario ve opción de eliminar
- ✅ Se muestra confirmación: "¿Estás seguro de eliminar este lugar?"
- ✅ Al confirmar, lugar se elimina de `placesViewModel.places`
- ✅ Lugar desaparece de la UI inmediatamente
- ✅ No hay crash

**Verificación en código:**
```kotlin
// PlacesViewModel.kt - deletePlace()
fun deletePlace(placeId: String, userId: String) {
    val place = _places.value.find { it.id == placeId }
    if (place?.ownerId == userId) {
        _places.value = _places.value.filter { it.id != placeId }
    }
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido (si no está implementado)

**Notas:**
```
_________________________________________
```

---

#### Test 10.2: No poder eliminar lugar ajeno
**Pasos:**
1. Login como usuario A
2. Crear un lugar
3. Cerrar sesión
4. Login como usuario B
5. Intentar eliminar el lugar de usuario A

**Resultado Esperado:**
- ✅ Usuario B NO ve opción de eliminar
- ✅ Si intenta eliminar programáticamente, falla la validación
- ✅ Solo propietario puede eliminar

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido (si no está implementado)

**Notas:**
```
_________________________________________
```

---

#### Test 10.3: Admin puede eliminar cualquier lugar
**Pasos:**
1. Login como admin
2. Ver lista de lugares
3. Intentar eliminar un lugar de otro usuario

**Resultado Esperado:**
- ✅ Admin puede eliminar cualquier lugar (opcional)
- ✅ Se registra en historial de moderación (opcional)

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido (si no está implementado)

**Notas:**
```
_________________________________________
```

---

## 1️⃣1️⃣ Cerrar Sesión

### Objetivo
Verificar que un usuario puede cerrar sesión correctamente.

### Pasos de Prueba

#### Test 11.1: Cerrar sesión exitosamente
**Pasos:**
1. Login con cualquier usuario
2. Navegar a perfil o configuración
3. Hacer clic en "Cerrar Sesión"

**Resultado Esperado:**
- ✅ Se llama a `sessionManager.clearSession()`
- ✅ `getUserId()` retorna `null`
- ✅ Navega automáticamente a **Login**
- ✅ Backstack se limpia (no se puede volver atrás)

**Verificación en código:**
```kotlin
// ProfileScreen o similar
Button(onClick = {
    sessionManager.clearSession()
    navController.navigate(RouteScreen.Login.route) {
        popUpTo(0) { inclusive = true }
    }
}) {
    Text("Cerrar Sesión")
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 11.2: Verificar limpieza de sesión
**Pasos:**
1. Login y cerrar sesión
2. Cerrar y volver a abrir la app

**Resultado Esperado:**
- ✅ La app muestra Login (no hay auto-login)
- ✅ No hay información de sesión anterior

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 11.3: No poder acceder a pantallas protegidas sin sesión
**Pasos:**
1. Cerrar sesión
2. Intentar navegar manualmente a HomeUser o HomeAdmin

**Resultado Esperado:**
- ✅ No es posible acceder sin sesión
- ✅ Redirige a Login

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 1️⃣2️⃣ Navegación y ViewModels Compartidos

### Objetivo
Verificar que la arquitectura de navegación y ViewModels compartidos funciona correctamente.

### Pasos de Prueba

#### Test 12.1: ViewModels compartidos entre pantallas
**Pasos:**
1. Login como usuario
2. Crear un lugar en CreatePlaceScreen
3. Sin recargar, ir a PlacesScreen

**Resultado Esperado:**
- ✅ El lugar creado aparece inmediatamente en PlacesScreen
- ✅ Ambas pantallas usan la misma instancia de `placesViewModel`
- ✅ No hay necesidad de "refresh"

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 12.2: Estado persiste durante navegación
**Pasos:**
1. Marcar 3 lugares como favoritos
2. Navegar entre HomeUser, PlacesScreen, PlaceDetail
3. Verificar que favoritos se mantienen

**Resultado Esperado:**
- ✅ Estado de favoritos persiste durante toda la sesión
- ✅ ViewModels no se recrean en cada navegación

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 12.3: Reactividad automática
**Pasos:**
1. Abrir PlaceDetailScreen de un lugar
2. Como admin, aprobar ese lugar
3. Verificar que el cambio se refleja inmediatamente en PlaceDetail

**Resultado Esperado:**
- ✅ UI se actualiza automáticamente sin necesidad de recargar
- ✅ StateFlow emite nuevo valor
- ✅ Composables se recomponen

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 12.4: Navegación hacia atrás
**Pasos:**
1. Navegar Login → HomeUser → PlaceDetail
2. Presionar botón "Atrás" del dispositivo

**Resultado Esperado:**
- ✅ Navega a HomeUser
- ✅ Estado se mantiene

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

#### Test 12.5: Type-safe navigation
**Pasos:**
1. Navegar a PlaceDetail con ID válido
2. Verificar que el parámetro se pasa correctamente

**Resultado Esperado:**
- ✅ ID se extrae con `toRoute<UserScreen.PlaceDetail>()`
- ✅ No hay problemas de parsing
- ✅ No hay uso de Strings manuales

**Verificación en código:**
```kotlin
composable<UserScreen.PlaceDetail> {
    val args = it.toRoute<UserScreen.PlaceDetail>()
    PlaceDetailScreen(
        id = args.id,  // ✓ Type-safe
        // ...
    )
}
```

**Estado:** ⬜ No probado | ✅ Aprobado | ❌ Fallido

**Notas:**
```
_________________________________________
```

---

## 🎯 Resumen de Resultados

### Estadísticas Globales

| Categoría | Total Tests | ✅ Aprobados | ❌ Fallidos | ⬜ No Probados |
|-----------|-------------|--------------|-------------|----------------|
| 1. Auto-login | 4 | 0 | 0 | 4 |
| 2. Registro | 4 | 0 | 0 | 4 |
| 3. Login | 4 | 0 | 0 | 4 |
| 4. Crear Lugar | 6 | 0 | 0 | 6 |
| 5. Detalle Lugar | 5 | 0 | 0 | 5 |
| 6. Reviews | 5 | 0 | 0 | 5 |
| 7. Favoritos | 4 | 0 | 0 | 4 |
| 8. Moderación | 5 | 0 | 0 | 5 |
| 9. Historial | 4 | 0 | 0 | 4 |
| 10. Eliminar | 3 | 0 | 0 | 3 |
| 11. Cerrar Sesión | 3 | 0 | 0 | 3 |
| 12. Navegación | 5 | 0 | 0 | 5 |
| **TOTAL** | **52** | **0** | **0** | **52** |

### Criterios de Aceptación Generales

- [ ] ✅ No hay crashes por NPE (no se usa `!!`)
- [ ] ✅ Cambios en ViewModels se reflejan inmediatamente en UI
- [ ] ✅ ViewModels compartidos funcionan correctamente
- [ ] ✅ Sin persistencia externa (todo en memoria)
- [ ] ✅ Null safety en todo el código UI
- [ ] ✅ No hay duplicación de ViewModels

### Issues Encontrados

| ID | Descripción | Severidad | Pantalla Afectada | Estado |
|----|-------------|-----------|-------------------|--------|
| - | - | - | - | - |

**Severidad:** 🔴 Crítico | 🟡 Mayor | 🟢 Menor

---

## 📝 Notas Adicionales

### Datos de Prueba Precargados

```kotlin
// UsersViewModel.kt - init
private val _users = MutableStateFlow(
    listOf(
        User(
            id = "admin-1",
            name = "Admin User",
            email = "admin@test.com",
            password = "admin123",
            city = "Bogotá",
            profileImage = "",
            role = Role.ADMIN,
            favorites = emptyList()
        ),
        User(
            id = "user-1",
            name = "Test User",
            email = "user@test.com",
            password = "user123",
            city = "Pereira",
            profileImage = "",
            role = Role.USER,
            favorites = emptyList()
        )
    )
)
```

### Herramientas de Debugging

Para facilitar las pruebas, agregar logs temporales:

```kotlin
// Navigation.kt
LaunchedEffect(Unit) {
    val currentUserId = sessionManager.getUserId()
    Log.d("QA_TEST", "Auto-login: userId = $currentUserId")
}

// PlacesViewModel.kt
fun addPlace(place: Place) {
    Log.d("QA_TEST", "Adding place: ${place.id}, approved=${place.approved}")
    _places.value += place
}

// UsersViewModel.kt
fun toggleFavorite(userId: String, placeId: String) {
    Log.d("QA_TEST", "Toggle favorite: user=$userId, place=$placeId")
    // ...
}
```

### Comandos de Verificación

```bash
# Compilar en modo debug
./gradlew compileDebugKotlin

# Generar APK de prueba
./gradlew assembleDebug

# Limpiar build
./gradlew clean

# Ver logs en tiempo real
adb logcat | grep "QA_TEST"
```

---

## ✅ Firma de Aprobación

| Rol | Nombre | Fecha | Firma |
|-----|--------|-------|-------|
| QA Tester | | | |
| Developer | | | |
| Project Lead | | | |

---

**Versión del Documento:** 1.0  
**Última Actualización:** Octubre 2025  
**Próxima Revisión:** Post-integración de base de datos (Fase 3)

---

## 🔗 Referencias

- [Navigation.kt](/app/src/main/java/co/edu/eam/lugaresapp/ui/navigation/Navigation.kt)
- [PlacesViewModel.kt](/app/src/main/java/co/edu/eam/lugaresapp/viewmodel/PlacesViewModel.kt)
- [UsersViewModel.kt](/app/src/main/java/co/edu/eam/lugaresapp/viewmodel/UsersViewModel.kt)
- [RewiewsViewModel.kt](/app/src/main/java/co/edu/eam/lugaresapp/viewmodel/RewiewsViewModel.kt)
- [SessionManager.kt](/app/src/main/java/co/edu/eam/lugaresapp/utils/SessionManager.kt)

---

**FIN DEL CHECKLIST** 🎉
