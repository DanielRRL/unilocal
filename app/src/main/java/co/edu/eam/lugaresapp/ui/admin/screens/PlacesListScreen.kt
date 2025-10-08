package co.edu.eam.lugaresapp.ui.admin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

@Composable
fun PlacesListScreen() {
    val placesViewModel: PlacesViewModel = viewModel()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val pendingPlaces by placesViewModel.places.collectAsState()
    val moderatorId = sessionManager.getUserId()
    
    // Dialog state for rejection reason
    var showRejectDialog by remember { mutableStateOf(false) }
    var selectedPlaceForRejection by remember { mutableStateOf<Place?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lugares Pendientes de Aprobación",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        val pendingList = placesViewModel.getPendingPlaces()
        
        if (pendingList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay lugares pendientes de aprobación",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendingList) { place ->
                    PendingPlaceCard(
                        place = place,
                        onApprove = {
                            moderatorId?.let { modId ->
                                placesViewModel.approvePlace(place.id, modId)
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image
            AsyncImage(
                model = place.images.firstOrNull() ?: "https://via.placeholder.com/150",
                contentDescription = "Imagen de ${place.title}",
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            
            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tipo: ${place.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
                Text("Autorizar")
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
            Text("Rechazar lugar")
        },
        text = {
            Column {
                Text(
                    text = "¿Estás seguro de rechazar '${place.title}'?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Razón (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
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