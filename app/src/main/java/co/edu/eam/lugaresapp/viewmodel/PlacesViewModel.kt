package co.edu.eam.lugaresapp.viewmodel

import androidx.lifecycle.ViewModel
import co.edu.eam.lugaresapp.model.Location
import co.edu.eam.lugaresapp.model.ModerationRecord
import co.edu.eam.lugaresapp.model.Place
import co.edu.eam.lugaresapp.model.PlaceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * VIEWMODEL DE LUGARES uniLocal
 * 
 * Este ViewModel maneja toda la lógica de negocio relacionada con lugares y moderación.
 * 
 * RESPONSABILIDADES:
 * - Gestión CRUD de lugares
 * - Sistema de moderación (aprobar/rechazar lugares)
 * - Filtrado de lugares pendientes de aprobación
 * - Registro de acciones de moderación
 * - Búsqueda y filtrado de lugares
 * 
 * PATRÓN ARQUITECTÓNICO:
 * - StateFlow para exposición reactiva de datos
 * - Inmutabilidad mediante copy() y reasignación
 * - Sin efectos secundarios fuera del ViewModel
 * - Persistencia en memoria (temporal)
 */
class PlacesViewModel: ViewModel() {

    /**
     * Estado privado de lugares
     * Lista mutable que contiene todos los lugares de la aplicación
     */
    private val _places = MutableStateFlow(emptyList<Place>())
    
    /**
     * Estado público de lugares para observación desde la UI
     */
    val places: StateFlow<List<Place>> = _places.asStateFlow()

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
     * Para desarrollo, usamos una ubicación fija cercana a los lugares de prueba.
     */
    val defaultUserLocation = Location(1.23, 2.34)

    init {
        loadPlaces()
    }

    /**
     * CARGA DE LUGARES INICIALES
     * 
     * Carga datos de prueba en la aplicación.
     * En producción, estos datos vendrían de una base de datos o API.
     * 
     * NOTA: Los lugares de prueba se crean con approved=false por defecto,
     * simulando que necesitan aprobación de un administrador.
     */
    fun loadPlaces(){

        _places.value = listOf(
            Place(
                id = "1",
                title = "Restaurante El paisa",
                description = "El mejor restaurante paisa",
                address = "Cra 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://elbalconpaisa.com/images/about-img-1.png"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.RESTAURANT,
                schedules = listOf(),
                approved = true  // Pre-aprobado para testing
            ),
            Place(
                id = "2",
                title = "Bar test 1",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
                // approved = false por defecto (pendiente de moderación)
            ),
            Place(
                id = "3",
                title = "Bar test 2",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "4",
                title = "Bar test 3",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "5",
                title = "Bar test 4",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            ),
            Place(
                id = "6",
                title = "Bar test 5",
                description = "Un bar test",
                address = "Calle 12 # 12 - 12",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn0.uncomo.com/es/posts/6/8/4/como_gestionar_un_bar_22486_orig.jpg"),
                phones = listOf("3123123123", "3123123123"),
                type = PlaceType.BAR,
                schedules = listOf()
            )
        )

    }

    // ==================== OPERACIONES CRUD ====================

    /**
     * AGREGAR NUEVO LUGAR
     * 
     * Añade un nuevo lugar a la lista de lugares.
     * El lugar se añade con el estado que tenga configurado (approved true/false).
     * 
     * @param place Lugar a agregar
     */
    fun addPlace(place: Place) {
        _places.value = _places.value + place
    }

    /**
     * ELIMINAR LUGAR
     * 
     * Elimina un lugar de la lista por su ID.
     * Utiliza filter para crear una nueva lista sin el lugar especificado.
     * 
     * @param placeId ID del lugar a eliminar
     */
    fun deletePlace(placeId: String) {
        _places.value = _places.value.filter { it.id != placeId }
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
     * APROBAR LUGAR
     * 
     * Marca un lugar como aprobado y registra la acción de moderación.
     * 
     * FUNCIONAMIENTO:
     * 1. Actualiza el lugar estableciendo approved = true
     * 2. Crea un registro de moderación con action = "APPROVE"
     * 3. Añade el registro al historial de moderación
     * 
     * @param placeId ID del lugar a aprobar
     * @param moderatorId ID del administrador que aprueba
     */
    fun approvePlace(placeId: String, moderatorId: String) {
        // Actualizar el lugar a approved = true
        _places.value = _places.value.map { place ->
            if (place.id == placeId) {
                place.copy(approved = true)
            } else {
                place
            }
        }
        
        // Registrar la acción de moderación
        addModerationRecord(
            placeId = placeId,
            moderatorId = moderatorId,
            action = "APPROVE",
            reason = null
        )
    }

    /**
     * RECHAZAR LUGAR
     * 
     * Rechaza un lugar y registra la acción con una razón opcional.
     * 
     * FUNCIONAMIENTO:
     * 1. Establece approved = false en el lugar
     * 2. Crea un registro de moderación con action = "REJECT"
     * 3. Incluye la razón del rechazo si fue proporcionada
     * 
     * NOTA: En versiones futuras, esta función podría eliminar el lugar
     * en lugar de solo marcarlo como no aprobado.
     * 
     * @param placeId ID del lugar a rechazar
     * @param moderatorId ID del administrador que rechaza
     * @param reason Razón opcional del rechazo
     */
    fun rejectPlace(placeId: String, moderatorId: String, reason: String? = null) {
        // Marcar como no aprobado (en el futuro podría eliminarse)
        _places.value = _places.value.map { place ->
            if (place.id == placeId) {
                place.copy(approved = false)
            } else {
                place
            }
        }
        
        // Registrar la acción de moderación con razón
        addModerationRecord(
            placeId = placeId,
            moderatorId = moderatorId,
            action = "REJECT",
            reason = reason
        )
    }

    /**
     * AGREGAR REGISTRO DE MODERACIÓN
     * 
     * Función privada que añade un registro al historial de moderación.
     * Genera automáticamente el ID y timestamp del registro.
     * 
     * @param placeId ID del lugar moderado
     * @param moderatorId ID del moderador
     * @param action Acción realizada (APPROVE, REJECT, etc.)
     * @param reason Razón opcional de la acción
     */
    private fun addModerationRecord(
        placeId: String,
        moderatorId: String,
        action: String,
        reason: String?
    ) {
        val record = ModerationRecord(
            id = UUID.randomUUID().toString(),
            placeId = placeId,
            moderatorId = moderatorId,
            action = action,
            timestamp = System.currentTimeMillis(),
            reason = reason
        )
        
        _moderationRecords.value = _moderationRecords.value + record
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
                .filter { (_, distance) -> distance <= maxDistance }
                .sortedBy { (_, distance) -> distance }
                .map { (place, _) -> place }
        }
        
        return result
    }

}


