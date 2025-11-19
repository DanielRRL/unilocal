package co.edu.eam.lugaresapp.model

/**
 * MODELO DE DATOS: RESEÑA uniLocal
 * 
 * Representa una reseña o comentario de un usuario sobre un lugar.
 * 
 * FIREBASE FIRESTORE COMPATIBILITY:
 * - Constructor sin argumentos requerido para deserialización automática
 * - Valores por defecto permiten que Firebase cree instancias vacías
 * - date almacenado como Long (timestamp en milisegundos) para compatibilidad con Firebase
 * - Firebase mapea automáticamente los campos del documento a las propiedades
 * 
 * @property id Identificador único de la reseña
 * @property userID ID del usuario que realizó la reseña
 * @property placeID ID del lugar al que pertenece la reseña
 * @property rating Calificación numérica del lugar (típicamente 1-5)
 * @property comment Texto del comentario
 * @property date Fecha y hora de creación de la reseña (timestamp en milisegundos)
 * @property ownerResponse Respuesta opcional del propietario del lugar a la reseña
 */
data class Review(
    var id: String = "",
    val userID: String = "",
    val placeID: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val date: Long = System.currentTimeMillis(),
    val ownerResponse: String? = null
)