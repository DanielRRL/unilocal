package co.edu.eam.lugaresapp.model

/**
 * MODELO DE HORARIO
 * 
 * Representa un horario de atención con día y horas.
 * Usa String para compatibilidad con Firebase Firestore.
 */
data class Schedule(
    val day: String = "",
    val open: String = "",  // Formato: "HH:mm" (ej: "08:00")
    val close: String = ""  // Formato: "HH:mm" (ej: "18:00")
)