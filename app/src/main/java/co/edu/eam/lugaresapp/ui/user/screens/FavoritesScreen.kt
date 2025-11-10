package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.ui.components.RatingStarsWithValue
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.RewiewsViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE FAVORITOS
 * 
 * Muestra todos los lugares que el usuario ha marcado como favoritos.
 * 
 * CARACTERÍSTICAS según diseño (imagen 1):
 * - Título "Favoritos" en la parte superior
 * - Lista de lugares favoritos con:
 *   * Imagen del lugar
 *   * Nombre del lugar
 *   * Calificación con estrellas
 *   * Click para navegar al detalle
 * - Mensaje si no hay favoritos
 * 
 * @param usersViewModel ViewModel de usuarios
 * @param placesViewModel ViewModel de lugares
 * @param reviewsViewModel ViewModel de reseñas
 * @param onNavigateToPlaceDetail Callback para navegar al detalle del lugar
 */
@Composable
fun FavoritesScreen(
    usersViewModel: UsersViewModel,
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
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
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Sin favoritos",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Inicia sesión para ver tus favoritos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }
    
    /**
     * Obtener IDs de favoritos y lugares completos
     */
    val favoriteIds = usersViewModel.getFavorites(currentUserId)
    val favoritePlaces = favoriteIds.mapNotNull { placeId ->
        placesViewModel.findById(placeId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /**
         * TÍTULO
         */
        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        /**
         * LISTA DE FAVORITOS
         */
        if (favoritePlaces.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Sin favoritos",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No tienes lugares favoritos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Explora y guarda tus lugares preferidos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoritePlaces) { place ->
                    FavoriteCard(
                        place = place,
                        reviewsViewModel = reviewsViewModel,
                        onClick = { onNavigateToPlaceDetail(place.id) }
                    )
                }
            }
        }
    }
}

/**
 * TARJETA DE LUGAR FAVORITO
 * 
 * Muestra información resumida de un lugar favorito.
 * 
 * @param place Lugar a mostrar
 * @param reviewsViewModel ViewModel para obtener calificaciones
 * @param onClick Callback al hacer click en la tarjeta
 */
@Composable
fun FavoriteCard(
    place: co.edu.eam.lugaresapp.model.Place,
    reviewsViewModel: RewiewsViewModel,
    onClick: () -> Unit
) {
    val averageRating = reviewsViewModel.getAverageRating(place.id)
    val reviewCount = reviewsViewModel.getReviewCount(place.id)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            /**
             * IMAGEN DE FONDO
             */
            AsyncImage(
                model = place.images.firstOrNull() ?: "",
                contentDescription = place.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            /**
             * OVERLAY CON INFORMACIÓN
             */
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    /**
                     * NOMBRE DEL LUGAR
                     */
                    Text(
                        text = place.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    
                    /**
                     * CALIFICACIÓN
                     */
                    if (reviewCount > 0) {
                        RatingStarsWithValue(
                            rating = averageRating,
                            reviewCount = reviewCount,
                            starSize = 20.dp
                        )
                    } else {
                        Text(
                            text = "Sin calificaciones",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
