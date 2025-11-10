package co.edu.eam.lugaresapp.ui.user.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.ui.user.nav.UserScreen

/**
 * BARRA DE NAVEGACIÓN INFERIOR DE USUARIO
 * 
 * Muestra 4 opciones principales:
 * - Inicio: Mapa con lugares
 * - Favoritos: Lugares favoritos del usuario
 * - Mis Lugares: Lugares creados por el usuario
 * - Perfil: Información del usuario
 * 
 * NOTA: Búsqueda se mantiene separada y se accede desde el mapa
 */
@Composable
fun BottomBarUser(
    navController: NavHostController,
    showTopBar: (Boolean) -> Unit,
    titleTopBar: (Int) -> Unit
){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(currentDestination) {
        val destination = Destination.entries.find { it.route::class.qualifiedName == currentDestination?.route }
        if (destination != null) {
            showTopBar(destination.showTopBar)
            titleTopBar(destination.label)
        }
    }

    NavigationBar{

        Destination.entries.forEachIndexed { index, destination ->

            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(destination.label)
                    )
                },
                onClick = {
                    navController.navigate(destination.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                selected = isSelected
            )
        }
    }
}

/**
 * DESTINOS DE LA NAVEGACIÓN INFERIOR
 * 
 * Define las 4 pantallas principales accesibles desde la barra inferior
 */
enum class Destination(
    val route: UserScreen,
    val label: Int,
    val icon: ImageVector,
    val showTopBar: Boolean = true
){
    HOME(UserScreen.Map, R.string.menu_home, Icons.Default.Home, false),
    FAVORITES(UserScreen.Favorites, R.string.menu_favorites, Icons.Default.Favorite),
    MY_PLACES(UserScreen.MyPlaces, R.string.menu_my_places, Icons.Default.Place),
    PROFILE(UserScreen.Profile, R.string.menu_profile, Icons.Default.AccountCircle)
}