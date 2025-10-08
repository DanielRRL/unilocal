package co.edu.eam.lugaresapp.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Clase ligera para manejar la sesión de usuario usando SharedPreferences.
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "unilocal_prefs"
        private const val KEY_USER_ID = "current_user_id"
    }
    
    /**
     * Guarda el ID del usuario en sesión.
     */
    fun saveUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }
    
    /**
     * Obtiene el ID del usuario en sesión, o null si no hay sesión activa.
     */
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }
    
    /**
     * Limpia la sesión del usuario.
     */
    fun clear() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }
}
