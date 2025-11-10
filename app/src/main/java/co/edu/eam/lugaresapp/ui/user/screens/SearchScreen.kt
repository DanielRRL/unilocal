package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.ui.components.PlaceCompactCard
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

/**
 * PANTALLA DE BÚSQUEDA
 * 
 * Pantalla que permite buscar lugares por categoría y distancia.
 * Muestra un mapa con todos los lugares filtrados y tarjetas en la parte inferior.
 * 
 * CARACTERÍSTICAS:
 * - Mapa de fondo con marcadores de lugares filtrados
 * - Barra de búsqueda en la parte superior
 * - Filtro por categoría (dropdown)
 * - Filtro por distancia (slider con valores en km)
 * - Lista de resultados en tarjetas compactas
 * - Botón flotante para agregar nuevo lugar
 * - Click en lugar navega al detalle
 * 
 * DISEÑO según imágenes proporcionadas:
 * - Fondo: Mapa con marcadores
 * - Top: SearchBar + filtros en card azul
 * - Bottom: Tarjetas de lugares con scroll
 * - Floating: Botón "+" para crear lugar
 * 
 * @param placesViewModel ViewModel de lugares
 * @param onNavigateToPlaceDetail Callback para navegar al detalle
 * @param onNavigateToCreatePlace Callback para crear nuevo lugar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit = {},
    onNavigateToCreatePlace: () -> Unit = {}
) {
    /**
     * ESTADOS DE FILTROS
     */
    var selectedCategory by remember { mutableStateOf<PlaceType?>(null) }
    var distanceKm by remember { mutableFloatStateOf(5f) } // Distancia en km
    var expandedCategoryDropdown by remember { mutableStateOf(false) }

    /**
     * OBTENER LUGARES FILTRADOS
     * - Solo lugares aprobados
     * - Filtrados por categoría si está seleccionada
     */
    val filteredPlaces = placesViewModel.getApprovedPlaces().filter { place ->
        selectedCategory == null || place.type == selectedCategory
    }

    Box(modifier = Modifier.fillMaxSize()) {
        /**
         * MAPA DE FONDO
         * Muestra todos los lugares filtrados con marcadores
         */
        Map(
            places = filteredPlaces,
            centerLatitude = 4.4687891,
            centerLongitude = -75.6491181,
            initialZoom = 13.0,
            hasLocationPermission = false, // TODO: Integrar permisos de ubicación
            onMarkerClick = { placeId ->
                onNavigateToPlaceDetail(placeId)
            }
        )

        /**
         * PANEL DE FILTROS (TOP)
         * Card azul con barra de búsqueda y filtros
         */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                /**
                 * BARRA DE BÚSQUEDA
                 * Muestra "Buscar" como título
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Buscar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                /**
                 * FILTRO POR CATEGORÍA
                 * Dropdown con todas las categorías disponibles
                 */
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCategoryDropdown,
                    onExpandedChange = { expandedCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.displayName ?: "Todas",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { 
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoryDropdown) 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategoryDropdown,
                        onDismissRequest = { expandedCategoryDropdown = false }
                    ) {
                        // Opción "Todas"
                        DropdownMenuItem(
                            text = { Text("Todas") },
                            onClick = {
                                selectedCategory = null
                                expandedCategoryDropdown = false
                            }
                        )

                        // Categorías específicas
                        PlaceType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedCategory = type
                                    expandedCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                /**
                 * FILTRO POR DISTANCIA
                 * Slider con valores de 0 a 10 km
                 */
                Text(
                    text = "Distancia",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = distanceKm,
                        onValueChange = { distanceKm = it },
                        valueRange = 0f..10f,
                        steps = 19, // Incrementos de 0.5 km
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "${distanceKm.toInt()} km",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        /**
         * LISTA DE RESULTADOS (BOTTOM)
         * Tarjetas compactas con scroll horizontal
         */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                /**
                 * ENCABEZADO DE RESULTADOS
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedCategory != null) {
                            selectedCategory!!.displayName
                        } else {
                            "Todos los lugares"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${filteredPlaces.size} lugares",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                /**
                 * LISTA DE LUGARES
                 * Tarjetas compactas horizontales con scroll
                 */
                if (filteredPlaces.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay lugares disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(filteredPlaces.take(5)) { place -> // Mostrar máximo 5 lugares
                            PlaceCompactCard(
                                place = place,
                                onClick = { onNavigateToPlaceDetail(place.id) }
                            )
                        }
                    }
                }
            }
        }

        /**
         * BOTÓN FLOTANTE PARA CREAR LUGAR
         * Ubicado en la esquina inferior derecha
         */
        FloatingActionButton(
            onClick = onNavigateToCreatePlace,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 220.dp, end = 16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar nuevo lugar",
                tint = Color.White
            )
        }
    }
}