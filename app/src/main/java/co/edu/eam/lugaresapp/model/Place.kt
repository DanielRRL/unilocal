package co.edu.eam.lugaresapp.model

/**
 * MODELO DE DATOS: LUGAR uniLocal
 * 
 * Representa un lugar registrado en la plataforma con toda su información.
 * 
 * @property id Identificador único del lugar
 * @property title Nombre del lugar
 * @property description Descripción detallada del lugar
 * @property address Dirección física del lugar
 * @property location Coordenadas geográficas del lugar
 * @property images URLs de imágenes del lugar
 * @property phones Números de teléfono de contacto
 * @property type Categoría del lugar
 * @property schedules Horarios de atención
 * @property approved Indica si el lugar ha sido aprobado por un administrador (moderación)
 * @property ownerId ID del usuario propietario del lugar (puede ser null para lugares heredados)
 * @property createdAt Timestamp de creación del lugar en milisegundos
 */
data class Place(
    val id: String,
    val title: String,
    val description: String,
    val address: String,
    val location: Location,
    val images: List<String>,
    val phones: List<String>,
    val type: PlaceType,
    val schedules: List<Schedule>,
    val approved: Boolean = false,
    val ownerId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)