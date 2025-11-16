package co.edu.eam.lugaresapp.ui.user.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
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
import co.edu.eam.lugaresapp.model.Review
import co.edu.eam.lugaresapp.ui.components.RatingStars
import co.edu.eam.lugaresapp.ui.components.RatingStarsWithValue
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.RewiewsViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

/**
 * PANTALLA DE DETALLE DE LUGAR
 * 
 * Muestra información completa de un lugar específico.
 * 
 * CARACTERÍSTICAS:
 * - Galería de imágenes con scroll horizontal
 * - Información detallada del lugar
 * - Botón de favorito con toggle
 * - Botón de eliminar para propietario
 * - Estado de horario en tiempo real (Abierto/Cerrado)
 * - Lista de reseñas con respuestas de propietarios
 * - Formulario para agregar reseñas
 * - Formulario para responder reseñas (solo propietario)
 * - Validación de sesión para acciones
 * 
 * @param placesViewModel ViewModel de lugares
 * @param reviewsViewModel ViewModel de reseñas
 * @param usersViewModel ViewModel de usuarios
 * @param padding Padding de la navegación
 * @param id ID del lugar a mostrar
 * @param onNavigateBack Callback para volver atrás después de eliminar
 */
@Composable
fun PlaceDetailScreen(
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
    usersViewModel: UsersViewModel,
    padding: PaddingValues,
    id: String,
    onNavigateBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()
    
    val place = placesViewModel.findById(id)
    
    // Estados del formulario de review
    var reviewRating by remember { mutableStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }
    
    // Estado para responder a una reseña
    var selectedReviewForResponse by remember { mutableStateOf<Review?>(null) }
    var responseText by remember { mutableStateOf("") }
    
    // Estado para diálogo de eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Obtener favoritos del usuario
    val favorites = currentUserId?.let { 
        usersViewModel.getFavorites(it) 
    } ?: emptyList()
    val isFavorite = favorites.contains(id)
    
    // Obtener reseñas del lugar
    val placeReviews = reviewsViewModel.findByPlaceId(id)
    val averageRating = reviewsViewModel.getAverageRating(id)
    val reviewCount = reviewsViewModel.getReviewCount(id)
    
    // Verificar si el usuario actual es el propietario
    val isOwner = currentUserId != null && place?.ownerId == currentUserId

    if (place == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Lugar no encontrado")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // ==================== GALERÍA DE IMÁGENES ====================
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(place.images) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen de ${place.title}",
                    modifier = Modifier
                        .width(300.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // ==================== INFORMACIÓN PRINCIPAL ====================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título y botón de favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de favorito
                    IconButton(
                        onClick = {
                            if (currentUserId != null) {
                                usersViewModel.toggleFavorite(currentUserId, id)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Debes iniciar sesión para guardar favoritos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Botón de eliminar (solo para el propietario)
                    if (isOwner) {
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar lugar",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rating promedio - Usando componente RatingStarsWithValue
            if (reviewCount > 0) {
                RatingStarsWithValue(
                    rating = averageRating,
                    reviewCount = reviewCount,
                    starSize = 24.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Tipo de lugar
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = place.type.displayName,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado: Abierto/Cerrado
            val scheduleStatus = calculateScheduleStatus(place.schedules)
            Surface(
                color = when (scheduleStatus) {
                    "Abierto" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    "Cerrado" -> Color(0xFFF44336).copy(alpha = 0.1f)
                    else -> Color.Gray.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = scheduleStatus,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = when (scheduleStatus) {
                        "Abierto" -> Color(0xFF4CAF50)
                        "Cerrado" -> Color(0xFFF44336)
                        else -> Color.Gray
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dirección
            Text(
                text = "Dirección",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.address,
                style = MaterialTheme.typography.bodyLarge
            )

            // Teléfonos
            if (place.phones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Teléfonos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                place.phones.forEach { phone ->
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Horarios
            if (place.schedules.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Horarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                place.schedules.forEach { schedule ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = schedule.day,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${schedule.open} - ${schedule.close}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()

            // ==================== SECCIÓN DE RESEÑAS ====================
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Reseñas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Formulario para agregar reseña
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Agregar Reseña",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de estrellas - Usando componente RatingStars
                    Text(
                        text = "Calificación",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingStars(
                        rating = reviewRating,
                        isInteractive = true,
                        starSize = 32.dp,
                        onRatingChange = { newRating -> reviewRating = newRating }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo de comentario
                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón enviar
                    Button(
                        onClick = {
                            if (currentUserId == null) {
                                Toast.makeText(
                                    context,
                                    "Debes iniciar sesión para agregar una reseña",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }

                            if (reviewComment.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "El comentario no puede estar vacío",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val newReview = Review(
                                id = UUID.randomUUID().toString(),
                                userID = currentUserId,
                                placeID = id,
                                rating = reviewRating,
                                comment = reviewComment.trim(),
                                date = LocalDateTime.now(),
                                ownerResponse = null
                            )

                            reviewsViewModel.addReview(newReview)

                            Toast.makeText(
                                context,
                                "Reseña agregada exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Limpiar formulario
                            reviewComment = ""
                            reviewRating = 5
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Publicar Reseña")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de reseñas
            if (placeReviews.isEmpty()) {
                Text(
                    text = "No hay reseñas aún. ¡Sé el primero en dejar una!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            } else {
                placeReviews.forEach { review ->
                    ReviewCard(
                        review = review,
                        usersViewModel = usersViewModel,
                        isOwner = isOwner,
                        onRespondClick = {
                            selectedReviewForResponse = review
                            responseText = ""
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    
    // ==================== DIÁLOGOS ====================
    
    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Lugar") },
            text = { 
                Text("¿Estás seguro que deseas eliminar este lugar? Esta acción no se puede deshacer.") 
            },
            confirmButton = {
                Button(
                    onClick = {
                        placesViewModel.deletePlace(id)
                        showDeleteDialog = false
                        Toast.makeText(
                            context,
                            "Lugar eliminado exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateBack?.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo para responder a una reseña
    if (selectedReviewForResponse != null) {
        AlertDialog(
            onDismissRequest = { 
                selectedReviewForResponse = null
                responseText = ""
            },
            title = { Text("Responder Reseña") },
            text = {
                Column {
                    Text(
                        text = "Comentario: \"${selectedReviewForResponse?.comment}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = responseText,
                        onValueChange = { responseText = it },
                        label = { Text("Tu respuesta") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (responseText.isNotBlank()) {
                            selectedReviewForResponse?.let { review ->
                                reviewsViewModel.addOwnerResponse(review.id, responseText.trim())
                                Toast.makeText(
                                    context,
                                    "Respuesta publicada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            selectedReviewForResponse = null
                            responseText = ""
                        } else {
                            Toast.makeText(
                                context,
                                "La respuesta no puede estar vacía",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Publicar")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    selectedReviewForResponse = null
                    responseText = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * TARJETA DE RESEÑA
 * 
 * Muestra una reseña individual con rating, comentario y respuesta del propietario.
 * Si el usuario es el propietario del lugar y la reseña no tiene respuesta, muestra un botón para responder.
 * 
 * @param review Reseña a mostrar
 * @param usersViewModel ViewModel de usuarios
 * @param isOwner Indica si el usuario actual es el propietario del lugar
 * @param onRespondClick Callback para responder a la reseña
 */
@Composable
fun ReviewCard(
    review: Review,
    usersViewModel: UsersViewModel,
    isOwner: Boolean = false,
    onRespondClick: () -> Unit = {}
) {
    val user = usersViewModel.getUserFromList(review.userID)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Usuario y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user?.name ?: "Usuario desconocido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = review.date.toLocalDate().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rating - Usando componente RatingStars
            RatingStars(
                rating = review.rating,
                isInteractive = false,
                starSize = 20.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Comentario
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyLarge
            )

            // Respuesta del propietario
            review.ownerResponse?.let { response ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Respuesta del propietario",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = response,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // Botón de responder (solo para propietario y si no hay respuesta aún)
            if (isOwner && review.ownerResponse == null) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = onRespondClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Reply,
                        contentDescription = "Responder",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Responder a esta reseña")
                }
            }
        }
    }
}

/**
 * CALCULAR ESTADO DE HORARIO
 * 
 * Determina si el lugar está abierto, cerrado o sin horario definido.
 * 
 * @param schedules Lista de horarios del lugar
 * @return String con el estado: "Abierto", "Cerrado" o "Horario no definido"
 */
fun calculateScheduleStatus(schedules: List<co.edu.eam.lugaresapp.model.Schedule>): String {
    if (schedules.isEmpty()) {
        return "Horario no definido"
    }

    val now = LocalTime.now()
    val today = DayOfWeek.from(LocalDateTime.now())
    val todayName = today.getDisplayName(TextStyle.FULL, Locale("es", "ES"))

    val todaySchedule = schedules.find { 
        it.day.equals(todayName, ignoreCase = true) 
    }

    return if (todaySchedule != null) {
        try {
            val openTime = LocalTime.parse(todaySchedule.open)
            val closeTime = LocalTime.parse(todaySchedule.close)
            if (now.isAfter(openTime) && now.isBefore(closeTime)) {
                "Abierto"
            } else {
                "Cerrado"
            }
        } catch (e: Exception) {
            "Horario inválido"
        }
    } else {
        "Cerrado"
    }
}