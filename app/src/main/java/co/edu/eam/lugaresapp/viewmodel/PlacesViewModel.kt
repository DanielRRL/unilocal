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

}