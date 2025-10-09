package co.edu.eam.lugaresapp.ui.user

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.data.SessionManager
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
 * - TopBar con título dinámico y botón de logout
 * - BottomBar con navegación entre secciones
 * - ContentUser con las pantallas internas
 * 
 * @param placesViewModel ViewModel compartido de lugares
 * @param reviewsViewModel ViewModel compartido de reseñas
 * @param usersViewModel ViewModel compartido de usuarios
 * @param onLogout Callback para cerrar sesión y volver al login
 */
@Composable
fun HomeUser(
    placesViewModel: PlacesViewModel,
    reviewsViewModel: RewiewsViewModel,
    usersViewModel: UsersViewModel,
    onLogout: () -> Unit
){
    val context = LocalContext.current
    val navController = rememberNavController()
    var showTopBar by remember { mutableStateOf(true) }
    var titleTopBar by remember { mutableIntStateOf(R.string.title_user) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showTopBar
            ){
                TopBarUser(
                    title = stringResource(titleTopBar),
                    onLogoutClick = { showLogoutDialog = true }
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
    
    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        val sessionManager = SessionManager(context)
                        sessionManager.clear()
                        showLogoutDialog = false
                        Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUser(
    title: String,
    onLogoutClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title
            )
        },
        actions = {
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}