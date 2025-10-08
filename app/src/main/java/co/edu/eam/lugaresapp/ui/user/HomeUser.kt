package co.edu.eam.lugaresapp.ui.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.user.bottombar.BottomBarUser
import co.edu.eam.lugaresapp.ui.user.nav.ContentUser
import co.edu.eam.lugaresapp.ui.user.nav.UserScreen
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.RewiewsViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * HOME DEL USUARIO
 * 
 * Pantalla principal del módulo de usuario que contiene:
 * - TopBar con título dinámico
 * - BottomBar con navegación entre secciones
 * - ContentUser con las pantallas internas
 * 
 * @param placesViewModel ViewModel compartido de lugares
 * @param reviewsViewModel ViewModel compartido de reseñas
 * @param usersViewModel ViewModel compartido de usuarios
 */
@Composable
fun HomeUser(
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
    usersViewModel: UsersViewModel
){

    val navController = rememberNavController()
    var showTopBar by remember { mutableStateOf(true) }
    var titleTopBar by remember { mutableIntStateOf(R.string.title_user) }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showTopBar
            ){
                TopBarUser(
                    title = stringResource(titleTopBar)
                )
            }
        },
        bottomBar = {
            BottomBarUser(
                navController = navController,
                showTopBar = {
                    showTopBar = it
                },
                titleTopBar = {
                    titleTopBar = it
                }
            )
        }
    ) { padding ->
        ContentUser(
            navController = navController,
            padding = padding,
            placesViewModel = placesViewModel,
            reviewsViewModel = reviewsViewModel,
            usersViewModel = usersViewModel
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUser(
    title: String
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title
            )
        }
    )
}