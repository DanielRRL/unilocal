package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlacesViewModel: ViewModel() {

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces(){

        _places.value = listOf(
            Place(
                id = "1",
                title = "Restaurante El paisa",
                description = "El mejor restaurante paisa",
                address = "Cra 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://elbalconpaisa.com/images/about-img-1.png"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.RESTAURANT,
                schedules = listOf()
            ),
            Place(
                id = "2",
                title = "Bar test 1",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "3",
                title = "Bar test 2",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "4",
                title = "Bar test 3",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "5",
                title = "Bar test 4",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "6",
                title = "Bar test 5",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            )
        )

    }

    fun create(place: Place){
        _places.value = _places.value + place
    }

    fun findById(id: String): Place?{
        return _places.value.find { it.id == id }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    fun findByName(name: String): List<Place>{
        return _places.value.filter { it.title.contains(name) }
    }

}