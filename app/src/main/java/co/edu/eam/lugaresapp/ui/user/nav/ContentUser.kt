package co.edu.eam.lugaresapp.ui.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import co.edu.eam.lugaresapp.ui.user.screens.*
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
        /**
         * PANTALLA DE MAPA
         * Muestra todos los lugares aprobados en un mapa interactivo
         */
        composable<UserScreen.Map> {
            MapScreen(
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                }
            )
        }
        
        /**
         * PANTALLA DE BÚSQUEDA
         * Permite buscar lugares por categoría y distancia
         * Muestra mapa de fondo con resultados filtrados
         */
        composable<UserScreen.Search> {
            SearchScreen(
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                },
                onNavigateToCreatePlace = {
                    // TODO: Navegar a CreatePlaceScreen cuando esté integrada
                }
            )
        }
        
        /**
         * PANTALLA DE LISTA DE LUGARES
         * Vista de lista con filtros de búsqueda
         */
        composable<UserScreen.Places> {
            PlacesScreen(
                padding = padding,
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                }
            )
        }
        
        /**
         * PANTALLA DE FAVORITOS
         * Muestra todos los lugares marcados como favoritos por el usuario
         */
        composable<UserScreen.Favorites> {
            FavoritesScreen(
                usersViewModel = usersViewModel,
                placesViewModel = placesViewModel,
                reviewsViewModel = reviewsViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                }
            )
        }
        
        /**
         * PANTALLA DE MIS LUGARES
         * Muestra lugares creados por el usuario con sus estados
         */
        composable<UserScreen.MyPlaces> {
            MyPlacesScreen(
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = { placeId ->
                    navController.navigate(UserScreen.PlaceDetail(placeId))
                }
            )
        }
        
        /**
         * PANTALLA DE PERFIL
         * Información del usuario con opción de editar datos
         */
        composable<UserScreen.Profile> {
            ProfileScreen(
                usersViewModel = usersViewModel,
                onLogout = {
                    // La navegación al login se manejará desde Navigation.kt
                }
            )
        }
        
        /**
         * PANTALLA DE DETALLE DE LUGAR
         * Información completa de un lugar específico
         */
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