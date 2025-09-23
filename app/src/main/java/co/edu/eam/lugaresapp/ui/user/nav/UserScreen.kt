package co.edu.eam.lugaresapp.ui.user.nav

import kotlinx.serialization.Serializable

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
    data class PlaceDetail(val id: String) : UserScreen()

}