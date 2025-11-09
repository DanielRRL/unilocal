package co.edu.eam.lugaresapp.ui.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.edu.eam.lugaresapp.ui.user.screens.MapScreen
import co.edu.eam.lugaresapp.ui.user.screens.PlaceDetailScreen
import co.edu.eam.lugaresapp.ui.user.screens.PlacesScreen
import co.edu.eam.lugaresapp.ui.user.screens.ProfileScreen
import co.edu.eam.lugaresapp.ui.user.screens.SearchScreen
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.RewiewsViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * NAVEGACIÓN INTERNA DE USUARIO
 * 
 * Maneja la navegación entre las pantallas del módulo de usuario.
 * Recibe los ViewModels desde HomeUser para compartir estado entre pantallas.
 * 
 * IMPORTANTE: No crea nuevas instancias de ViewModels, los recibe como parámetros
 * para garantizar que todas las pantallas compartan el mismo estado.
 * 
 * @param padding Padding de la navegación principal
 * @param navController Controlador de navegación interna
 * @param placesViewModel ViewModel compartido de lugares
 * @param reviewsViewModel ViewModel compartido de reseñas
 * @param usersViewModel ViewModel compartido de usuarios
 */
@Composable
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController,
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
    usersViewModel: UsersViewModel
){

    NavHost(
        navController = navController,
        startDestination = UserScreen.Map
    ){
        // Pantalla de Mapa
        composable<UserScreen.Map> {
            MapScreen()
        }
        
        // Pantalla de Búsqueda (placeholder)
        composable<UserScreen.Search> {
            SearchScreen()
        }
        
        // Pantalla de lista de lugares
        composable<UserScreen.Places> {
            PlacesScreen(
                padding = padding,
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = {
                    navController.navigate(UserScreen.PlaceDetail(it))
                }
            )
        }
        
        // Pantalla de perfil
        composable<UserScreen.Profile> {
            ProfileScreen(
                usersViewModel = usersViewModel,
                placesViewModel = placesViewModel,
                reviewsViewModel = reviewsViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                },
                onLogout = {
                    // La navegación al login se manejará desde Navigation.kt
                }
            )
        }
        
        // Pantalla de detalle de lugar
        composable<UserScreen.PlaceDetail> {
            val args = it.toRoute<UserScreen.PlaceDetail>()
            PlaceDetailScreen(
                placesViewModel = placesViewModel,
                reviewsViewModel = reviewsViewModel,
                usersViewModel = usersViewModel,
                padding = padding,
                id = args.id,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }

}