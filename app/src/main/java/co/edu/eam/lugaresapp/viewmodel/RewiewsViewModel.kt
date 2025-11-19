package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.eam.lugaresapp.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * VIEWMODEL DE RESEÑAS uniLocal CON FIREBASE
 * 
 * Este ViewModel maneja toda la lógica de negocio relacionada con reseñas y valoraciones de lugares.
 * 
 * RESPONSABILIDADES:
 * - Gestión CRUD de reseñas en Firebase Firestore
 * - Filtrado de reseñas por lugar
 * - Sistema de respuestas de propietarios a reseñas
 * - Exposición reactiva de datos mediante StateFlow
 * - Sincronización en tiempo real con Firebase
 * 
 * PATRÓN ARQUITECTÓNICO:
 * - StateFlow para exposición reactiva de datos
 * - Firebase Firestore para persistencia
 * - SnapshotListener para actualizaciones en tiempo real
 * - viewModelScope para manejo de coroutines
 * 
 * CASOS DE USO:
 * - Usuarios pueden agregar reseñas a lugares
 * - Propietarios pueden responder a reseñas
 * - Administradores pueden eliminar reseñas inapropiadas
 * - Consulta de reseñas por lugar específico
 */
class RewiewsViewModel: ViewModel() {

    // Firebase Firestore instance
    private val db = FirebaseFirestore.getInstance()

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
        loadReviews()
    }

    /**
     * CARGA DE RESEÑAS DESDE FIREBASE
     * 
     * Establece un SnapshotListener para sincronización en tiempo real.
     * Cualquier cambio en Firebase se reflejará automáticamente en la UI.
     */
    private fun loadReviews() {
        viewModelScope.launch {
            try {
                db.collection("reviews")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            android.util.Log.e("RewiewsViewModel", "Error al cargar reseñas: ${error.message}", error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val reviewsList = snapshot.documents.mapNotNull { document ->
                                document.toObject(Review::class.java)?.apply {
                                    val idField = this::class.java.getDeclaredField("id")
                                    idField.isAccessible = true
                                    idField.set(this, document.id)
                                }
                            }
                            _reviews.value = reviewsList
                            android.util.Log.d("RewiewsViewModel", "Reseñas cargadas: ${reviewsList.size}")
                        }
                    }
            } catch (e: Exception) {
                android.util.Log.e("RewiewsViewModel", "Error al configurar listener de reseñas: ${e.message}", e)
            }
        }
    }

    // ==================== OPERACIONES CRUD ====================

    /**
     * AGREGAR NUEVA RESEÑA A FIREBASE
     * 
     * Añade una nueva reseña a Firebase Firestore.
     * El SnapshotListener actualizará automáticamente el StateFlow.
     * 
     * @param review Reseña a agregar
     */
    fun addReview(review: Review) {
        viewModelScope.launch {
            try {
                addReviewFirebase(review)
                android.util.Log.d("RewiewsViewModel", "Reseña agregada: ${review.id}")
            } catch (e: Exception) {
                android.util.Log.e("RewiewsViewModel", "Error al agregar reseña: ${e.message}", e)
            }
        }
    }

    /**
     * Función suspendida para agregar reseña a Firebase
     */
    private suspend fun addReviewFirebase(review: Review) {
        db.collection("reviews")
            .document(review.id)
            .set(review)
            .await()
    }

    /**
     * BUSCAR RESEÑAS POR LUGAR
     * 
     * Retorna todas las reseñas asociadas a un lugar específico.
     * 
     * @param placeId ID del lugar del cual obtener reseñas
     * @return Lista de reseñas del lugar especificado
     */
    fun findByPlaceId(placeId: String): List<Review> {
        return _reviews.value.filter { it.placeID == placeId }
    }

    /**
     * RESPONDER A UNA RESEÑA EN FIREBASE
     * 
     * Permite al propietario de un lugar responder a una reseña específica.
     * Actualiza el campo ownerResponse en Firebase.
     * 
     * @param reviewId ID de la reseña a la que se responde
     * @param response Texto de la respuesta del propietario
     */
    fun replyToReview(reviewId: String, response: String) {
        viewModelScope.launch {
            try {
                replyToReviewFirebase(reviewId, response)
                android.util.Log.d("RewiewsViewModel", "Respuesta agregada a reseña: $reviewId")
            } catch (e: Exception) {
                android.util.Log.e("RewiewsViewModel", "Error al agregar respuesta: ${e.message}", e)
            }
        }
    }

    /**
     * Función suspendida para actualizar respuesta en Firebase
     */
    private suspend fun replyToReviewFirebase(reviewId: String, response: String) {
        db.collection("reviews")
            .document(reviewId)
            .update("ownerResponse", response)
            .await()
    }
    
    /**
     * AGREGAR RESPUESTA DE PROPIETARIO
     * 
     * Alias para replyToReview. Permite al propietario responder a una reseña.
     * 
     * @param reviewId ID de la reseña a la que se responde
     * @param response Texto de la respuesta del propietario
     */
    fun addOwnerResponse(reviewId: String, response: String) {
        replyToReview(reviewId, response)
    }

    /**
     * ELIMINAR RESEÑA DE FIREBASE
     * 
     * Elimina una reseña de Firebase Firestore.
     * Útil para moderación de contenido inapropiado.
     * 
     * @param reviewId ID de la reseña a eliminar
     */
    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            try {
                deleteReviewFirebase(reviewId)
                android.util.Log.d("RewiewsViewModel", "Reseña eliminada: $reviewId")
            } catch (e: Exception) {
                android.util.Log.e("RewiewsViewModel", "Error al eliminar reseña: ${e.message}", e)
            }
        }
    }

    /**
     * Función suspendida para eliminar reseña de Firebase
     */
    private suspend fun deleteReviewFirebase(reviewId: String) {
        db.collection("reviews")
            .document(reviewId)
            .delete()
            .await()
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