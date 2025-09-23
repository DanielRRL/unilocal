package co.edu.eam.lugaresapp.ui.admin.nav

import kotlinx.serialization.Serializable

sealed class AdminScreen {

    @Serializable
    data object PlacesList : AdminScreen()

    @Serializable
    data object History : AdminScreen()

}