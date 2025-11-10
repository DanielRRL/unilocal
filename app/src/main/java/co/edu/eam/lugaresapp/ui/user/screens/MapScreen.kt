package co.edu.eam.lugaresapp.ui.user.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

/**
 * PANTALLA DE MAPA - UniLocal
 * 
 * Pantalla que muestra todos los lugares aprobados en un mapa interactivo.
 * Utiliza Mapbox Maps SDK para Android con Jetpack Compose.
 * 
 * CARACTERÍSTICAS IMPLEMENTADAS:
 * - Mapa centrado en Armenia, Quindío, Colombia (Plaza de Bolívar)
 * - Gestión de permisos de ubicación con feedback visual
 * - Marcadores interactivos para cada lugar aprobado
 * - Click en marcador navega al detalle del lugar
 * - Ubicación del usuario en tiempo real (si hay permisos)
 * - UI/UX responsiva con Material Design 3
 * 
 * ARQUITECTURA:
 * - Sigue principio de Separación de Responsabilidades
 * - MapScreen gestiona: estado de UI, permisos, navegación
 * - Componente Map gestiona: renderizado del mapa, marcadores
 * - PlacesViewModel proporciona: datos de lugares
 * 
 * FLUJO DE PERMISOS:
 * 1. Al montar: Verifica permisos de ubicación
 * 2. Si no hay: Muestra card con botón para solicitar
 * 3. Usuario acepta: Activa ubicación en el mapa
 * 4. Usuario rechaza: Mapa funciona sin ubicación del usuario
 * 
 * UBICACIÓN CENTRAL:
 * - Latitud: 4.4687891
 * - Longitud: -75.6491181
 * - Lugar: Plaza de Bolívar, Armenia, Quindío, Colombia
 * 
 * @param placesViewModel ViewModel que proporciona lista de lugares
 * @param onNavigateToPlaceDetail Callback para navegar al detalle de un lugar
 * 
 * @see <a href="https://docs.mapbox.com/android/maps/guides/">Mapbox Android Documentation</a>
 */
@Composable
fun MapScreen(
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit = {}
) {
    /**
     * Contexto de la aplicación
     * Necesario para verificar y solicitar permisos
     */
    val context = LocalContext.current
    
    /**
     * Estado de permisos de ubicación
     * Verifica si el permiso ACCESS_FINE_LOCATION está concedido
     */
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    /**
     * Launcher para solicitar permisos
     * Contrato ActivityResultContracts.RequestPermission() maneja la solicitud
     */
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Actualizar estado cuando el usuario responde
        hasLocationPermission = isGranted
        
        // Feedback visual mediante Toast
        Toast.makeText(
            context,
            if (isGranted) "✓ Permiso de ubicación concedido" else "✗ Permiso de ubicación denegado",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    /**
     * Obtener lugares aprobados del ViewModel
     * Solo los lugares con approved=true se muestran en el mapa
     */
    val approvedPlaces = placesViewModel.getApprovedPlaces()
    
    /**
     * Efecto secundario: Solicitar permisos al iniciar
     * LaunchedEffect con Unit se ejecuta solo una vez al montar
     */
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    
    /**
     * UI PRINCIPAL
     * Box permite superponer elementos (mapa + overlays)
     */
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            /**
             * COMPONENTE DE MAPA
             * Renderiza el mapa con todos los lugares aprobados
             */
            Map(
                // Lugares a mostrar como marcadores
                places = approvedPlaces,
                
                // Centro: Plaza de Bolívar, Armenia, Quindío
                centerLatitude = 4.4687891,
                centerLongitude = -75.6491181,
                
                // Zoom apropiado para ver la ciudad completa
                initialZoom = 13.0,
                
                // Vista 2D sin inclinación
                initialPitch = 0.0,
                
                // Habilitar ubicación del usuario si hay permisos
                hasLocationPermission = hasLocationPermission,
                
                // Callback cuando se hace click en un marcador
                onMarkerClick = { placeId ->
                    onNavigateToPlaceDetail(placeId)
                }
            )
            
            /**
             * OVERLAY SUPERIOR: Información del mapa
             * Card semitransparente en la parte superior
             */
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    /**
                     * TÍTULO DEL MAPA
                     */
                    Text(
                        text = "🗺️ Mapa de Lugares",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    /**
                     * UBICACIÓN CENTRAL
                     */
                    Text(
                        text = "Armenia, Quindío",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    /**
                     * CONTADOR DE LUGARES
                     */
                    Text(
                        text = "${approvedPlaces.size} ${if (approvedPlaces.size == 1) "lugar" else "lugares"} disponibles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    /**
                     * INDICADOR DE PERMISOS
                     * Solo se muestra si no hay permisos concedidos
                     */
                    if (!hasLocationPermission) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Text(
                            text = "⚠️ Permiso de ubicación requerido",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        /**
                         * BOTÓN PARA SOLICITAR PERMISOS
                         */
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Conceder Permiso")
                        }
                    } else {
                        /**
                         * INDICADOR DE PERMISOS CONCEDIDOS
                         */
                        Text(
                            text = "✓ Ubicación en tiempo real activada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            /**
             * OVERLAY INFERIOR: Instrucciones
             * Card semitransparente en la parte inferior
             */
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f)
                )
            ) {
                Text(
                    text = "� Toca un marcador para ver los detalles del lugar",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}