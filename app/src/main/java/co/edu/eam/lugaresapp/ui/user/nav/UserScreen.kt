package co.edu.eam.lugaresapp.ui.user.nav

import kotlinx.serialization.Serializable

/**
 * RUTAS DE NAVEGACIÓN DE USUARIO
 * 
 * Define todas las pantallas disponibles para usuarios normales.
 */
sealed class UserScreen {

    @Serializable
    data object Map : UserScreen()

    @Serializable
    data object Search : UserScreen()

    @Serializable
    data object Places : UserScreen()

    @Serializable
    data object Profile : UserScreen()
    
    @Serializable
    data object Favorites : UserScreen()
    
    @Serializable
    data object MyPlaces : UserScreen()
    
    @Serializable
    data object CreatePlace : UserScreen()

    @Serializable
    data class PlaceDetail(val id: String) : UserScreen()

}