package co.edu.eam.lugaresapp.ui.admin.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.edu.eam.lugaresapp.ui.admin.screens.HistoryScreen
import co.edu.eam.lugaresapp.ui.admin.screens.PlacesListScreen

@Composable
fun ContentAdmin(
    padding: PaddingValues,
    navController: NavHostController
){

    NavHost(
        navController = navController,
        startDestination = AdminScreen.PlacesList
    ){
        composable<AdminScreen.PlacesList> {
            PlacesListScreen()
        }
        composable<AdminScreen.History> {
            HistoryScreen()
        }
    }

}