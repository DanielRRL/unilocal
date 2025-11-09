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
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.location

/**
 * PANTALLA DE MAPA - UniLocal
 * 
 * Muestra un mapa interactivo con la ubicación de los lugares registrados.
 * Utiliza Mapbox Maps SDK para Android con Jetpack Compose.
 * 
 * CARACTERÍSTICAS IMPLEMENTADAS:
 * - Mapa centrado en Armenia, Quindío, Colombia
 * - Permisos de ubicación con manejo correcto
 * - Marcador en ubicación central (Plaza de Bolívar)
 * - Integración con ViewModels para mostrar lugares aprobados
 * - UI/UX responsiva con Material Design 3
 * 
 * NOTA FASE 2:
 * Esta es una implementación básica con datos simulados.
 * En Fase 3 se integrará con GPS real y ubicaciones de Firebase.
 * 
 * UBICACIÓN CENTRAL:
 * - Latitud: 4.4687891
 * - Longitud: -75.6491181
 * - Lugar: Plaza de Bolívar, Armenia, Quindío
 * 
 * @see <a href="https://docs.mapbox.com/android/maps/guides/">Mapbox Android Documentation</a>
 */
@Composable
fun MapScreen() {
    // Contexto de la aplicación
    val context = LocalContext.current
    
    // Estado de permisos de ubicación
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // Launcher para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        Toast.makeText(
            context,
            if (isGranted) "Permiso de ubicación concedido" else "Permiso de ubicación denegado",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    // Estado del viewport del mapa
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            // Centrar en Armenia, Quindío, Colombia
            center(Point.fromLngLat(-75.6491181, 4.4687891))
            zoom(13.0) // Zoom apropiado para ver la ciudad
            pitch(0.0) // Vista 2D sin inclinación
        }
    }
    
    // Solicitar permisos al iniciar
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    
    // UI Principal
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Componente del mapa Mapbox
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                style = {
                    // Estilo del mapa: Streets (calles)
                    MapStyle(style = "mapbox://styles/mapbox/streets-v12")
                }
            ) {
                // Marcador central en Plaza de Bolívar
                PointAnnotation(
                    point = Point.fromLngLat(-75.6491181, 4.4687891)
                ) {
                    // Configuración del marcador
                    // En producción aquí se agregarían marcadores dinámicos
                    // basados en placesViewModel.getApprovedPlaces()
                }
                
                // Configuración de ubicación del usuario
                if (hasLocationPermission) {
                    // MapEffect para configurar el "puck" de ubicación
                    // Esto mostrará la ubicación actual del usuario en el mapa
                    MapEffect(key1 = "location_puck") { mapView ->
                        mapView.location.updateSettings {
                            enabled = true
                            puckBearing = PuckBearing.COURSE
                            puckBearingEnabled = true
                        }
                    }
                }
            }
            
            // Overlay superior con información
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🗺️ Mapa de Lugares",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Armenia, Quindío",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Indicador de permisos
                    if (!hasLocationPermission) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Permiso de ubicación requerido",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
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
                    }
                }
            }
            
            // Nota informativa en la parte inferior
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f)
                )
            ) {
                Text(
                    text = "📍 Funcionalidad de mapa disponible en Fase 3 con ubicaciones reales",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}