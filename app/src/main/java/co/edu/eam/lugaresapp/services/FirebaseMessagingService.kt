package co.edu.eam.lugaresapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import co.edu.eam.lugaresapp.MainActivity
import co.edu.eam.lugaresapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * SERVICIO DE NOTIFICACIONES PUSH
 * 
 * Servicio que maneja las notificaciones push de Firebase Cloud Messaging.
 * Extiende FirebaseMessagingService para recibir y procesar mensajes push.
 * 
 * CARACTERÍSTICAS:
 * - Recibe notificaciones cuando la app está en foreground/background
 * - Crea canal de notificaciones para Android 8+
 * - Muestra notificaciones con título, mensaje e icono
 * - Navega a la app al hacer click en la notificación
 * - Registra token FCM para enviar notificaciones específicas al dispositivo
 * 
 * CASOS DE USO:
 * - Notificar aprobación/rechazo de lugares creados
 * - Avisar sobre nuevos comentarios en lugares del usuario
 * - Alertar sobre respuestas a comentarios
 * - Notificar cambios en favoritos
 * 
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: Solo maneja notificaciones push
 * - Open/Closed: Extensible para diferentes tipos de notificaciones
 */
class LugaresFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "lugares_notifications"
        private const val CHANNEL_NAME = "Notificaciones de Lugares"
        private const val CHANNEL_DESCRIPTION = "Notificaciones sobre lugares, comentarios y aprobaciones"
    }

    /**
     * Se llama cuando se recibe un nuevo token de registro FCM
     * Este token identifica únicamente este dispositivo para enviar notificaciones
     * 
     * @param token Token FCM del dispositivo
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token FCM: $token")
        
        // TODO: Enviar el token al servidor backend para almacenarlo
        // y poder enviar notificaciones push a este dispositivo específico
        sendTokenToServer(token)
    }

    /**
     * Se llama cuando se recibe un mensaje push
     * 
     * @param remoteMessage Mensaje recibido con datos y notificación
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "Mensaje recibido de: ${remoteMessage.from}")
        
        // Verificar si el mensaje contiene una notificación
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "UniLocal"
            val body = notification.body ?: ""
            
            Log.d(TAG, "Notificación - Título: $title, Mensaje: $body")
            
            showNotification(title, body)
        }
        
        // Verificar si el mensaje contiene datos adicionales
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Datos del mensaje: ${remoteMessage.data}")
            
            // Procesar datos específicos según el tipo de notificación
            handleNotificationData(remoteMessage.data)
        }
    }

    /**
     * Muestra una notificación en la barra de notificaciones
     * 
     * @param title Título de la notificación
     * @param message Mensaje de la notificación
     */
    private fun showNotification(title: String, message: String) {
        // Crear canal de notificaciones (necesario para Android 8+)
        createNotificationChannel()
        
        // Intent para abrir la app al hacer click en la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Construir la notificación
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.red_marker) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Se cierra al hacer click
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
        
        // Mostrar la notificación
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    /**
     * Crea el canal de notificaciones para Android 8.0+
     * Los canales permiten al usuario controlar las notificaciones por categoría
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Procesa los datos adicionales del mensaje push
     * Permite realizar acciones específicas según el tipo de notificación
     * 
     * @param data Mapa con los datos del mensaje
     */
    private fun handleNotificationData(data: Map<String, String>) {
        val type = data["type"] // Tipo de notificación: place_approved, new_comment, etc.
        val placeId = data["place_id"]
        val commentId = data["comment_id"]
        
        when (type) {
            "place_approved" -> {
                Log.d(TAG, "Lugar aprobado: $placeId")
                // TODO: Actualizar estado local del lugar si está en memoria
            }
            "place_rejected" -> {
                Log.d(TAG, "Lugar rechazado: $placeId")
                // TODO: Notificar al usuario sobre el rechazo
            }
            "new_comment" -> {
                Log.d(TAG, "Nuevo comentario en lugar: $placeId")
                // TODO: Actualizar lista de comentarios si está visible
            }
            "comment_reply" -> {
                Log.d(TAG, "Respuesta a comentario: $commentId")
                // TODO: Navegar al comentario específico
            }
        }
    }

    /**
     * Envía el token FCM al servidor backend
     * El servidor debe almacenar este token asociado al usuario
     * para poder enviar notificaciones específicas
     * 
     * @param token Token FCM del dispositivo
     */
    private fun sendTokenToServer(token: String) {
        // TODO: Implementar cuando haya backend
        // API call: POST /api/users/{userId}/fcm-token
        // Body: { "token": token }
        
        Log.d(TAG, "Token FCM listo para enviar al servidor: $token")
    }
}
