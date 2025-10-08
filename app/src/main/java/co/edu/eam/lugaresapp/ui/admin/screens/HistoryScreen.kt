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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.model.ModerationRecord
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * PANTALLA DE HISTORIAL DE MODERACIÓN
 * 
 * Muestra el historial completo de acciones de moderación (aprobar/rechazar).
 * 
 * @param placesViewModel ViewModel de lugares (compartido)
 */
@Composable
fun HistoryScreen(
    placesViewModel: PlacesViewModel
) {
    val moderationRecords by placesViewModel.moderationRecords.collectAsState()
    val places by placesViewModel.places.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de Moderación",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (moderationRecords.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay registros de moderación",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(moderationRecords.sortedByDescending { it.timestamp }) { record ->
                    ModerationRecordCard(
                        record = record,
                        placeName = places.find { it.id == record.placeId }?.title ?: "Lugar desconocido"
                    )
                }
            }
        }
    }
}

@Composable
fun ModerationRecordCard(
    record: ModerationRecord,
    placeName: String
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(record.timestamp))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (record.action == "approved") {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon
            Icon(
                imageVector = if (record.action == "approved") {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Filled.Cancel
                },
                contentDescription = record.action,
                tint = if (record.action == "approved") {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier.size(32.dp)
            )
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Action
                Text(
                    text = if (record.action == "approved") "APROBADO" else "RECHAZADO",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (record.action == "approved") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Place Name
                Text(
                    text = "Lugar: $placeName",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                // Place ID
                Text(
                    text = "ID: ${record.placeId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Moderator
                Text(
                    text = "Moderador: ${record.moderatorId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Timestamp
                Text(
                    text = "Fecha: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Reason (if rejected)
                if (record.action == "rejected" && record.reason != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Razón: ${record.reason}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
