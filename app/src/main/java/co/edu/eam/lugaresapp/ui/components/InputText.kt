package co.edu.eam.lugaresapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * COMPONENTE INPUTTEXT AVANZADO
 * 
 * Este es un componente reutilizable que mejora OutlinedTextField de Material 3.
 * Proporciona funcionalidades adicionales como:
 * - Validación en tiempo real con mensajes de error
 * - Soporte para campos de contraseña con toggle show/hide
 * - Iconos leading personalizables
 * - Mensajes de error integrados
 * - Estilos consistentes con el tema uniLocal
 * 
 * @param value: String - El texto actual del campo
 * @param onValueChange: (String) -> Unit - Callback que se ejecuta cuando cambia el texto
 * @param label: String - Etiqueta flotante del campo
 * @param modifier: Modifier - Modificadores de Compose para personalizar el layout
 * @param placeholder: String - Texto de ayuda que aparece cuando el campo está vacío
 * @param isPassword: Boolean - Si es true, oculta el texto y muestra botón de toggle
 * @param isError: Boolean - Si es true, muestra el campo en estado de error
 * @param errorMessage: String - Mensaje de error a mostrar debajo del campo
 * @param keyboardType: KeyboardType - Tipo de teclado (Email, Number, etc.)
 * @param leadingIcon: ImageVector? - Icono opcional al inicio del campo
 * @param enabled: Boolean - Si el campo está habilitado o deshabilitado
 * @param maxLines: Int - Número máximo de líneas
 * @param singleLine: Boolean - Si debe ser una sola línea
 */
@Composable
fun InputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    maxLines: Int = 1,
    singleLine: Boolean = true
) {
    /**
     * ESTADO LOCAL PARA CONTRASEÑA
     * 
     * remember() mantiene el estado durante recomposiciones de Compose.
     * mutableStateOf() crea un estado reactivo que dispara recomposición cuando cambia.
     * 
     * passwordVisible controla si la contraseña está visible o oculta.
     */
    var passwordVisible by remember { mutableStateOf(false) }

    /**
     * LAYOUT EN COLUMNA
     * 
     * Column organiza los elementos verticalmente:
     * 1. OutlinedTextField (el campo de texto principal)
     * 2. Text (mensaje de error, solo si existe)
     */
    Column(modifier = modifier) {
        /**
         * CAMPO DE TEXTO PRINCIPAL
         * 
         * OutlinedTextField es el componente base de Material 3 para campos de texto.
         * Tiene borde visible y etiqueta flotante que se anima al hacer focus.
         */
        OutlinedTextField(
            value = value,                    // Texto actual del campo
            onValueChange = onValueChange,    // Callback cuando cambia el texto
            label = { 
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium  // Tipografía Material 3 para etiquetas
                ) 
            },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(text = placeholder) }  // Mostrar placeholder solo si no está vacío
            } else null,
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho disponible
            /**
             * TRANSFORMACIÓN VISUAL PARA CONTRASEÑAS
             * 
             * PasswordVisualTransformation() convierte el texto en puntos (•••)
             * VisualTransformation.None muestra el texto normal
             */
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType), // Tipo de teclado
            isError = isError,          // Estado de error (cambia colores)
            enabled = enabled,          // Si está habilitado o deshabilitado
            maxLines = maxLines,        // Número máximo de líneas
            singleLine = singleLine,    // Si debe ser una sola línea
            /**
             * ICONO LEADING (AL INICIO DEL CAMPO)
             * 
             * leadingIcon?.let { } es una forma segura de manejar valores nullable.
             * Solo ejecuta el bloque si leadingIcon no es null.
             */
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null, // null porque es decorativo
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Color del tema
                    )
                }
            },
            /**
             * ICONO TRAILING (AL FINAL DEL CAMPO)
             * 
             * Solo para campos de contraseña, muestra un botón para alternar visibilidad.
             */
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) { // Toggle del estado
                        Icon(
                            // Cambia el icono según el estado de visibilidad
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            } else null,
            shape = RoundedCornerShape(12.dp), // Bordes redondeados de 12 puntos de densidad
            /**
             * COLORES PERSONALIZADOS
             * 
             * OutlinedTextFieldDefaults.colors() permite personalizar todos los colores
             * del campo según el estado (focused, unfocused, error, etc.)
             */
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,        // Borde al hacer focus
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,      // Borde sin focus
                errorBorderColor = MaterialTheme.colorScheme.error,            // Borde en error
                focusedLabelColor = MaterialTheme.colorScheme.primary,         // Etiqueta al hacer focus
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, // Etiqueta sin focus
                errorLabelColor = MaterialTheme.colorScheme.error              // Etiqueta en error
            )
        )
        
        /**
         * MENSAJE DE ERROR
         * 
         * Solo se muestra si hay error Y hay un mensaje.
         * if es una expresión en Kotlin, puede usarse dentro de composables.
         */
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,      // Color de error del tema
                style = MaterialTheme.typography.bodySmall,   // Tipografía pequeña para errores
                modifier = Modifier.padding(start = 16.dp, top = 4.dp) // Padding para alinear con el campo
            )
        }
    }
}

/**
 * FUNCIÓN DE COMPATIBILIDAD
 * 
 * Esta es la versión anterior del componente, mantenida para compatibilidad
 * con código existente que use la interfaz anterior.
 * 
 * PATRÓN: Function Overloading - Kotlin permite múltiples funciones con el mismo nombre
 * pero diferentes parámetros.
 */
@Composable
fun InputText(
    label: String,
    supportingText: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    onValidate: (String) -> Boolean = { false }, // Función de validación personalizada
    isPassword: Boolean = false
) {
    /**
     * ESTADO LOCAL PARA ERROR
     * 
     * remember() persiste el estado entre recomposiciones
     * mutableStateOf() crea estado reactivo que dispara recomposición
     */
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)            // Llama al callback del padre
            isError = onValidate(it)     // Ejecuta validación personalizada
        },
        label = { Text(text = label) },
        isError = isError,               // Aplica estado de error
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = {
            /**
             * TEXTO DE SOPORTE CONDICIONAL
             * 
             * Solo muestra el texto de soporte si hay error Y el texto no está vacío.
             * isNotBlank() verifica que no sea null, vacío o solo espacios.
             */
            if (isError && supportingText.isNotBlank()) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}