package co.edu.eam.lugaresapp.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import co.edu.eam.lugaresapp.R
import co.edu.eam.lugaresapp.data.SessionManager
import co.edu.eam.lugaresapp.ui.admin.bottombar.BottomBarAdmin
import co.edu.eam.lugaresapp.ui.admin.nav.ContentAdmin
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel
import co.edu.eam.lugaresapp.viewmodel.UsersViewModel

/**
 * HOME DEL ADMINISTRADOR
 * 
 * Pantalla principal del módulo de administración que contiene:
 * - TopBar con título y botón de logout
 * - BottomBar con navegación entre secciones
 * - ContentAdmin con las pantallas de moderación e historial
 * 
 * @param placesViewModel ViewModel compartido de lugares
 * @param usersViewModel ViewModel compartido de usuarios
 * @param onLogout Callback para cerrar sesión y volver al login
 */
@Composable
fun HomeAdmin(
    placesViewModel: PlacesViewModel,
    usersViewModel: UsersViewModel,
    onLogout: () -> Unit
){
    val context = LocalContext.current
    val navController = rememberNavController()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarAdmin(
                onLogoutClick = { showLogoutDialog = true }
            )
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
    
    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro que deseas cerrar sesión como administrador?") },
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
fun TopBarAdmin(
    onLogoutClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.title_admin)
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