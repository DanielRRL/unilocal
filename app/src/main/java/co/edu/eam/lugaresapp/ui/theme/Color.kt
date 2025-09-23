package co.edu.eam.lugaresapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * DEFINICIÓN DE COLORES uniLocal
 * Este archivo contiene todos los colores utilizados en la aplicación uniLocal.
 * Los colores están basados en el mockup azul proporcionado por el cliente.
 * 
 * PATRÓN: Se usa Color(0xFFHEXCODE) donde:
 * - 0xFF indica opacidad completa 
 * - Los siguientes 6 dígitos son el código hexadecimal del color
 */

// === COLORES PRINCIPALES uniLocal  ===
val UniLocalPrimary = Color(0xFF87CEEB)      // #87CEEB - Azul cielo: Color principal para botones, enlaces y elementos destacados
val UniLocalBackground = Color(0xFFE6F0FA)   // #b8ddffff - Azul claro: Fondo de pantallas y contenedores principales  
val UniLocalButton = Color(0xFF0D1B2A)       // #0D1B2A - Azul marino/negro: Botones de acción principal y texto sobre fondos claros
val UniLocalTextPrimary = Color(0xFF000000)  // #000000 - Negro: Texto principal, títulos y contenido importante
val UniLocalTextSecondary = Color(0xFF333333) // #333333 - Gris oscuro: Texto secundario, subtítulos y elementos menos prominentes

// === COLORES DE COMPATIBILIDAD ===
val UniLocalBlueLight = UniLocalBackground  // Alias para compatibilidad con código anterior
val UniLocalBlue = UniLocalPrimary          // Alias para compatibilidad con código anterior  
val UniLocalDark = UniLocalButton           // Alias para compatibilidad con código anterior

// === COLORES MATERIAL DESIGN  ===
// Estos colores son necesarios para el correcto funcionamiento del tema Material 3
// Aunque no se usen directamente, el sistema los requiere para fallbacks y compatibilidad
val Purple80 = Color(0xFFD0BCFF)    // Violeta claro - Usado como fallback en modo oscuro
val PurpleGrey80 = Color(0xFFCCC2DC) // Violeta gris claro - Elementos secundarios en modo oscuro
val Pink80 = Color(0xFFEFB8C8)      // Rosa claro - Acentos en modo oscuro

val Purple40 = Color(0xFF6650a4)    // Violeta oscuro - Usado como fallback en modo claro
val PurpleGrey40 = Color(0xFF625b71) // Violeta gris oscuro - Elementos secundarios en modo claro  
val Pink40 = Color(0xFF7D5260)      // Rosa oscuro - Acentos en modo claro