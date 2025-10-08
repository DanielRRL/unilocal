package co.edu.eam.lugaresapp.model

/**
 * MODELO DE DATOS: REGISTRO DE MODERACIÓN uniLocal
 * 
 * Representa un registro de acciones de moderación realizadas sobre lugares.
 * Permite auditoría y seguimiento de aprobaciones/rechazos de lugares.
 * 
 * @property id Identificador único del registro de moderación
 * @property placeId ID del lugar sobre el que se realizó la acción de moderación
 * @property moderatorId ID del usuario moderador que realizó la acción
 * @property action Tipo de acción realizada (APPROVE, REJECT, etc.)
 * @property timestamp Timestamp en milisegundos de cuando se realizó la acción
 * @property reason Razón opcional de la acción (especialmente útil para rechazos)
 */
data class ModerationRecord(
    val id: String,
    val placeId: String,
    val moderatorId: String,
    val action: String,
    val timestamp: Long,
    val reason: String? = null
)
