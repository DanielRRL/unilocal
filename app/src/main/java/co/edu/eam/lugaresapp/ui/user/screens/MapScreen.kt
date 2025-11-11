package co.edu.eam.lugaresapp.ui.user.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

@Composable
fun MapScreen(
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToCreatePlace: () -> Unit = {}
) {
    val context = LocalContext.current
    
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    
    val approvedPlaces = placesViewModel.getApprovedPlaces()
    
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Map(
            places = approvedPlaces,
            centerLatitude = 4.4687891,
            centerLongitude = -75.6491181,
            initialZoom = 13.0,
            initialPitch = 0.0,
            hasLocationPermission = hasLocationPermission,
            onMarkerClick = { placeId ->
                onNavigateToPlaceDetail(placeId)
            }
        )
        
        // Botón de búsqueda en la parte superior
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = onNavigateToSearch
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Buscar",
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            }
        }
        
        // Botón flotante para crear lugar
        FloatingActionButton(
            onClick = onNavigateToCreatePlace,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear lugar",
                tint = Color.White
            )
        }
    }
}
