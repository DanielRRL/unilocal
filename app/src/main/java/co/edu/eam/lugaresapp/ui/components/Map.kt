
package co.edu.eam.lugaresapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.model.Place

/**
 * COMPONENTE DE MAPA REUTILIZABLE
 * 
 * Componente que encapsula la lógica de visualización de mapas usando Mapbox.
 * Sigue el principio de Responsabilidad Única (SOLID) al enfocarse solo en la visualización del mapa.
 * 
 * CARACTERÍSTICAS:
 * - Muestra un mapa centrado en coordenadas específicas
 * - Renderiza marcadores para una lista de lugares
 * - Soporta ubicación del usuario si tiene permisos
 * - Zoom y pitch configurables
 * - Estilo de mapa personalizable
 * 
 * PRINCIPIOS APLICADOS:
 * - Single Responsibility: Solo se encarga de renderizar el mapa
 * - Open/Closed: Extensible mediante parámetros, cerrado para modificación
 * - Dependency Inversion: Depende de abstracciones (List<Place>), no de implementaciones concretas
 * 
 * @param modifier Modificador para personalizar el layout del mapa
 * @param places Lista de lugares a mostrar como marcadores en el mapa
 * @param centerLatitude Latitud del centro inicial del mapa
 * @param centerLongitude Longitud del centro inicial del mapa
 * @param initialZoom Nivel de zoom inicial (1.0 = mundo completo, 22.0 = máximo zoom)
 * @param initialPitch Inclinación inicial del mapa en grados (0.0 = vista 2D, 60.0 = vista 3D)
 * @param hasLocationPermission Indica si la app tiene permisos de ubicación
 * @param onMarkerClick Callback cuando se hace click en un marcador (recibe el ID del lugar)
 * @param onMapClick Callback cuando se hace click en el mapa (recibe latitud y longitud)
 * 
 * EJEMPLO DE USO:
 * ```kotlin
 * Map(
 *     places = approvedPlaces,
 *     centerLatitude = 4.4687891,
 *     centerLongitude = -75.6491181,
 *     initialZoom = 13.0,
 *     hasLocationPermission = true,
 *     onMarkerClick = { placeId -> navigateToDetail(placeId) },
 *     onMapClick = { lat, lng -> updateLocation(lat, lng) }
 * )
 * ```
 */
@Composable
fun Map(
    modifier: Modifier = Modifier,
    places: List<Place> = emptyList(),
    centerLatitude: Double = 4.4687891,    // Armenia, Quindío por defecto
    centerLongitude: Double = -75.6491181,
    initialZoom: Double = 13.0,
    initialPitch: Double = 0.0,
    hasLocationPermission: Boolean = false,
    onMarkerClick: (String) -> Unit = {},
    onMapClick: (Double, Double) -> Unit = { _, _ -> }
) {
    /**
     * Estado del viewport del mapa
     * Maneja la cámara (posición, zoom, rotación, pitch)
     */
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            // Punto central del mapa (longitud, latitud)
            center(Point.fromLngLat(centerLongitude, centerLatitude))
            
            // Nivel de zoom (1.0 = mundo completo, 22.0 = edificios individuales)
            zoom(initialZoom)
            
            // Inclinación de la cámara (0.0 = vista cenital, 60.0 = perspectiva)
            pitch(45.0)
        }
    }

    /**
     * Icono personalizado para los marcadores
     * rememberIconImage carga y cachea el drawable
     */
    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource(id = R.drawable.red_marker)
    )

    /**
     * Componente principal del mapa
     */
    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = mapViewportState
    ) {
        /**
         * Renderizar marcadores para cada lugar
         * Cada PointAnnotation representa un pin en el mapa
         */
        places.forEach { place ->
            PointAnnotation(
                // Posición del marcador (debe coincidir con place.location)
                point = Point.fromLngLat(
                    place.location.longitude,
                    place.location.latitude
                ),
                // Click listener que propaga el evento hacia arriba
                onClick = {
                    onMarkerClick(place.id)
                    true // Consumir el evento
                }
            ) {
                // Configuración del icono del marcador
                iconImage = marker
            }
        }

        /**
         * Configurar puck de ubicación del usuario
         * Solo si se tienen los permisos necesarios
         */
        if (hasLocationPermission) {
            MapEffect(key1 = "location_puck") { mapView ->
                mapView.location.updateSettings {
                    // Habilitar visualización de ubicación
                    enabled = true
                    
                    // Puck 2D por defecto (punto azul con círculo)
                    locationPuck = createDefault2DPuck(withBearing = true)
                    
                    // Orientación del puck según la dirección del movimiento
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }
            }
        }
        
        /**
         * Configurar listener para clicks en el mapa
         * Permite selección interactiva de ubicación
         */
        MapEffect(key1 = "map_click_listener") { mapView ->
            val clickListener = OnMapClickListener { point ->
                // Llamar callback con latitud y longitud
                onMapClick(point.latitude(), point.longitude())
                true // Consumir el evento
            }
            mapView.mapboxMap.addOnMapClickListener(clickListener)
        }
    }
}