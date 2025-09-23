package co.edu.eam.lugaresapp.ui.user.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
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

                        //showTopBar(destination.showTopBar)
                        //titleTopBar(destination.label)
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

enum class Destination(
    val route: UserScreen,
    val label: Int,
    val icon: ImageVector,
    val showTopBar: Boolean = true
){
    HOME(UserScreen.Map, R.string.menu_home, Icons.Default.Home ),
    SEARCH(UserScreen.Search, R.string.menu_search, Icons.Default.Search, false),
    MY_PLACES(UserScreen.Places, R.string.menu_my_places, Icons.Default.Place),
    PROFILE(UserScreen.Profile, R.string.menu_profile, Icons.Default.AccountCircle)
}