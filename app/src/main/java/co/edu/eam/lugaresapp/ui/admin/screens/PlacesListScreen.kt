package co.edu.eam.lugaresapp.ui.admin.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel
import co.edu.eam.lugaresapp.utils.RequestResult
import java.text.SimpleDateFormat
import java.util.*

/**
 * PANTALLA DE LISTA DE LUGARES PENDIENTES
 * 
 * Permite a los administradores ver y moderar lugares pendientes de aprobación.
 * Conectada 100% con Firebase - sin datos quemados.
 * 
 * @param placesViewModel ViewModel de lugares (compartido)
 * @param usersViewModel ViewModel de usuarios (compartido)
 */
@Composable
fun PlacesListScreen(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val places by placesViewModel.places.collectAsState()
    val reportResult by placesViewModel.reportResult.collectAsState()
    val moderatorId = sessionManager.getUserId()
    
    // Dialog state for rejection reason
    var showRejectDialog by remember { mutableStateOf(false) }
    var selectedPlaceForRejection by remember { mutableStateOf<Place?>(null) }
    
    // Observar el resultado de las operaciones
    LaunchedEffect(reportResult) {
        when (reportResult) {
            is RequestResult.Success -> {
                Toast.makeText(
                    context,
                    (reportResult as RequestResult.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                placesViewModel.resetOperationResult()
            }
            is RequestResult.Failure -> {
                Toast.makeText(
                    context,
                    "Error: ${(reportResult as RequestResult.Failure).errorMessage}",
                    Toast.LENGTH_LONG
                ).show()
                placesViewModel.resetOperationResult()
            }
            else -> {}
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con contador
        val pendingList = placesViewModel.getPendingPlaces()
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Solicitudes Pendientes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${pendingList.size} lugar(es) esperando revisión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (pendingList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "✓ No hay solicitudes pendientes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Todas las solicitudes han sido revisadas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = pendingList,
                    key = { it.id }
                ) { place ->
                    PendingPlaceCard(
                        place = place,
                        onApprove = {
                            moderatorId?.let { modId ->
                                placesViewModel.approvePlace(place.id, modId)
                            } ?: run {
                                Toast.makeText(context, "Error: No se pudo obtener el ID del moderador", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onReject = {
                            selectedPlaceForRejection = place
                            showRejectDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Reject Dialog
    if (showRejectDialog && selectedPlaceForRejection != null) {
        RejectDialog(
            place = selectedPlaceForRejection!!,
            onConfirm = { reason ->
                moderatorId?.let { modId ->
                    placesViewModel.rejectPlace(
                        selectedPlaceForRejection!!.id,
                        modId,
                        reason.ifBlank { null }
                    )
                } ?: run {
                    Toast.makeText(context, "Error: No se pudo obtener el ID del moderador", Toast.LENGTH_SHORT).show()
                }
                showRejectDialog = false
                selectedPlaceForRejection = null
            },
            onDismiss = {
                showRejectDialog = false
                selectedPlaceForRejection = null
            }
        )
    }
}

@Composable
fun PendingPlaceCard(
    place: Place,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(place.createdAt))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image
                AsyncImage(
                    model = place.images.firstOrNull() ?: "https://via.placeholder.com/150",
                    contentDescription = "Imagen de ${place.title}",
                    modifier = Modifier
                        .size(100.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = place.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = place.type.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Location
                    Text(
                        text = "📍 ${place.address}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Owner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "ID: ${place.ownerId?.take(8) ?: "Desconocido"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Approve Button
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Autorizar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Aprobar")
                }
                
                // Reject Button
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "Rechazar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rechazar")
                }
            }
        }
    }
}

@Composable
fun RejectDialog(
    place: Place,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Rechazar Solicitud")
        },
        text = {
            Column {
                Text(
                    text = "¿Estás seguro de rechazar '${place.title}'?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esta acción quedará registrada en el historial de moderación.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Razón del rechazo (opcional)") },
                    placeholder = { Text("Ej: Información incompleta, duplicado, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(reason) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Rechazar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}