package co.edu.eam.lugaresapp.model

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
)