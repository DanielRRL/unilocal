package co.edu.eam.lugaresapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat

/**
 * Administrador de ubicación del usuario
 * Gestiona permisos y obtención de coordenadas GPS
 */
class UserLocationManager(private val context: Context) {
    
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null
    
    /**
     * Verifica si se tienen los permisos de ubicación
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Obtiene la última ubicación conocida
     * @return Location si está disponible, null si no
     */
    fun getLastKnownLocation(): Location? {
        if (!hasLocationPermission()) return null
        
        try {
            // Intentar obtener de GPS primero
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (gpsLocation != null) return gpsLocation
            
            // Si no hay GPS, intentar con red
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: SecurityException) {
            return null
        }
    }
    
    /**
     * Solicita actualizaciones de ubicación
     * @param onLocationChanged Callback que se llama cuando la ubicación cambia
     */
    fun requestLocationUpdates(onLocationChanged: (Location) -> Unit) {
        if (!hasLocationPermission()) return
        
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                onLocationChanged(location)
            }
            
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        
        try {
            // Solicitar actualizaciones cada 10 segundos o 10 metros
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000L, // 10 segundos
                10f,    // 10 metros
                locationListener!!
            )
        } catch (e: SecurityException) {
            // Permiso no concedido
        }
    }
    
    /**
     * Detiene las actualizaciones de ubicación
     */
    fun removeLocationUpdates() {
        locationListener?.let {
            try {
                locationManager.removeUpdates(it)
            } catch (e: SecurityException) {
                // Ignorar
            }
        }
        locationListener = null
    }
}
