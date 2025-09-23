package co.edu.eam.lugaresapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * SISTEMA DE TEMAS uniLocal
 * 
 * Este archivo define el sistema de colores Material 3 para la aplicación uniLocal.
 * Material 3 usa un ColorScheme que mapea colores específicos a roles semánticos.
 * 
 * CONCEPTO CLAVE: En lugar de usar colores directamente, Material 3 asigna
 * roles como 'primary', 'secondary', 'background' etc. que se adaptan
 * automáticamente a modo claro/oscuro.
 */

/**
 * ESQUEMA DE COLORES MODO CLARO
 * Define cómo se ven los colores uniLocal en modo claro (Light Mode)
 * 
 * lightColorScheme() es una función de Material 3 que crea un esquema
 * de colores optimizado para fondos claros y buena legibilidad.
 */
private val UniLocalLightColorScheme = lightColorScheme(
    // === COLORES PRINCIPALES ===
    primary = UniLocalPrimary,               // Color principal (#87CEEB) - Botones, enlaces, elementos destacados
    onPrimary = UniLocalTextPrimary,         // Texto sobre el color principal - Debe contrastar bien
    primaryContainer = UniLocalBackground,   // Contenedores con color principal suave
    onPrimaryContainer = UniLocalTextPrimary, // Texto sobre contenedores principales
    
    // === COLORES SECUNDARIOS ===
    secondary = UniLocalPrimary,             // Elementos secundarios - Usamos el mismo azul para coherencia
    onSecondary = UniLocalTextPrimary,       // Texto sobre elementos secundarios
    secondaryContainer = UniLocalBackground, // Contenedores secundarios con fondo suave
    onSecondaryContainer = UniLocalTextSecondary, // Texto menos prominente en contenedores secundarios
    
    // === COLORES TERCIARIOS ===
    tertiary = UniLocalButton,               // Color terciario (#0D1B2A) - Para acentos y botones de acción
    onTertiary = UniLocalBackground,         // Texto sobre color terciario - Fondo claro sobre botón oscuro
    
    // === COLORES DE FONDO Y SUPERFICIE ===
    background = UniLocalBackground,         // Fondo principal de la app (#E6F0FA)
    onBackground = UniLocalTextPrimary,      // Texto sobre el fondo principal
    
    surface = UniLocalBackground,            // Superficie de cards, dialogs, etc.
    onSurface = UniLocalTextPrimary,         // Texto sobre superficies
    surfaceVariant = UniLocalPrimary,        // Variante de superficie para elementos especiales
    onSurfaceVariant = UniLocalTextPrimary,  // Texto sobre variantes de superficie
    
    // === COLORES DE ERROR ===
    error = Color(0xFFDC3545),              // Rojo para errores - Color estándar de Bootstrap
    onError = Color(0xFFFFFFFF),            // Texto blanco sobre error rojo
    
    // === COLORES DE CONTORNO ===
    outline = UniLocalTextSecondary,         // Bordes de campos de texto, divisores
    outlineVariant = UniLocalPrimary         // Bordes destacados, focus states
)

/**
 * ESQUEMA DE COLORES MODO OSCURO
 * Define cómo se ven los colores uniLocal en modo oscuro (Dark Mode)
 * 
 * darkColorScheme() invierte automáticamente las relaciones de contraste
 * para mantener legibilidad en fondos oscuros.
 */
private val UniLocalDarkColorScheme = darkColorScheme(
    // === COLORES PRINCIPALES EN MODO OSCURO ===
    primary = UniLocalPrimary,               // Mantenemos el azul principal
    onPrimary = UniLocalTextPrimary,         // Texto negro sobre azul (buen contraste)
    primaryContainer = UniLocalButton,       // Contenedor oscuro en modo oscuro
    onPrimaryContainer = UniLocalBackground, // Texto claro sobre contenedor oscuro
    
    // === COLORES SECUNDARIOS EN MODO OSCURO ===
    secondary = UniLocalPrimary,             // Consistencia con el azul principal
    onSecondary = UniLocalTextPrimary,       // Texto que contrasta bien
    
    // === FONDOS Y SUPERFICIES EN MODO OSCURO ===
    background = UniLocalButton,             // Fondo oscuro (#0D1B2A)
    onBackground = UniLocalBackground,       // Texto claro sobre fondo oscuro
    
    surface = UniLocalButton,                // Superficies oscuras
    onSurface = UniLocalBackground,          // Texto claro sobre superficies oscuras
    
    // === COLORES DE ERROR (iguales en ambos modos) ===
    error = Color(0xFFDC3545),              // Rojo estándar para errores
    onError = Color(0xFFFFFFFF)             // Texto blanco sobre error
)

/**
 * FUNCIÓN PRINCIPAL DEL TEMA uniLocal
 * 
 * Esta función @Composable aplica el tema uniLocal a toda la aplicación.
 * Es el punto de entrada del sistema de diseño y debe envolver todo el contenido.
 * 
 * @param darkTheme: Boolean - Si es true, usa modo oscuro. Por defecto detecta el tema del sistema
 * @param dynamicColor: Boolean - Si es true, usa colores dinámicos de Android 12+. Lo deshabilitamos para mantener consistencia
 * @param content: @Composable () -> Unit - El contenido de la app que será envuelto por este tema
 */
@Composable
fun LugaresAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // isSystemInDarkTheme() detecta automáticamente si el usuario usa modo oscuro
    dynamicColor: Boolean = false, // Deshabilitado para usar colores uniLocal consistentes (no los colores dinámicos del sistema)
    content: @Composable () -> Unit // Función lambda que contiene toda la UI de la app
) {
    /**
     * LÓGICA DE SELECCIÓN DE ESQUEMA DE COLORES
     * 
     * when es como un switch en otros lenguajes, evalúa condiciones en orden:
     * 1. Si dynamicColor está habilitado Y el dispositivo soporta colores dinámicos (Android 12+)
     * 2. Si darkTheme es true, usa esquema oscuro
     * 3. En cualquier otro caso, usa esquema claro
     */
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current // Obtiene el contexto de Android para colores dinámicos
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> UniLocalDarkColorScheme   // Usa nuestro esquema oscuro personalizado
        else -> UniLocalLightColorScheme       // Usa nuestro esquema claro personalizado (por defecto)
    }

    /**
     * APLICACIÓN DEL TEMA MATERIAL 3
     * 
     * MaterialTheme es el composable que aplica el sistema de diseño Material 3
     * a todo su contenido hijo. Proporciona:
     * - colorScheme: Los colores que definimos arriba
     * - typography: Tipografía Material 3 (definida en Typography.kt)
     * - content: Todo el contenido de la aplicación
     */
    MaterialTheme(
        colorScheme = colorScheme,  // Aplica los colores uniLocal
        typography = Typography,    // Aplica la tipografía Material 3
        content = content          // Renderiza todo el contenido de la app con este tema
    )
}