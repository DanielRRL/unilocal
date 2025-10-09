# 🧪 Guía de Pruebas Manuales - UniLocal App

**Objetivo:** Guía paso a paso para ejecutar el checklist de pruebas manuales de la Fase 2.

---

## 📱 Preparación del Entorno de Pruebas

### 1. Requisitos Previos

- ✅ Android Studio instalado
- ✅ Dispositivo Android físico o emulador (API 24+)
- ✅ SDK de Android configurado
- ✅ Código fuente compilado sin errores

### 2. Compilar el Proyecto

```bash
cd /home/daniel/Escritorio/unilocal

# Limpiar build anterior
./gradlew clean

# Compilar en modo debug
./gradlew compileDebugKotlin

# Generar APK
./gradlew assembleDebug
```

**APK generado en:** `app/build/outputs/apk/debug/app-debug.apk`

### 3. Instalar en Dispositivo

#### Opción A: Desde Android Studio
1. Abrir el proyecto en Android Studio
2. Conectar dispositivo o iniciar emulador
3. Hacer clic en ▶️ "Run" (Shift+F10)
4. Esperar a que la app se instale y lance

#### Opción B: Desde Terminal
```bash
# Verificar dispositivos conectados
adb devices

# Instalar APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Abrir la app
adb shell am start -n co.edu.eam.lugaresapp/.MainActivity
```

---

## 🔍 Herramientas de Debugging

### 1. Logcat para Monitorear Logs

```bash
# Ver todos los logs
adb logcat

# Filtrar por tag de pruebas
adb logcat | grep "QA_TEST"

# Filtrar por paquete
adb logcat | grep "co.edu.eam.lugaresapp"

# Guardar logs en archivo
adb logcat > test_logs.txt
```

### 2. Inspeccionar Base de Datos (SharedPreferences)

```bash
# Acceder al shell del dispositivo
adb shell

# Navegar a datos de la app
cd /data/data/co.edu.eam.lugaresapp/shared_prefs/

# Ver contenido del archivo de sesión
cat user_session.xml
```

### 3. Limpiar Datos de la App

```bash
# Limpiar datos y cache
adb shell pm clear co.edu.eam.lugaresapp

# Reiniciar app
adb shell am start -n co.edu.eam.lugaresapp/.MainActivity
```

---

## 📋 Secuencia Recomendada de Pruebas

### Fase 1: Pruebas de Autenticación (30 min)

1. **Test 1.1 - 1.4:** Inicio y Auto-login
   - Instalar app fresh
   - Verificar pantalla de Login
   - Probar auto-login con diferentes roles

2. **Test 2.1 - 2.4:** Registro de Usuario
   - Registrar 2 usuarios nuevos
   - Probar validaciones

3. **Test 3.1 - 3.4:** Login
   - Login con usuarios precargados
   - Login con usuarios nuevos
   - Probar errores

### Fase 2: Pruebas de Funcionalidad Usuario (45 min)

4. **Test 4.1 - 4.6:** Creación de Lugar
   - Crear 5 lugares con diferentes datos
   - Probar todas las validaciones

5. **Test 5.1 - 5.5:** Detalle de Lugar
   - Ver detalles de lugares creados
   - Probar con ID inválido
   - Verificar galería, horarios, teléfonos

6. **Test 6.1 - 6.5:** Reviews
   - Agregar 3-5 reviews a diferentes lugares
   - Verificar cálculo de rating promedio

7. **Test 7.1 - 7.4:** Favoritos
   - Marcar 10 lugares como favoritos
   - Verificar persistencia

### Fase 3: Pruebas de Funcionalidad Admin (30 min)

8. **Test 8.1 - 8.5:** Moderación
   - Login como admin
   - Aprobar 3 lugares
   - Rechazar 2 lugares

9. **Test 9.1 - 9.4:** Historial
   - Verificar 5 registros de moderación
   - Verificar formato de fecha
   - Verificar colores

### Fase 4: Pruebas de Navegación y Arquitectura (20 min)

10. **Test 10.1 - 10.3:** Eliminar Lugar (si implementado)

11. **Test 11.1 - 11.3:** Cerrar Sesión
    - Probar logout con diferentes usuarios

12. **Test 12.1 - 12.5:** Navegación y ViewModels
    - Verificar reactividad
    - Verificar estado compartido

---

## 🎯 Escenarios de Prueba Específicos

### Escenario A: Flujo Completo de Usuario Regular

**Duración:** 15 minutos

```
1. Abrir app → Ver Login
2. Registrarse como "María López" (maria@test.com / maria123)
3. Auto-navegación a HomeUser
4. Ir a CreatePlace
5. Crear lugar "Restaurante La Montaña"
   - Descripción: "Deliciosa comida típica con vista panorámica"
   - Dirección: "Carrera 7 #15-30"
   - Teléfono: "3001112233"
   - Tipo: "Restaurante"
6. Verificar que el lugar NO aparece en PlacesScreen (no aprobado)
7. Ir a PlacesScreen y ver otros lugares aprobados
8. Abrir detalle de un lugar
9. Agregar review con 5 estrellas: "Excelente servicio"
10. Marcar el lugar como favorito ❤️
11. Cerrar sesión
12. Verificar regreso a Login
```

**✅ Criterios de Éxito:**
- No hay crashes en ningún paso
- Lugar se crea con `approved=false`
- Review aparece inmediatamente
- Favorito se marca correctamente
- Logout funciona

---

### Escenario B: Flujo Completo de Admin

**Duración:** 10 minutos

```
1. Login como admin (admin@test.com / admin123)
2. Auto-navegación a HomeAdmin
3. Ir a PlacesList (moderación)
4. Ver lista de lugares pendientes
5. Aprobar "Restaurante La Montaña"
6. Verificar que desaparece de la lista
7. Rechazar otro lugar con razón: "Dirección incorrecta"
8. Ir a HistoryScreen
9. Verificar 2 registros:
   - ✅ Aprobado: Restaurante La Montaña (verde)
   - ❌ Rechazado: [Otro lugar] (rojo, con razón)
10. Cerrar sesión
```

**✅ Criterios de Éxito:**
- Solo admin puede acceder a estas pantallas
- Aprobación actualiza estado inmediatamente
- Historial muestra todos los registros
- Colores y formato correctos

---

### Escenario C: Verificación de ViewModels Compartidos

**Duración:** 5 minutos

```
1. Login como usuario
2. Abrir CreatePlaceScreen
3. Crear lugar "Café Central"
4. SIN RECARGAR la app:
   a. Ir a PlacesScreen
   b. Verificar que "Café Central" NO aparece (no aprobado)
5. Login como admin
6. Aprobar "Café Central"
7. Login como usuario nuevamente
8. Ir a PlacesScreen
9. Verificar que "Café Central" AHORA SÍ aparece
```

**✅ Criterios de Éxito:**
- Cambios se reflejan sin necesidad de reiniciar
- ViewModels mantienen estado consistente
- Reactividad funciona correctamente

---

### Escenario D: Prueba de Estabilidad (Stress Test)

**Duración:** 20 minutos

```
1. Crear 20 usuarios diferentes
2. Con cada usuario, crear 2 lugares
3. Agregar 5 reviews a cada lugar
4. Marcar 10 lugares como favoritos
5. Como admin, aprobar 20 lugares
6. Rechazar 20 lugares
7. Navegar entre todas las pantallas 50 veces
8. Rotar el dispositivo 10 veces
9. Poner app en background y foreground 5 veces
10. Verificar que no hay crashes ni memory leaks
```

**✅ Criterios de Éxito:**
- No hay crashes en ningún momento
- UI responde rápidamente
- Scroll es fluido con 40 lugares
- Rotación mantiene estado
- No hay memory leaks (usar Android Profiler)

---

## 🐛 Reporte de Bugs

### Template de Bug Report

Cuando encuentres un bug, usar este formato:

```markdown
## Bug #[ID]

**Título:** [Descripción breve del bug]

**Severidad:** 🔴 Crítico | 🟡 Mayor | 🟢 Menor

**Pantalla:** [Nombre de la pantalla afectada]

**Pasos para Reproducir:**
1. [Paso 1]
2. [Paso 2]
3. [Paso 3]

**Resultado Esperado:**
[Lo que debería pasar]

**Resultado Actual:**
[Lo que realmente pasa]

**Stacktrace (si hay crash):**
```
[Pegar stacktrace aquí]
```

**Screenshots:**
[Adjuntar capturas de pantalla]

**Entorno:**
- Dispositivo: [Modelo]
- Android Version: [Ej: API 30 / Android 11]
- App Version: [Debug / Release]

**Workaround:**
[Si existe alguna solución temporal]
```

### Ejemplo de Bug Report

```markdown
## Bug #001

**Título:** Crash al aprobar lugar sin conexión

**Severidad:** 🔴 Crítico

**Pantalla:** PlacesListScreen

**Pasos para Reproducir:**
1. Login como admin
2. Ir a PlacesList
3. Desactivar internet
4. Hacer clic en "Aprobar" de un lugar

**Resultado Esperado:**
Se debe aprobar el lugar en memoria sin necesidad de internet

**Resultado Actual:**
La app crashea con NullPointerException

**Stacktrace:**
```
java.lang.NullPointerException: Attempt to invoke virtual method 'void ...' on a null object reference
    at PlacesViewModel.approvePlace(PlacesViewModel.kt:45)
    at PlacesListScreen.kt:123
```

**Entorno:**
- Dispositivo: Samsung Galaxy S21
- Android Version: API 30 / Android 11
- App Version: Debug

**Workaround:**
Mantener internet activado durante las pruebas
```

---

## 📊 Checklist de Validación Final

Antes de dar por completadas las pruebas, verificar:

### Funcionalidad
- [ ] ✅ Todos los tests de autenticación pasan
- [ ] ✅ Todos los tests de CRUD de lugares pasan
- [ ] ✅ Todos los tests de reviews y favoritos pasan
- [ ] ✅ Todos los tests de moderación pasan
- [ ] ✅ Navegación funciona correctamente

### Arquitectura
- [ ] ✅ No hay crashes por NPE (no se usa `!!`)
- [ ] ✅ ViewModels compartidos funcionan
- [ ] ✅ StateFlow emite cambios correctamente
- [ ] ✅ UI se actualiza automáticamente (reactividad)
- [ ] ✅ No hay memory leaks

### UX/UI
- [ ] ✅ Validaciones de formularios funcionan
- [ ] ✅ Mensajes de error son claros
- [ ] ✅ Loading states son visibles
- [ ] ✅ Scroll es fluido
- [ ] ✅ Botones responden al toque

### Performance
- [ ] ✅ App inicia en < 3 segundos
- [ ] ✅ Navegación es instantánea
- [ ] ✅ Listas con 50+ items no lag
- [ ] ✅ Rotación mantiene estado
- [ ] ✅ Background/Foreground funciona

---

## 🎓 Tips para Testing Efectivo

### 1. Usar Datos Realistas
```kotlin
// ❌ MAL
name = "a"
description = "b"

// ✅ BIEN
name = "Restaurante El Jardín"
description = "Hermoso restaurante con comida gourmet y ambiente familiar"
```

### 2. Probar Casos Edge
```kotlin
// Probar con:
- Strings vacíos
- Strings muy largos (1000+ caracteres)
- Caracteres especiales: áéíóú ñÑ !@#$%
- Emojis: 😀🎉❤️
- Números negativos
- IDs inválidos
```

### 3. Verificar Reactividad
```kotlin
// Después de cada acción, verificar:
1. UI se actualiza automáticamente
2. No hay necesidad de "pull to refresh"
3. Cambios son instantáneos
```

### 4. Documentar Todo
- Hacer screenshots de cada test
- Grabar video de flujos completos
- Anotar tiempos de respuesta
- Reportar cualquier anomalía

---

## 📞 Contacto y Soporte

**QA Lead:** [Nombre]  
**Email:** [email@test.com]  
**Slack:** @qa-team

**Desarrolladores:**
- Backend: [Nombre]
- Frontend: [Nombre]
- Mobile: [Nombre]

---

**Versión:** 1.0  
**Última Actualización:** Octubre 2025
