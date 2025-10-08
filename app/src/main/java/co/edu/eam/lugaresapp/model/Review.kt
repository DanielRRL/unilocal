package co.edu.eam.lugaresapp.model

import java.time.LocalDateTime

/**
 * MODELO DE DATOS: RESEÑA uniLocal
 * 
 * Representa una reseña o comentario de un usuario sobre un lugar.
 * 
 * @property id Identificador único de la reseña
 * @property userID ID del usuario que realizó la reseña
 * @property placeID ID del lugar al que pertenece la reseña
 * @property rating Calificación numérica del lugar (típicamente 1-5)
 * @property comment Texto del comentario
 * @property date Fecha y hora de creación de la reseña
 * @property ownerResponse Respuesta opcional del propietario del lugar a la reseña
 */
data class Review(
    val id: String,
    val userID: String,
    val placeID: String,
    val rating: Int,
    val comment: String,
    val date: LocalDateTime,
    val ownerResponse: String? = null
)