package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

@Composable
fun PlacesScreen(
    padding: PaddingValues,
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit
){
    // Estados de búsqueda y filtros
    var searchText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<PlaceType?>(null) }
    var maxDistance by remember { mutableStateOf(5000f) } // 5km por defecto
    var showFilters by remember { mutableStateOf(false) }
    
    // Aplicar filtros
    val filteredPlaces = remember(searchText, selectedType, maxDistance) {
        placesViewModel.searchWithFilters(
            searchText = searchText,
            selectedType = selectedType,
            userLat = placesViewModel.defaultUserLocation.latitude,
            userLon = placesViewModel.defaultUserLocation.longitude,
            maxDistance = maxDistance.toDouble()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Buscar lugares...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar"
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Limpiar"
                        )
                    }
                }
            },
            singleLine = true
        )
        
        // Botón para mostrar/ocultar filtros
        TextButton(
            onClick = { showFilters = !showFilters },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(if (showFilters) "Ocultar filtros" else "Mostrar filtros")
        }
        
        // Panel de filtros
        if (showFilters) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Filtro de tipo
                    Text(
                        text = "Tipo de lugar",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Chips para tipos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedType == null,
                            onClick = { selectedType = null },
                            label = { Text("Todos") }
                        )
                        
                        PlaceType.values().forEach { type ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { selectedType = if (selectedType == type) null else type },
                                label = { Text(type.displayName) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Filtro de distancia
                    Text(
                        text = "Distancia máxima: ${(maxDistance / 1000).toInt()} km",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Slider(
                        value = maxDistance,
                        onValueChange = { maxDistance = it },
                        valueRange = 0f..10000f,
                        steps = 19, // 0, 500, 1000, ..., 10000
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "0 km",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "10 km",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Botón para limpiar filtros
                    Button(
                        onClick = {
                            searchText = ""
                            selectedType = null
                            maxDistance = 5000f
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar filtros")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Contador de resultados
        Text(
            text = "${filteredPlaces.size} lugares encontrados",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Lista de lugares filtrados
        if (filteredPlaces.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se encontraron lugares",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Intenta ajustar los filtros",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filteredPlaces) { place ->
                    ListItem(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                onNavigateToPlaceDetail(place.id)
                            },
                        headlineContent = {
                            Text(text = place.title)
                        },
                        supportingContent = {
                            Column {
                                Text(text = place.description)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tipo: ${place.type.displayName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        trailingContent = {
                            AsyncImage(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .width(100.dp)
                                    .height(100.dp),
                                model = place.images.firstOrNull() ?: "",
                                contentDescription = place.title,
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }
            }
        }
    }
}
