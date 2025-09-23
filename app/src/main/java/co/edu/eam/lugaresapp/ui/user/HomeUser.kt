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

@Composable
fun HomeUser(){

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
            padding = padding
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