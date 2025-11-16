package co.edu.eam.lugaresapp.model

/**
 * MODELO DE UBICACIÓN
 * 
 * Coordenadas geográficas con valores por defecto para Firebase.
 */
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)