package co.edu.eam.lugaresapp.model

import java.time.LocalTime

data class Schedule (
    val day: String,
    val open: LocalTime,
    val close: LocalTime
)