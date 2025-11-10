package co.edu.eam.lugaresapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.model.Place

/**
 * COMPONENTE DE TARJETA DE LUGAR
 * 
 * Componente reutilizable para mostrar información resumida de un lugar.
 * Sigue principios SOLID para ser flexible y mantenible.
 * 
 * CARACTERÍSTICAS:
 * - Imagen del lugar con carga asíncrona
 * - Nombre y descripción truncada
 * - Tipo de lugar en chip
 * - Rating opcional (si se proporciona)
 * - Estado de aprobación (para admin)
 * - Click handler para navegación
 * 
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: Solo presenta información de un lugar
 * - Open/Closed: Extensible mediante parámetros opcionales
 * - Dependency Inversion: Recibe datos (Place), no conoce el origen
 * 
 * @param place Objeto Place con toda la información
 * @param modifier Modificador para personalización del layout
 * @param showApprovalStatus Mostrar indicador de estado de aprobación (para admin)
 * @param rating Calificación promedio opcional (null si no se quiere mostrar)
 * @param reviewCount Número de reseñas opcional
 * @param onClick Callback cuando se hace click en la tarjeta
 * 
 * EJEMPLO DE USO:
 * ```kotlin
 * // Vista de usuario - con rating
 * PlaceCard(
 *     place = place,
 *     rating = 4.5,
 *     reviewCount = 32,
 *     onClick = { navigateToDetail(place.id) }
 * )
 * 
 * // Vista de admin - con estado de aprobación
 * PlaceCard(
 *     place = place,
 *     showApprovalStatus = true,
 *     onClick = { navigateToDetail(place.id) }
 * )
 * ```
 */
@Composable
fun PlaceCard(
    place: Place,
    modifier: Modifier = Modifier,
    showApprovalStatus: Boolean = false,
    rating: Double? = null,
    reviewCount: Int? = null,
    onClick: () -> Unit = {}
) {
    /**
     * Card principal con elevación y bordes redondeados
     */
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        /**
         * Columna vertical para organizar contenido
         */
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            /**
             * IMAGEN DEL LUGAR
             * Carga asíncrona con Coil, tamaño fijo para consistencia
             */
            AsyncImage(
                model = place.images.firstOrNull() ?: "https://via.placeholder.com/400x200?text=Sin+Imagen",
                contentDescription = "Imagen de ${place.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            
            /**
             * CONTENIDO DE TEXTO
             * Padding uniforme para todos los elementos
             */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                /**
                 * FILA SUPERIOR: Título y estado de aprobación
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /**
                     * TÍTULO DEL LUGAR
                     * Máximo 2 líneas con ellipsis
                     */
                    Text(
                        text = place.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    /**
                     * INDICADOR DE APROBACIÓN (solo admin)
                     * Chip pequeño con color según estado
                     */
                    if (showApprovalStatus) {
                        Surface(
                            color = if (place.approved) 
                                Color(0xFF4CAF50).copy(alpha = 0.2f) 
                            else 
                                Color(0xFFFF9800).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (place.approved) "✓ Aprobado" else "⏳ Pendiente",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (place.approved) Color(0xFF4CAF50) else Color(0xFFFF9800),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                /**
                 * CHIP DE CATEGORÍA
                 * Muestra el tipo de lugar
                 */
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = place.type.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                /**
                 * DESCRIPCIÓN
                 * Truncada a 2 líneas para mantener altura consistente
                 */
                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                /**
                 * DIRECCIÓN
                 * Icono de ubicación + texto truncado
                 */
                Text(
                    text = "📍 ${place.address}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                /**
                 * RATING (opcional)
                 * Solo se muestra si se proporciona
                 */
                if (rating != null) {
                    RatingStarsWithValue(
                        rating = rating,
                        reviewCount = reviewCount,
                        starSize = 16.dp
                    )
                }
            }
        }
    }
}

/**
 * VARIANTE: TARJETA COMPACTA DE LUGAR
 * 
 * Versión más pequeña para listas densas o vistas previas.
 * 
 * DIFERENCIAS CON PlaceCard:
 * - Imagen más pequeña (80dp vs 160dp)
 * - Layout horizontal en lugar de vertical
 * - Menos información mostrada
 * 
 * @param place Objeto Place con la información
 * @param modifier Modificador para personalización
 * @param distanceText Texto opcional de distancia (ej: "2.3 km")
 * @param onClick Callback cuando se hace click
 * 
 * EJEMPLO DE USO:
 * ```kotlin
 * PlaceCompactCard(
 *     place = place,
 *     distanceText = "2.3 km",
 *     onClick = { navigateToDetail(place.id) }
 * )
 * ```
 */
@Composable
fun PlaceCompactCard(
    place: Place,
    modifier: Modifier = Modifier,
    distanceText: String? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        /**
         * ROW HORIZONTAL: Imagen a la izquierda, contenido a la derecha
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            /**
             * IMAGEN CUADRADA PEQUEÑA
             */
            AsyncImage(
                model = place.images.firstOrNull() ?: "https://via.placeholder.com/80x80?text=Sin+Imagen",
                contentDescription = "Imagen de ${place.title}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            /**
             * CONTENIDO DE TEXTO
             */
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                /**
                 * TÍTULO
                 */
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                /**
                 * CATEGORÍA
                 */
                Text(
                    text = place.type.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                /**
                 * DIRECCIÓN O DISTANCIA
                 */
                Text(
                    text = if (distanceText != null) {
                        "📍 $distanceText"
                    } else {
                        "📍 ${place.address}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
