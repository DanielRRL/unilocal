package co.edu.eam.lugaresapp.ui.user.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.edu.eam.lugaresapp.viewmodel.PlacesViewModel

@Composable
fun PlaceDetailScreen(
    placesViewModel: PlacesViewModel,
    padding: PaddingValues,
    id: String,
) {

    val place = placesViewModel.findById(id)

    Box(
        modifier = Modifier
            .padding(padding)
    ) {
        Text(
            text = place!!.title
        )
    }


}