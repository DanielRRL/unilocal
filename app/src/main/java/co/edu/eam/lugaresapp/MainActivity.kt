package co.edu.eam.lugaresapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import co.edu.eam.lugaresapp.ui.navigation.AppNavigation
import co.edu.eam.lugaresapp.ui.theme.LugaresAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

/**
 * ACTIVIDAD PRINCIPAL uniLocal
 * 
 * Esta es la única Activity de la aplicación (patrón Single Activity).
 * Todo el resto de la UI se maneja con Jetpack Compose y Navigation Compose.
 * 
 * RESPONSABILIDADES:
 * - Punto de entrada de la aplicación Android
 * - Configuración del tema uniLocal
 * - Inicialización del sistema de navegación
 * - Configuración de edge-to-edge display
 * 
 * CONCEPTOS CLAVE:
 * - ComponentActivity: Clase base que soporta Jetpack Compose
 * - Single Activity Pattern: Una sola Activity, múltiples Composables
 * - Edge-to-edge: UI que se extiende hasta los bordes de la pantalla
 * - Scaffold: Layout base de Material 3 que proporciona estructura
 */
class MainActivity : ComponentActivity() {
    
    /**
     * MÉTODO PRINCIPAL DE INICIALIZACIÓN
     * 
     * onCreate() se ejecuta cuando Android crea la Activity.
     * Es donde configuramos toda la UI de la aplicación.
     * 
     * @param savedInstanceState: Bundle? - Estado guardado de la Activity (si existe)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Llamada obligatoria al método padre
        
        // ==================== INICIALIZACIÓN DE FIREBASE ====================
        
        try {
            // Inicializar Firebase App explícitamente
            FirebaseApp.initializeApp(this)
            Log.d("MainActivity", "✅ Firebase inicializado correctamente")
            
            // Verificar conexión a Firestore
            val db = FirebaseFirestore.getInstance()
            Log.d("MainActivity", "✅ Firestore instance obtenida: ${db.app.name}")
            
            // Habilitar logging de Firestore para debugging
            FirebaseFirestore.setLoggingEnabled(true)
            Log.d("MainActivity", "✅ Firestore logging habilitado")
            
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ ERROR al inicializar Firebase: ${e.message}", e)
        }
        
        /**
         * HABILITACIÓN DE EDGE-TO-EDGE
         * 
         * enableEdgeToEdge() permite que la UI se extienda hasta los bordes
         * de la pantalla, incluyendo debajo de la barra de estado y navegación.
         * Esto da una experiencia más inmersiva y moderna.
         */
        enableEdgeToEdge()
        
        /**
         * CONFIGURACIÓN DEL CONTENIDO COMPOSE
         * 
         * setContent {} es la función que conecta Jetpack Compose con la Activity.
         * Todo lo que esté dentro de este bloque será renderizado como UI.
         */
        setContent {
            /**
             * APLICACIÓN DEL TEMA uniLocal
             * 
             * LugaresAppTheme envuelve toda la aplicación y aplica:
             * - Colores del mockup azul (#87CEEB, #d2e8feff, etc.)
             * - Tipografía Material 3
             * - Esquemas light/dark
             * - Todos los tokens de diseño de uniLocal
             */
            LugaresAppTheme {
                /**
                 * SCAFFOLD - ESTRUCTURA BASE
                 * 
                 * Scaffold es el layout fundamental de Material 3 que proporciona:
                 * - Estructura base para top bar, bottom bar, FAB, etc.
                 * - Manejo automático de insets (barras del sistema)
                 * - innerPadding que respeta las barras del sistema
                 * 
                 * En nuestro caso, solo usamos el innerPadding para que el contenido
                 * no se superponga con las barras del sistema.
                 */
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /**
                     * NAVEGACIÓN PRINCIPAL
                     * 
                     * AppNavigation es nuestro sistema de navegación que maneja
                     * todas las pantallas de la aplicación (Login, Register, Home, etc.)
                     * 
                     * El modifier.padding(innerPadding) asegura que el contenido
                     * no se superponga con las barras del sistema.
                     */
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
