package co.edu.eam.lugaresapp.model

/**
 * MODELO DE DATOS: LUGAR uniLocal
 * 
 * Representa un lugar registrado en la plataforma con toda su información.
 * Compatible con Firebase Firestore (requiere constructor sin argumentos).
 */
data class Place(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val address: String = "",
    val location: Location = Location(),
    val images: List<String> = emptyList(),
    val phones: List<String> = emptyList(),
    val type: PlaceType = PlaceType.OTHER,
    val schedules: List<Schedule> = emptyList(),
    val approved: Boolean = false,
    val ownerId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)