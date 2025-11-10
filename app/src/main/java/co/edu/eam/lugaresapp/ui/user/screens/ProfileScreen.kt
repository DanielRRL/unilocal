package co.edu.eam.lugaresapp.ui.user.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import co.edu.eam.lugaresapp.ui.components.RatingStars
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.RewiewsViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * PANTALLA DE PERFIL COMPLETA
 * 
 * Muestra información detallada del usuario actual:
 * - Avatar y datos personales
 * - Estadísticas (lugares creados, reseñas, favoritos)
 * - Lugares creados por el usuario
 * - Lugares favoritos
 * - Reseñas realizadas
 * - Botón para cerrar sesión
 * 
 * @param usersViewModel ViewModel de usuarios
 * @param placesViewModel ViewModel de lugares
 * @param reviewsViewModel ViewModel de reseñas
 * @param onNavigateToPlaceDetail Callback para navegar a detalle de lugar
 * @param onLogout Callback para cerrar sesión
 */
@Composable
fun ProfileScreen(
    usersViewModel: UsersViewModel,
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
    onNavigateToPlaceDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    if (currentUserId == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay sesión activa")
        }
        return
    }
    
    val user = usersViewModel.findById(currentUserId)
    val userPlaces = placesViewModel.getPlacesByOwner(currentUserId)
    val userReviews = reviewsViewModel.findByUserId(currentUserId)
    val userFavorites = usersViewModel.getFavorites(currentUserId)
    
    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Usuario no encontrado")
        }
        return
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera con avatar y datos
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Avatar de ${user.name}",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Nombre
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Email
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Ciudad
                    Text(
                        text = user.city,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        // Estadísticas
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Estadísticas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            icon = Icons.Filled.Place,
                            value = userPlaces.size.toString(),
                            label = "Lugares"
                        )
                        StatItem(
                            icon = Icons.Filled.Star,
                            value = userReviews.size.toString(),
                            label = "Reseñas"
                        )
                        StatItem(
                            icon = Icons.Filled.Favorite,
                            value = userFavorites.size.toString(),
                            label = "Favoritos"
                        )
                    }
                }
            }
        }
        
        // Sección: Mis Lugares
        if (userPlaces.isNotEmpty()) {
            item {
                Text(
                    text = "Mis Lugares (${userPlaces.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(userPlaces) { place ->
                PlaceListItem(
                    title = place.title,
                    description = place.description,
                    imageUrl = place.images.firstOrNull() ?: "",
                    approved = place.approved,
                    onClick = { onNavigateToPlaceDetail(place.id) }
                )
            }
        }
        
        // Sección: Mis Reseñas
        if (userReviews.isNotEmpty()) {
            item {
                Text(
                    text = "Mis Reseñas (${userReviews.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(userReviews) { review ->
                val place = placesViewModel.findById(review.placeID)
                if (place != null) {
                    ReviewListItem(
                        placeName = place.title,
                        rating = review.rating,
                        comment = review.comment,
                        date = review.date.toLocalDate().toString(),
                        hasResponse = review.ownerResponse != null,
                        onClick = { onNavigateToPlaceDetail(place.id) }
                    )
                }
            }
        }
        
        // Botón de cerrar sesión
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    
    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        sessionManager.clear()
                        showLogoutDialog = false
                        onLogout()
                        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PlaceListItem(
    title: String,
    description: String,
    imageUrl: String,
    approved: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (approved) "✓ Aprobado" else "⏳ Pendiente",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (approved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ReviewListItem(
    placeName: String,
    rating: Int,
    comment: String,
    date: String,
    hasResponse: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = placeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Rating usando componente reutilizable
            RatingStars(
                rating = rating,
                isInteractive = false,
                starSize = 16.dp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = comment,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            
            if (hasResponse) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💬 Con respuesta del propietario",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}