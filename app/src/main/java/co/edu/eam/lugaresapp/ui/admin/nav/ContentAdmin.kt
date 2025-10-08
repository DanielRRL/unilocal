package co.edu.eam.lugaresapp.ui.admin.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.edu.eam.lugaresapp.ui.admin.screens.HistoryScreen
import co.edu.eam.lugaresapp.ui.admin.screens.PlacesListScreen
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * NAVEGACIÓN INTERNA DE ADMINISTRADOR
 * 
 * Maneja la navegación entre las pantallas del módulo de administración.
 * Recibe los ViewModels desde HomeAdmin para compartir estado entre pantallas.
 * 
 * @param padding Padding de la navegación principal
 * @param navController Controlador de navegación interna
 * @param placesViewModel ViewModel compartido de lugares
 * @param usersViewModel ViewModel compartido de usuarios
 */
@Composable
fun ContentAdmin(
    padding: PaddingValues,
    navController: NavHostController,
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel
){

    NavHost(
        navController = navController,
        startDestination = AdminScreen.PlacesList
    ){
        composable<AdminScreen.PlacesList> {
            PlacesListScreen(
                placesViewModel = placesViewModel,
                usersViewModel = usersViewModel
            )
        }
        composable<AdminScreen.History> {
            HistoryScreen(
                placesViewModel = placesViewModel
            )
        }
    }

}