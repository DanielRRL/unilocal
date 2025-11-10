package co.edu.eam.lugaresapp.utils

import kotlin.math.*

/**
 * Utilidades para cálculos de ubicación geográfica
 */
object LocationUtils {
    
    // Radio de la Tierra en kilómetros
    private const val EARTH_RADIUS_KM = 6371.0
    
    /**
     * Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine
     * 
     * @param lat1 Latitud del primer punto en grados
     * @param lon1 Longitud del primer punto en grados
     * @param lat2 Latitud del segundo punto en grados
     * @param lon2 Longitud del segundo punto en grados
     * @return Distancia en kilómetros
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        // Convertir grados a radianes
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)
        
        // Diferencias
        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad
        
        // Fórmula de Haversine
        val a = sin(dLat / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(dLon / 2).pow(2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        // Distancia en kilómetros
        return EARTH_RADIUS_KM * c
    }
    
    /**
     * Formatea la distancia para mostrar al usuario
     * 
     * @param distanceKm Distancia en kilómetros
     * @return String formateado (ej: "2.5 km" o "350 m")
     */
    fun formatDistance(distanceKm: Double): String {
        return when {
            distanceKm < 0.1 -> "${(distanceKm * 1000).roundToInt()} m"
            distanceKm < 1.0 -> "${String.format("%.1f", distanceKm * 1000)} m"
            else -> "${String.format("%.1f", distanceKm)} km"
        }
    }
}
