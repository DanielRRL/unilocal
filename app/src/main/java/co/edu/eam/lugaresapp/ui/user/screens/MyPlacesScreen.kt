package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

/**
 * PANTALLA DE MIS LUGARES
 * 
 * Muestra todos los lugares creados por el usuario con sus estados:
 * - ✅ APROBADO (verde): Lugar visible para todos los usuarios
 * - ⏳ PENDIENTE DE APROBACIÓN (amarillo/naranja): Esperando revisión de admin
 * - ❌ RECHAZADO (rojo): No cumple con los requisitos
 * 
 * CARACTERÍSTICAS según diseño (imagen 3):
 * - Título "Mis Lugares"
 * - Lista de lugares con:
 *   * Imagen del lugar
 *   * Nombre del lugar
 *   * Badge de estado (Aprobado/Pendiente/Rechazado)
 *   * Click para ver/editar detalle
 * - Mensaje si no ha creado lugares
 * 
 * @param placesViewModel ViewModel de lugares
 * @param onNavigateToPlaceDetail Callback para navegar al detalle
 */
@Composable
fun MyPlacesScreen(
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()
    
    /**
     * Validar sesión activa
     */
    if (currentUserId == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Sin lugares",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Inicia sesión para ver tus lugares",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }
    
    /**
     * Obtener lugares del usuario
     */
    val userPlaces = placesViewModel.getPlacesByOwner(currentUserId)
    
    /**
     * Filtrar por estado
     */
    val approvedPlaces = userPlaces.filter { it.approved }
    val pendingPlaces = userPlaces.filter { !it.approved }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /**
         * TÍTULO
         */
        Text(
            text = "Mis Lugares",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        /**
         * CONTENIDO
         */
        if (userPlaces.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = "Sin lugares",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No has creado lugares aún",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Comparte tus lugares favoritos con la comunidad",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                /**
                 * SECCIÓN: Aprobados
                 */
                if (approvedPlaces.isNotEmpty()) {
                    item {
                        Text(
                            text = "✅ Aprobados (${approvedPlaces.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(approvedPlaces) { place ->
                        MyPlaceCard(
                            place = place,
                            status = PlaceStatus.APPROVED,
                            onClick = { onNavigateToPlaceDetail(place.id) }
                        )
                    }
                }
                
                /**
                 * SECCIÓN: Pendientes de aprobación
                 */
                if (pendingPlaces.isNotEmpty()) {
                    item {
                        Text(
                            text = "⏳ Pendientes de Aprobación (${pendingPlaces.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(pendingPlaces) { place ->
                        MyPlaceCard(
                            place = place,
                            status = PlaceStatus.PENDING,
                            onClick = { onNavigateToPlaceDetail(place.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * ESTADOS DE LUGARES
 */
enum class PlaceStatus {
    APPROVED,
    PENDING,
    REJECTED
}

/**
 * TARJETA DE LUGAR DEL USUARIO
 * 
 * Muestra un lugar creado por el usuario con su estado de aprobación.
 * 
 * @param place Lugar a mostrar
 * @param status Estado del lugar
 * @param onClick Callback al hacer click
 */
@Composable
fun MyPlaceCard(
    place: Place,
    status: PlaceStatus,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            /**
             * IMAGEN
             */
            AsyncImage(
                model = place.images.firstOrNull() ?: "",
                contentDescription = place.title,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                contentScale = ContentScale.Crop
            )
            
            /**
             * INFORMACIÓN
             */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                /**
                 * NOMBRE
                 */
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                
                /**
                 * BADGE DE ESTADO
                 */
                Surface(
                    color = when (status) {
                        PlaceStatus.APPROVED -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        PlaceStatus.PENDING -> Color(0xFFFF9800).copy(alpha = 0.2f)
                        PlaceStatus.REJECTED -> Color(0xFFF44336).copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = when (status) {
                            PlaceStatus.APPROVED -> "APROBADO"
                            PlaceStatus.PENDING -> "PENDIENTE DE APROBACIÓN"
                            PlaceStatus.REJECTED -> "RECHAZADO"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            PlaceStatus.APPROVED -> Color(0xFF4CAF50)
                            PlaceStatus.PENDING -> Color(0xFFFF9800)
                            PlaceStatus.REJECTED -> Color(0xFFF44336)
                        }
                    )
                }
            }
        }
    }
}
