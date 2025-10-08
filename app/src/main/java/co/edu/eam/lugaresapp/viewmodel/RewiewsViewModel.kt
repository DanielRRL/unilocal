package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import co.edu.eam.lugaresapp.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.util.UUID

/**
 * VIEWMODEL DE RESEÑAS uniLocal
 * 
 * Este ViewModel maneja toda la lógica de negocio relacionada con reseñas y valoraciones de lugares.
 * 
 * RESPONSABILIDADES:
 * - Gestión CRUD de reseñas
 * - Filtrado de reseñas por lugar
 * - Sistema de respuestas de propietarios a reseñas
 * - Exposición reactiva de datos mediante StateFlow
 * 
 * PATRÓN ARQUITECTÓNICO:
 * - StateFlow para exposición reactiva de datos
 * - Inmutabilidad mediante copy() y reasignación
 * - Sin efectos secundarios fuera del ViewModel
 * - Persistencia en memoria (temporal)
 * 
 * CASOS DE USO:
 * - Usuarios pueden agregar reseñas a lugares
 * - Propietarios pueden responder a reseñas
 * - Administradores pueden eliminar reseñas inapropiadas
 * - Consulta de reseñas por lugar específico
 */
class RewiewsViewModel: ViewModel() {

    /**
     * Estado privado de reseñas
     * Lista mutable que contiene todas las reseñas de la aplicación
     */
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    
    /**
     * Estado público de reseñas para observación desde la UI
     * Los Composables que observen este StateFlow se recompondrán automáticamente
     * cuando la lista de reseñas cambie
     */
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init {
        loadSampleReviews()
    }

    /**
     * CARGA DE RESEÑAS DE PRUEBA
     * 
     * Carga datos de prueba para demostración.
     * En producción, estos datos vendrían de una base de datos o API.
     */
    private fun loadSampleReviews() {
        _reviews.value = listOf(
            Review(
                id = "review1",
                userID = "2", // Daniel (usuario regular)
                placeID = "1", // Restaurante El Paisa
                rating = 5,
                comment = "Excelente comida y servicio. Muy recomendado!",
                date = LocalDateTime.now().minusDays(2),
                ownerResponse = "Gracias por tu comentario! Esperamos verte pronto."
            ),
            Review(
                id = "review2",
                userID = "2",
                placeID = "1",
                rating = 4,
                comment = "Buena comida pero un poco caro.",
                date = LocalDateTime.now().minusDays(1)
                // Sin respuesta del propietario
            ),
            Review(
                id = "review3",
                userID = "2",
                placeID = "2", // Bar test 1
                rating = 3,
                comment = "Ambiente agradable pero demoran mucho.",
                date = LocalDateTime.now().minusHours(5)
            )
        )
    }

    // ==================== OPERACIONES CRUD ====================

    /**
     * AGREGAR NUEVA RESEÑA
     * 
     * Añade una nueva reseña a la lista de reseñas.
     * La reseña se añade con los datos proporcionados.
     * 
     * FUNCIONAMIENTO:
     * - Concatena la nueva reseña a la lista existente
     * - El StateFlow notifica automáticamente el cambio
     * - La UI se actualiza reactivamente
     * 
     * @param review Reseña a agregar
     * 
     * @example
     * ```kotlin
     * val newReview = Review(
     *     id = UUID.randomUUID().toString(),
     *     userID = currentUserId,
     *     placeID = placeId,
     *     rating = 5,
     *     comment = "Excelente lugar!",
     *     date = LocalDateTime.now()
     * )
     * rewiewsViewModel.addReview(newReview)
     * ```
     */
    fun addReview(review: Review) {
        _reviews.value = _reviews.value + review
    }

    /**
     * BUSCAR RESEÑAS POR LUGAR
     * 
     * Retorna todas las reseñas asociadas a un lugar específico.
     * Útil para mostrar las valoraciones en la pantalla de detalle de un lugar.
     * 
     * FUNCIONAMIENTO:
     * - Filtra la lista de reseñas por placeID
     * - Retorna una nueva lista inmutable
     * - No modifica el estado interno
     * 
     * @param placeId ID del lugar del cual obtener reseñas
     * @return Lista de reseñas del lugar especificado
     * 
     * @example
     * ```kotlin
     * val placeReviews = rewiewsViewModel.findByPlaceId("place123")
     * // Usar placeReviews para mostrar en UI
     * ```
     */
    fun findByPlaceId(placeId: String): List<Review> {
        return _reviews.value.filter { it.placeID == placeId }
    }

    /**
     * RESPONDER A UNA RESEÑA
     * 
     * Permite al propietario de un lugar responder a una reseña específica.
     * Actualiza el campo ownerResponse de la reseña.
     * 
     * FUNCIONAMIENTO:
     * - Busca la reseña por ID
     * - Crea una copia con el campo ownerResponse actualizado
     * - Mantiene las demás reseñas sin cambios
     * - El StateFlow notifica automáticamente el cambio
     * 
     * VALIDACIÓN:
     * - Se recomienda validar que el usuario que responde sea el propietario del lugar
     * - Esta validación debe hacerse en la capa de UI o en un caso de uso
     * 
     * @param reviewId ID de la reseña a la que se responde
     * @param response Texto de la respuesta del propietario
     * 
     * @example
     * ```kotlin
     * // Propietario responde a una reseña
     * rewiewsViewModel.replyToReview(
     *     reviewId = "review123",
     *     response = "Gracias por tu comentario, trabajaremos en mejorar!"
     * )
     * ```
     */
    fun replyToReview(reviewId: String, response: String) {
        _reviews.value = _reviews.value.map { review ->
            if (review.id == reviewId) {
                review.copy(ownerResponse = response)
            } else {
                review
            }
        }
    }

    /**
     * ELIMINAR RESEÑA
     * 
     * Elimina una reseña de la lista por su ID.
     * Útil para moderación de contenido inapropiado.
     * 
     * FUNCIONAMIENTO:
     * - Filtra la lista excluyendo la reseña con el ID especificado
     * - Crea una nueva lista sin la reseña eliminada
     * - El StateFlow notifica automáticamente el cambio
     * 
     * CASOS DE USO:
     * - Administradores eliminan contenido inapropiado
     * - Usuarios eliminan sus propias reseñas
     * - Sistema automático de moderación
     * 
     * @param reviewId ID de la reseña a eliminar
     * 
     * @example
     * ```kotlin
     * // Administrador elimina reseña inapropiada
     * rewiewsViewModel.deleteReview("review123")
     * ```
     */
    fun deleteReview(reviewId: String) {
        _reviews.value = _reviews.value.filter { it.id != reviewId }
    }

    // ==================== OPERACIONES DE CONSULTA Y ANÁLISIS ====================

    /**
     * BUSCAR RESEÑA POR ID
     * 
     * Busca una reseña específica por su ID único.
     * 
     * @param reviewId ID de la reseña a buscar
     * @return Reseña encontrada o null si no existe
     */
    fun findById(reviewId: String): Review? {
        return _reviews.value.find { it.id == reviewId }
    }

    /**
     * OBTENER RESEÑAS DE UN USUARIO
     * 
     * Retorna todas las reseñas creadas por un usuario específico.
     * Útil para mostrar el historial de reseñas de un usuario.
     * 
     * @param userId ID del usuario
     * @return Lista de reseñas del usuario
     */
    fun findByUserId(userId: String): List<Review> {
        return _reviews.value.filter { it.userID == userId }
    }

    /**
     * CALCULAR PROMEDIO DE CALIFICACIÓN
     * 
     * Calcula el promedio de rating para un lugar específico.
     * Útil para mostrar la calificación general de un lugar.
     * 
     * @param placeId ID del lugar
     * @return Promedio de rating o 0.0 si no hay reseñas
     */
    fun getAverageRating(placeId: String): Double {
        val placeReviews = findByPlaceId(placeId)
        if (placeReviews.isEmpty()) return 0.0
        return placeReviews.map { it.rating }.average()
    }

    /**
     * CONTAR RESEÑAS DE UN LUGAR
     * 
     * Cuenta el número total de reseñas para un lugar.
     * 
     * @param placeId ID del lugar
     * @return Número de reseñas
     */
    fun getReviewCount(placeId: String): Int {
        return findByPlaceId(placeId).size
    }

    /**
     * OBTENER RESEÑAS CON RESPUESTA
     * 
     * Retorna solo las reseñas que tienen respuesta del propietario.
     * Útil para análisis de engagement.
     * 
     * @param placeId ID del lugar (opcional)
     * @return Lista de reseñas con respuesta
     */
    fun getReviewsWithResponse(placeId: String? = null): List<Review> {
        val reviewsToFilter = if (placeId != null) {
            findByPlaceId(placeId)
        } else {
            _reviews.value
        }
        return reviewsToFilter.filter { it.ownerResponse != null }
    }

    /**
     * OBTENER RESEÑAS SIN RESPUESTA
     * 
     * Retorna reseñas que aún no han sido respondidas por el propietario.
     * Útil para propietarios que quieren gestionar sus respuestas pendientes.
     * 
     * @param placeId ID del lugar
     * @return Lista de reseñas sin respuesta
     */
    fun getPendingResponses(placeId: String): List<Review> {
        return findByPlaceId(placeId).filter { it.ownerResponse == null }
    }

    /**
     * OBTENER RESEÑAS RECIENTES
     * 
     * Retorna las reseñas más recientes de un lugar, ordenadas por fecha.
     * 
     * @param placeId ID del lugar
     * @param limit Número máximo de reseñas a retornar (por defecto 10)
     * @return Lista de reseñas recientes
     */
    fun getRecentReviews(placeId: String, limit: Int = 10): List<Review> {
        return findByPlaceId(placeId)
            .sortedByDescending { it.date }
            .take(limit)
    }

}