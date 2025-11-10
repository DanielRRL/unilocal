package co.edu.eam.lugaresapp.model

/**
 * TIPOS DE LUGARES
 * 
 * Enum que define las categorías disponibles para clasificar lugares.
 * Cada tipo tiene un nombre legible que se muestra en la UI.
 * 
 * CATEGORÍAS DISPONIBLES:
 * - Restaurante: Lugares de comida formal
 * - Cafetería: Cafés y lugares de bebidas/snacks
 * - Comidas Rápidas: Fast food, comida para llevar
 * - Bar: Bares, pubs, discotecas
 * - Hotel: Hoteles, hostales, alojamiento
 * - Parque: Parques, zonas verdes, recreación
 * - Museo: Museos, galerías, centros culturales
 * - Universidad: Instituciones educativas, universidades, colegios
 * - Tienda: Comercios, tiendas, centros comerciales
 * - Otro: Categoría genérica para otros tipos
 */
enum class PlaceType(
    val displayName: String
) {
    RESTAURANT("Restaurante"),
    CAFE("Cafetería"),
    FAST_FOOD("Comidas Rápidas"),
    BAR("Bar"),
    HOTEL("Hotel"),
    PARK("Parque"),
    MUSEUM("Museo"),
    UNIVERSITY("Universidad"),
    SHOPPING("Tienda"),
    OTHER("Otro")
}