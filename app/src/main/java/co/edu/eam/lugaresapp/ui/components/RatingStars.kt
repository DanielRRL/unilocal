package co.edu.eam.lugaresapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * COMPONENTE DE ESTRELLAS DE CALIFICACIÓN
 * 
 * Componente reutilizable para mostrar y/o seleccionar calificaciones con estrellas.
 * Sigue el principio de Responsabilidad Única (SOLID).
 * 
 * MODOS DE USO:
 * 1. SOLO LECTURA (isInteractive = false):
 *    - Muestra estrellas rellenas según el rating
 *    - Útil para mostrar calificaciones existentes
 * 
 * 2. INTERACTIVO (isInteractive = true):
 *    - Permite al usuario seleccionar una calificación
 *    - Callback onRatingChange se ejecuta cuando cambia
 * 
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: Solo maneja la visualización/selección de rating
 * - Open/Closed: Configurable mediante parámetros
 * - Interface Segregation: Callbacks opcionales según el modo
 * 
 * @param rating Calificación actual (1-5)
 * @param maxStars Número máximo de estrellas a mostrar (default 5)
 * @param isInteractive Si permite selección por parte del usuario
 * @param starSize Tamaño de cada estrella en Dp
 * @param starColor Color de las estrellas rellenas
 * @param emptyStarColor Color de las estrellas vacías
 * @param onRatingChange Callback que se ejecuta cuando el usuario selecciona una nueva calificación
 * @param modifier Modificador para personalización del layout
 * 
 * EJEMPLOS DE USO:
 * ```kotlin
 * // Modo solo lectura - Mostrar calificación promedio
 * RatingStars(
 *     rating = 4,
 *     isInteractive = false
 * )
 * 
 * // Modo interactivo - Formulario de reseña
 * var userRating by remember { mutableStateOf(5) }
 * RatingStars(
 *     rating = userRating,
 *     isInteractive = true,
 *     onRatingChange = { newRating -> userRating = newRating }
 * )
 * ```
 */
@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    isInteractive: Boolean = false,
    starSize: Dp = 24.dp,
    starColor: Color = Color(0xFFFFC107), // Amarillo dorado
    emptyStarColor: Color = Color.Gray,
    onRatingChange: (Int) -> Unit = {}
) {
    /**
     * Validación de parámetros
     * Asegura que el rating esté dentro del rango válido
     */
    val validRating = rating.coerceIn(0, maxStars)
    
    /**
     * Fila horizontal de estrellas
     */
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /**
         * Generar estrellas dinámicamente
         * 1 hasta maxStars (generalmente 5)
         */
        for (i in 1..maxStars) {
            /**
             * Determinar si esta estrella debe estar rellena
             * Rellena si el índice es menor o igual al rating
             */
            val isFilled = i <= validRating
            
            /**
             * Componente de estrella individual
             */
            if (isInteractive) {
                /**
                 * MODO INTERACTIVO
                 * Botón clickeable para seleccionar calificación
                 */
                IconButton(
                    onClick = { onRatingChange(i) },
                    modifier = Modifier.size(starSize)
                ) {
                    Icon(
                        imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Estrella $i",
                        tint = if (isFilled) starColor else emptyStarColor,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                /**
                 * MODO SOLO LECTURA
                 * Icono estático sin interacción
                 */
                Icon(
                    imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Estrella $i",
                    tint = if (isFilled) starColor else emptyStarColor,
                    modifier = Modifier.size(starSize)
                )
            }
        }
    }
}

/**
 * VARIANTE: CALIFICACIÓN CON VALOR NUMÉRICO
 * 
 * Extensión del componente RatingStars que incluye el valor numérico al lado.
 * Útil para mostrar promedios de calificación con decimales.
 * 
 * @param rating Calificación numérica (puede tener decimales)
 * @param reviewCount Número de reseñas (opcional)
 * @param modifier Modificador para personalización
 * 
 * EJEMPLO DE USO:
 * ```kotlin
 * RatingStarsWithValue(
 *     rating = 4.3,
 *     reviewCount = 127
 * )
 * // Muestra: ★★★★☆ 4.3 (127 reseñas)
 * ```
 */
@Composable
fun RatingStarsWithValue(
    rating: Double,
    modifier: Modifier = Modifier,
    reviewCount: Int? = null,
    starSize: Dp = 20.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        /**
         * Estrellas visuales
         * Redondea el rating para mostrar estrellas completas
         */
        RatingStars(
            rating = rating.toInt(),
            isInteractive = false,
            starSize = starSize
        )
        
        /**
         * Valor numérico
         * Formateado con un decimal
         */
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        /**
         * Contador de reseñas (opcional)
         * Solo se muestra si se proporciona
         */
        if (reviewCount != null) {
            Text(
                text = "($reviewCount ${if (reviewCount == 1) "reseña" else "reseñas"})",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
