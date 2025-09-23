package co.edu.eam.lugaresapp.ui.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun ContentUser(
    padding: PaddingValues,
    navController: NavHostController
){

    val placesViewModel: PlacesViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = UserScreen.Map
    ){
        composable<UserScreen.Map> {
            MapScreen()
        }
        composable<UserScreen.Search> {
            SearchScreen()
        }
        composable<UserScreen.Places> {
            PlacesScreen(
                padding = padding,
                placesViewModel = placesViewModel,
                onNavigateToPlaceDetail = {
                    navController.navigate(UserScreen.PlaceDetail(it))
                }
            )
        }
        composable<UserScreen.Profile> {
            ProfileScreen()
        }
        composable<UserScreen.PlaceDetail> {
            val args = it.toRoute<UserScreen.PlaceDetail>()
            PlaceDetailScreen(
                placesViewModel = placesViewModel,
                padding = padding,
                id = args.id
            )
        }
    }

}