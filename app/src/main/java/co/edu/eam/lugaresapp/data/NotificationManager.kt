package co.edu.eam.lugaresapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

/**
 * ADMINISTRADOR DE NOTIFICACIONES
 * 
 * Clase que gestiona los permisos y tokens de Firebase Cloud Messaging.
 * Proporciona métodos para verificar permisos y obtener el token FCM.
 * 
 * CARACTERÍSTICAS:
 * - Verifica si se tienen permisos de notificaciones (Android 13+)
 * - Obtiene el token FCM del dispositivo
 * - Suscribe a topics de Firebase para notificaciones grupales
 * 
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: Solo gestiona configuración de notificaciones
 * - Dependency Inversion: Depende de abstracciones (Context)
 * 
 * @param context Contexto de la aplicación
 */
class NotificationManager(private val context: Context) {
    
    companion object {
        private const val TAG = "NotificationManager"
    }
    
    /**
     * Verifica si la app tiene permisos para mostrar notificaciones
     * En Android 13+ se requiere permiso POST_NOTIFICATIONS
     * 
     * @return true si tiene permiso, false si no
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // En versiones anteriores no se requiere permiso
        }
    }
    
    /**
     * Obtiene el token FCM del dispositivo
     * Este token se debe enviar al backend para recibir notificaciones
     * 
     * @param onTokenReceived Callback que recibe el token
     */
    fun getFCMToken(onTokenReceived: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Error al obtener token FCM", task.exception)
                return@addOnCompleteListener
            }
            
            // Token obtenido exitosamente
            val token = task.result
            Log.d(TAG, "Token FCM obtenido: $token")
            onTokenReceived(token)
        }
    }
    
    /**
     * Suscribe el dispositivo a un topic de Firebase
     * Los topics permiten enviar notificaciones a grupos de usuarios
     * 
     * Ejemplos de topics:
     * - "all_users": Todos los usuarios
     * - "new_places": Notificar sobre lugares nuevos
     * - "moderation": Para moderadores
     * 
     * @param topic Nombre del topic
     * @param onComplete Callback que indica si fue exitoso
     */
    fun subscribeToTopic(topic: String, onComplete: (Boolean) -> Unit = {}) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                val success = task.isSuccessful
                if (success) {
                    Log.d(TAG, "Suscrito al topic: $topic")
                } else {
                    Log.w(TAG, "Error al suscribirse al topic: $topic", task.exception)
                }
                onComplete(success)
            }
    }
    
    /**
     * Cancela la suscripción a un topic de Firebase
     * 
     * @param topic Nombre del topic
     * @param onComplete Callback que indica si fue exitoso
     */
    fun unsubscribeFromTopic(topic: String, onComplete: (Boolean) -> Unit = {}) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                val success = task.isSuccessful
                if (success) {
                    Log.d(TAG, "Desuscrito del topic: $topic")
                } else {
                    Log.w(TAG, "Error al desuscribirse del topic: $topic", task.exception)
                }
                onComplete(success)
            }
    }
}
