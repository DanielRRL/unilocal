package co.edu.eam.lugaresapp.ui.user.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import co.edu.eam.lugaresapp.ui.components.Map
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

@Composable
fun MapScreen(
    placesViewModel: PlacesViewModel,
    onNavigateToPlaceDetail: (String) -> Unit = {}
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
    }
}
