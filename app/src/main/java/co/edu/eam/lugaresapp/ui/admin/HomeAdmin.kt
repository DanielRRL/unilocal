package co.edu.eam.lugaresapp.ui.admin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.admin.bottombar.BottomBarAdmin
import co.edu.eam.lugaresapp.ui.admin.nav.ContentAdmin
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * HOME DEL ADMINISTRADOR
 * 
 * Pantalla principal del módulo de administración que contiene:
 * - TopBar con título
 * - BottomBar con navegación entre secciones
 * - ContentAdmin con las pantallas de moderación e historial
 * 
 * @param placesViewModel ViewModel compartido de lugares
 * @param usersViewModel ViewModel compartido de usuarios
 */
@Composable
fun HomeAdmin(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel
){

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarAdmin()
        },
        bottomBar = {
            BottomBarAdmin(
                navController = navController
            )
        }
    ) { padding ->
        ContentAdmin(
            navController = navController,
            padding = padding,
            placesViewModel = placesViewModel,
            usersViewModel = usersViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAdmin(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.title_admin)
            )
        }
    )
}