package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.ModerationRecord
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import co.edu.eam.lugaresapp.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * VIEWMODEL DE LUGARES uniLocal
 * 
 * Este ViewModel maneja toda la lógica de negocio relacionada con lugares y moderación.
 * Integra Firebase Firestore para persistencia en tiempo real.
 * 
 * RESPONSABILIDADES:
 * - Gestión CRUD de lugares con Firebase
 * - Sistema de moderación (aprobar/rechazar lugares)
 * - Filtrado de lugares pendientes de aprobación
 * - Registro de acciones de moderación
 * - Búsqueda y filtrado de lugares
 * - Sincronización en tiempo real con Firebase
 * 
 * PATRÓN ARQUITECTÓNICO:
 * - StateFlow para exposición reactiva de datos
 * - RequestResult para estados de operaciones
 * - Coroutines para operaciones asíncronas
 * - Firebase Firestore como backend
 */
class PlacesViewModel: ViewModel() {

    /**
     * Estado privado de lugares
     * Lista mutable que contiene todos los lugares sincronizados con Firebase
     */
    private val _places = MutableStateFlow(emptyList<Place>())
    
    /**
     * Estado público de lugares para observación desde la UI
     */
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    /**
     * Estado de resultado de operaciones (crear, aprobar, rechazar)
     */
    private val _reportResult = MutableStateFlow<RequestResult?>(null)
    val reportResult: StateFlow<RequestResult?> = _reportResult.asStateFlow()

    /**
     * Estado privado de registros de moderación
     * Mantiene el historial de todas las acciones de moderación realizadas
     */
    private val _moderationRecords = MutableStateFlow(emptyList<ModerationRecord>())
    
    /**
     * Estado público de registros de moderación
     */
    val moderationRecords: StateFlow<List<ModerationRecord>> = _moderationRecords.asStateFlow()

    /**
     * UBICACIÓN SIMULADA DEL USUARIO
     * 
     * En producción, esta ubicación vendría del GPS o de la ciudad del usuario.
     * Para desarrollo, usamos una ubicación fija cercana a Armenia, Quindío.
     */
    val defaultUserLocation = Location(4.4687891, -75.6491181)

    /**
     * Instancia de Firebase Firestore
     */
    val db = Firebase.firestore

    init {
        loadPlaces()
        loadModerationRecords()
    }

    /**
     * CARGA DE LUGARES DESDE FIREBASE FIRESTORE
     * 
     * Carga todos los lugares existentes desde Firebase en tiempo real.
     * 
     * FUNCIONAMIENTO:
     * - Se ejecuta en una coroutine (viewModelScope) para operación asíncrona
     * - Consulta la colección "places" en Firestore
     * - Usa addSnapshotListener para escuchar cambios en tiempo real
     * - Convierte los documentos de Firebase a objetos Place
     * - Actualiza el StateFlow _places con la lista obtenida
     * - Maneja errores con logging
     */
    private fun loadPlaces(){
        viewModelScope.launch {
            try {
                db.collection("places")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            android.util.Log.e("PlacesViewModel", "Error al cargar lugares: ${error.message}", error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val placesList = snapshot.documents.mapNotNull { document ->
                                document.toObject(Place::class.java)?.apply {
                                    this::class.java.getDeclaredField("id").apply {
                                        isAccessible = true
                                        set(this@apply, document.id)
                                    }
                                }
                            }
                            _places.value = placesList
                            android.util.Log.d("PlacesViewModel", "Lugares cargados: ${placesList.size}")
                        }
                    }
            } catch (e: Exception) {
                android.util.Log.e("PlacesViewModel", "Error al configurar listener de lugares: ${e.message}", e)
            }
        }
    }

    /**
     * CARGA DE REGISTROS DE MODERACIÓN DESDE FIREBASE
     */
    private fun loadModerationRecords(){
        viewModelScope.launch {
            try {
                db.collection("moderation_records")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            android.util.Log.e("PlacesViewModel", "Error al cargar registros: ${error.message}", error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val recordsList = snapshot.documents.mapNotNull { document ->
                                document.toObject(ModerationRecord::class.java)?.apply {
                                    this::class.java.getDeclaredField("id").apply {
                                        isAccessible = true
                                        set(this@apply, document.id)
                                    }
                                }
                            }
                            _moderationRecords.value = recordsList
                            android.util.Log.d("PlacesViewModel", "Registros cargados: ${recordsList.size}")
                        }
                    }
            } catch (e: Exception) {
                android.util.Log.e("PlacesViewModel", "Error al configurar listener de moderación: ${e.message}", e)
            }
        }
    }

    // ==================== OPERACIONES CRUD ====================

    /**
     * AGREGAR NUEVO LUGAR CON FIREBASE
     * 
     * Guarda un nuevo lugar en Firebase Firestore de forma asíncrona.
     * 
     * FUNCIONAMIENTO:
     * - Ejecuta en viewModelScope para no bloquear UI
     * - Actualiza _reportResult con estados (Loading/Success/Failure)
     * - Llama a addPlaceFirebase() para guardar en Firestore
     * - El SnapshotListener actualiza automáticamente _places
     */
    fun addPlace(place: Place) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            try {
                addPlaceFirebase(place)
                _reportResult.value = RequestResult.Success("Lugar creado. Pendiente de aprobación")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al crear lugar")
                android.util.Log.e("PlacesViewModel", "Error al crear lugar: ${e.message}", e)
            }
        }
    }

    /**
     * GUARDAR LUGAR EN FIREBASE FIRESTORE
     */
    private suspend fun addPlaceFirebase(place: Place) {
        db.collection("places")
            .document(place.id)
            .set(place)
            .await()
    }

    /**
     * ELIMINAR LUGAR CON FIREBASE
     */
    fun deletePlace(placeId: String) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            try {
                deletePlaceFirebase(placeId)
                _reportResult.value = RequestResult.Success("Lugar eliminado correctamente")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al eliminar lugar")
                android.util.Log.e("PlacesViewModel", "Error al eliminar lugar: ${e.message}", e)
            }
        }
    }

    /**
     * ELIMINAR LUGAR DE FIREBASE FIRESTORE
     */
    private suspend fun deletePlaceFirebase(placeId: String) {
        db.collection("places")
            .document(placeId)
            .delete()
            .await()
    }

    /**
     * CREAR LUGAR (Legacy)
     * 
     * Función mantenida por compatibilidad con código existente.
     * Internamente llama a addPlace.
     * 
     * @param place Lugar a crear
     */
    fun create(place: Place){
        addPlace(place)
    }

    // ==================== OPERACIONES DE MODERACIÓN ====================

    /**
     * OBTENER LUGARES PENDIENTES DE APROBACIÓN
     * 
     * Retorna todos los lugares que aún no han sido aprobados por un moderador.
     * Útil para pantallas de moderación de administradores.
     * 
     * @return Lista de lugares no aprobados
     */
    fun getPendingPlaces(): List<Place> {
        return _places.value.filter { !it.approved }
    }
    
    /**
     * OBTENER LUGARES POR PROPIETARIO
     * 
     * Retorna todos los lugares creados por un usuario específico.
     * Útil para mostrar los lugares de un usuario en su perfil.
     * 
     * @param ownerId ID del propietario/usuario
     * @return Lista de lugares del propietario
     */
    fun getPlacesByOwner(ownerId: String): List<Place> {
        return _places.value.filter { it.ownerId == ownerId }
    }

    /**
     * APROBAR LUGAR CON FIREBASE
     * 
     * Marca un lugar como aprobado y registra la acción de moderación.
     * 
     * FUNCIONAMIENTO:
     * 1. Actualiza el lugar en Firebase estableciendo approved = true
     * 2. Crea un registro de moderación con action = "APPROVE"
     * 3. Guarda el registro en Firebase
     * 4. El SnapshotListener sincroniza automáticamente
     */
    fun approvePlace(placeId: String, moderatorId: String) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            try {
                // Actualizar lugar en Firebase
                approvePlaceFirebase(placeId)
                
                // Registrar acción de moderación
                addModerationRecord(
                    placeId = placeId,
                    moderatorId = moderatorId,
                    action = "APPROVE",
                    reason = null
                )
                
                _reportResult.value = RequestResult.Success("Lugar aprobado correctamente")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al aprobar lugar")
                android.util.Log.e("PlacesViewModel", "Error al aprobar lugar: ${e.message}", e)
            }
        }
    }

    /**
     * ACTUALIZAR ESTADO DE APROBACIÓN EN FIREBASE
     */
    private suspend fun approvePlaceFirebase(placeId: String) {
        db.collection("places")
            .document(placeId)
            .update("approved", true)
            .await()
    }

    /**
     * RECHAZAR LUGAR CON FIREBASE
     * 
     * Rechaza un lugar y registra la acción con una razón opcional.
     */
    fun rejectPlace(placeId: String, moderatorId: String, reason: String? = null) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            try {
                // Actualizar lugar en Firebase
                rejectPlaceFirebase(placeId)
                
                // Registrar acción de moderación
                addModerationRecord(
                    placeId = placeId,
                    moderatorId = moderatorId,
                    action = "REJECT",
                    reason = reason
                )
                
                _reportResult.value = RequestResult.Success("Lugar rechazado")
            } catch (e: Exception) {
                _reportResult.value = RequestResult.Failure(errorMessage = e.message ?: "Error al rechazar lugar")
                android.util.Log.e("PlacesViewModel", "Error al rechazar lugar: ${e.message}", e)
            }
        }
    }

    /**
     * MARCAR LUGAR COMO NO APROBADO EN FIREBASE
     */
    private suspend fun rejectPlaceFirebase(placeId: String) {
        db.collection("places")
            .document(placeId)
            .update("approved", false)
            .await()
    }

    /**
     * AGREGAR REGISTRO DE MODERACIÓN CON FIREBASE
     * 
     * Función privada que añade un registro al historial de moderación en Firebase.
     * Genera automáticamente el ID y timestamp del registro.
     */
    private fun addModerationRecord(
        placeId: String,
        moderatorId: String,
        action: String,
        reason: String?
    ) {
        viewModelScope.launch {
            try {
                val record = ModerationRecord(
                    id = UUID.randomUUID().toString(),
                    placeId = placeId,
                    moderatorId = moderatorId,
                    action = action,
                    timestamp = System.currentTimeMillis(),
                    reason = reason
                )
                
                addModerationRecordFirebase(record)
            } catch (e: Exception) {
                android.util.Log.e("PlacesViewModel", "Error al registrar moderación: ${e.message}", e)
            }
        }
    }

    /**
     * GUARDAR REGISTRO DE MODERACIÓN EN FIREBASE
     */
    private suspend fun addModerationRecordFirebase(record: ModerationRecord) {
        db.collection("moderation_records")
            .document(record.id)
            .set(record)
            .await()
    }

    // ==================== OPERACIONES DE BÚSQUEDA Y FILTRADO ====================

    /**
     * BUSCAR LUGAR POR ID
     * 
     * @param id ID del lugar a buscar
     * @return Lugar encontrado o null si no existe
     */
    fun findById(id: String): Place?{
        return _places.value.find { it.id == id }
    }

    /**
     * BUSCAR LUGARES POR TIPO
     * 
     * @param type Tipo de lugar (RESTAURANT, BAR, etc.)
     * @return Lista de lugares del tipo especificado
     */
    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    /**
     * BUSCAR LUGARES POR NOMBRE
     * 
     * Búsqueda case-sensitive que busca coincidencias parciales en el título.
     * 
     * @param name Texto a buscar en el título
     * @return Lista de lugares que contienen el texto en su título
     */
    fun findByName(name: String): List<Place>{
        return _places.value.filter { it.title.contains(name) }
    }

    /**
     * OBTENER LUGARES APROBADOS
     * 
     * Retorna solo los lugares que han sido aprobados por moderadores.
     * Útil para mostrar contenido público a usuarios regulares.
     * 
     * @return Lista de lugares aprobados
     */
    fun getApprovedPlaces(): List<Place> {
        return _places.value.filter { it.approved }
    }

    // ==================== CÁLCULO DE DISTANCIA ====================

    /**
     * CALCULAR DISTANCIA HAVERSINE
     * 
     * Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine.
     * Esta es la fórmula estándar para calcular distancias sobre la superficie de una esfera.
     * 
     * FÓRMULA HAVERSINE:
     * a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
     * c = 2 * atan2(√a, √(1−a))
     * d = R * c (donde R = radio de la Tierra)
     * 
     * @param lat1 Latitud del primer punto en grados
     * @param lon1 Longitud del primer punto en grados
     * @param lat2 Latitud del segundo punto en grados
     * @param lon2 Longitud del segundo punto en grados
     * @return Distancia en metros entre los dos puntos
     */
    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0 // Radio de la Tierra en kilómetros
        
        // Convertir grados a radianes
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        
        // Aplicar fórmula de Haversine
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) *
                Math.cos(lat1Rad) * Math.cos(lat2Rad)
        
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        
        // Retornar distancia en metros
        return earthRadiusKm * c * 1000
    }

    /**
     * FILTRAR LUGARES POR DISTANCIA
     * 
     * Retorna lugares que están dentro del radio especificado desde la ubicación del usuario.
     * Útil para búsquedas del tipo "lugares cerca de mí".
     * 
     * ALGORITMO:
     * 1. Para cada lugar, calcular distancia usando Haversine
     * 2. Filtrar lugares con distancia <= maxMeters
     * 3. Ordenar por distancia ascendente (más cercanos primero)
     * 
     * @param userLat Latitud del usuario
     * @param userLon Longitud del usuario
     * @param maxMeters Radio máximo en metros (por defecto 5000m = 5km)
     * @return Lista de lugares dentro del radio, ordenados por distancia
     */
    fun findByDistance(
        userLat: Double, 
        userLon: Double, 
        maxMeters: Double = 5000.0
    ): List<Place> {
        return _places.value
            .map { place -> 
                val distance = haversine(userLat, userLon, place.location.latitude, place.location.longitude)
                Pair(place, distance)
            }
            .filter { (_, distance) -> distance <= maxMeters }
            .sortedBy { (_, distance) -> distance }
            .map { (place, _) -> place }
    }

    /**
     * BÚSQUEDA COMBINADA CON FILTROS
     * 
     * Aplica múltiples filtros de forma combinada para búsquedas avanzadas.
     * 
     * ORDEN DE APLICACIÓN:
     * 1. Filtro de aprobación (solo aprobados)
     * 2. Filtro de nombre (si searchText no está vacío)
     * 3. Filtro de tipo (si selectedType no es null)
     * 4. Filtro de distancia (si maxDistance > 0)
     * 
     * @param searchText Texto para buscar en el nombre (case-insensitive)
     * @param selectedType Tipo de lugar a filtrar (null = todos los tipos)
     * @param userLat Latitud del usuario
     * @param userLon Longitud del usuario
     * @param maxDistance Distancia máxima en metros (0 = sin filtro de distancia)
     * @return Lista de lugares que cumplen todos los filtros activos
     */
    fun searchWithFilters(
        searchText: String = "",
        selectedType: PlaceType? = null,
        userLat: Double = defaultUserLocation.latitude,
        userLon: Double = defaultUserLocation.longitude,
        maxDistance: Double = 0.0
    ): List<Place> {
        var result = _places.value.filter { it.approved } // Solo lugares aprobados
        
        // Filtrar por nombre (case-insensitive)
        if (searchText.isNotBlank()) {
            result = result.filter { 
                it.title.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
            }
        }
        
        // Filtrar por tipo
        if (selectedType != null) {
            result = result.filter { it.type == selectedType }
        }
        
        // Filtrar por distancia
        if (maxDistance > 0) {
            result = result
                .map { place -> 
                    val distance = haversine(userLat, userLon, place.location.latitude, place.location.longitude)
                    Pair(place, distance)
                }
                .filter { (place: Place, distance: Double) -> distance <= maxDistance }
                .sortedBy { (place: Place, distance: Double) -> distance }
                .map { (place: Place, distance: Double) -> place }
        }
        
        return result
    }

    /**
     * RESETEAR RESULTADO DE OPERACIÓN
     */
    fun resetOperationResult() {
        _reportResult.value = null
    }

}


