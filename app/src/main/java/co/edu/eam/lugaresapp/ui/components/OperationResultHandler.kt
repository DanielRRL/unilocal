package co.edu.eam.lugaresapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.eam.lugaresapp.utils.RequestResult
import kotlinx.coroutines.delay

/**
 * MANEJADOR DE RESULTADOS DE OPERACIONES
 * 
 * Componente reutilizable que muestra el estado de operaciones asíncronas
 * con una UI profesional usando Material 3.
 * 
 * ESTADOS SOPORTADOS:
 * - Loading: Muestra un indicador de progreso circular con mensaje
 * - Success: Muestra un card verde con ícono de éxito
 * - Failure: Muestra un card rojo con ícono de error
 * - null: No muestra nada
 * 
 * CARACTERÍSTICAS:
 * - Animaciones suaves al aparecer/desaparecer
 * - Diseño Material 3 con elevation y colores apropiados
 * - Auto-navegación después de éxito (2 segundos)
 * - Auto-cierre después de error (4 segundos)
 * 
 * @param result Estado actual de la operación (Loading/Success/Failure/null)
 * @param onSuccess Callback ejecutado después de éxito (ej: navegar)
 * @param onFailure Callback ejecutado después de error (ej: resetear estado)
 */
@Composable
fun OperationResultHandler(
    result: RequestResult?,
    onSuccess: suspend () -> Unit,
    onFailure: suspend () -> Unit,
) {
    // Mostrar indicador de carga cuando está en progreso
    AnimatedVisibility(
        visible = result is RequestResult.Loading,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Registrando usuario...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Mostrar mensaje de éxito
    AnimatedVisibility(
        visible = result is RequestResult.Success,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        if (result is RequestResult.Success) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.15f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Éxito",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = result.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Mostrar mensaje de error
    AnimatedVisibility(
        visible = result is RequestResult.Failure,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        if (result is RequestResult.Failure) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF44336).copy(alpha = 0.15f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color(0xFFF44336),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Error en el registro",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }

    // Efecto para ejecutar callbacks después de mostrar resultado
    LaunchedEffect(result) {
        when (result) {
            is RequestResult.Success -> {
                delay(2000) // Mostrar mensaje de éxito por 2 segundos
                onSuccess()
            }
            is RequestResult.Failure -> {
                delay(4000) // Mostrar mensaje de error por 4 segundos
                onFailure()
            }
            else -> {}
        }
    }
}   