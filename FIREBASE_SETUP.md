# Configuración de Firebase Cloud Messaging

## 📱 Sistema de Notificaciones Push Implementado

Esta aplicación incluye un sistema completo de notificaciones push usando Firebase Cloud Messaging (FCM).

## 🔧 Configuración Requerida

### 1. Crear Proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Agrega una aplicación Android con el package name: `co.edu.eam.lugaresapp`

### 2. Descargar google-services.json

1. En Firebase Console, ve a Project Settings
2. En la sección "Your apps", selecciona tu app Android
3. Descarga el archivo `google-services.json`
4. **IMPORTANTE**: Reemplaza el archivo `app/google-services.json` con el que descargaste

### 3. Habilitar Cloud Messaging

1. En Firebase Console, ve a la sección "Cloud Messaging"
2. Verifica que esté habilitado
3. Guarda la **Server Key** para enviar notificaciones desde el backend

## 📂 Archivos Implementados

### Servicio de FCM
- **Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/services/FirebaseMessagingService.kt`
- **Función**: Recibe y procesa notificaciones push
- **Características**:
  - Crea canal de notificaciones
  - Muestra notificaciones con título y mensaje
  - Maneja clicks en notificaciones
  - Registra token FCM

### Administrador de Notificaciones
- **Ubicación**: `app/src/main/java/co/edu/eam/lugaresapp/data/NotificationManager.kt`
- **Función**: Gestiona permisos y tokens FCM
- **Métodos**:
  - `hasNotificationPermission()`: Verifica permisos
  - `getFCMToken()`: Obtiene token del dispositivo
  - `subscribeToTopic()`: Suscribe a topics grupales
  - `unsubscribeFromTopic()`: Cancela suscripciones

## 🔔 Tipos de Notificaciones Soportadas

El servicio está preparado para manejar los siguientes tipos:

1. **place_approved**: Cuando un lugar es aprobado
   ```json
   {
     "notification": {
       "title": "¡Lugar Aprobado!",
       "body": "Tu lugar 'Restaurante X' ha sido aprobado"
     },
     "data": {
       "type": "place_approved",
       "place_id": "12345"
     }
   }
   ```

2. **place_rejected**: Cuando un lugar es rechazado
   ```json
   {
     "notification": {
       "title": "Lugar No Aprobado",
       "body": "Tu lugar 'Cafetería Y' no cumple con los requisitos"
     },
     "data": {
       "type": "place_rejected",
       "place_id": "67890"
     }
   }
   ```

3. **new_comment**: Nuevo comentario en un lugar del usuario
   ```json
   {
     "notification": {
       "title": "Nuevo Comentario",
       "body": "Alguien comentó en tu lugar 'Bar Z'"
     },
     "data": {
       "type": "new_comment",
       "place_id": "12345",
       "comment_id": "98765"
     }
   }
   ```

4. **comment_reply**: Respuesta a un comentario
   ```json
   {
     "notification": {
       "title": "Nueva Respuesta",
       "body": "Respondieron a tu comentario"
     },
     "data": {
       "type": "comment_reply",
       "comment_id": "98765"
     }
   }
   ```

## 🚀 Uso en la Aplicación

### Obtener Token FCM

```kotlin
val notificationManager = NotificationManager(context)

// Verificar permisos (Android 13+)
if (notificationManager.hasNotificationPermission()) {
    // Obtener token
    notificationManager.getFCMToken { token ->
        // Enviar token al backend
        sendTokenToBackend(token)
    }
}
```

### Suscribirse a Topics

```kotlin
// Suscribir a todos los usuarios
notificationManager.subscribeToTopic("all_users")

// Suscribir a notificaciones de nuevos lugares
notificationManager.subscribeToTopic("new_places")
```

### Permisos de Notificaciones (Android 13+)

Agregar en tu Activity o Composable:

```kotlin
val requestPermissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        // Permiso concedido, obtener token
    }
}

// Solicitar permiso
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
}
```

## 🌐 Envío de Notificaciones desde Backend

### Usando Firebase Admin SDK (Node.js)

```javascript
const admin = require('firebase-admin');

// Enviar a un dispositivo específico
await admin.messaging().send({
  token: userToken,
  notification: {
    title: '¡Lugar Aprobado!',
    body: 'Tu lugar ha sido aprobado por los moderadores'
  },
  data: {
    type: 'place_approved',
    place_id: '12345'
  }
});

// Enviar a un topic
await admin.messaging().send({
  topic: 'all_users',
  notification: {
    title: 'Nuevo Lugar Disponible',
    body: 'Se agregó un nuevo restaurante cerca de ti'
  }
});
```

### Usando HTTP API

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "USER_FCM_TOKEN",
    "notification": {
      "title": "Título",
      "body": "Mensaje"
    },
    "data": {
      "type": "place_approved",
      "place_id": "12345"
    }
  }'
```

## ✅ Checklist de Configuración

- [ ] Crear proyecto en Firebase Console
- [ ] Agregar app Android con package `co.edu.eam.lugaresapp`
- [ ] Descargar y reemplazar `google-services.json`
- [ ] Habilitar Cloud Messaging en Firebase
- [ ] Guardar Server Key para backend
- [ ] Probar notificación de prueba desde Firebase Console
- [ ] Implementar endpoint en backend para almacenar tokens FCM
- [ ] Implementar lógica de envío de notificaciones en backend

## 🔒 Seguridad

- **NUNCA** commitear el archivo `google-services.json` real en repositorios públicos
- Agregar `google-services.json` al `.gitignore`
- Usar variables de entorno para Server Keys en backend
- Validar tokens FCM antes de enviar notificaciones

## 📚 Recursos

- [Documentación de FCM](https://firebase.google.com/docs/cloud-messaging)
- [FCM HTTP API](https://firebase.google.com/docs/cloud-messaging/http-server-ref)
- [Admin SDK](https://firebase.google.com/docs/admin/setup)
