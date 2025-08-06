package org.segn1s.mapexperiment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.map.MapController
import java.util.Locale
import android.widget.FrameLayout
import org.segn1s.mapexperiment.WeatherTileOverlayView
import android.widget.Toast
import com.tomtom.sdk.map.display.camera.CameraPosition

class MainActivity : AppCompatActivity() {
    private lateinit var mapFragment: MapFragment
    private lateinit var tomTomMap: TomTomMap
    private val apiKey = BuildConfig.TOMTOM_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initTomTomMap()
    }

    private fun initTomTomMap() {
        val mapOptions = MapOptions(mapKey = apiKey, locale = Locale("ru"))
        mapFragment = MapFragment.newInstance(mapOptions)
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_container, mapFragment)
            .commit()
        mapFragment.getMapAsync { map ->
            tomTomMap = map
            // Устанавливаем русский язык для карты
            // val mapController = (tomTomMap as? MapController)
            // mapController?.setLanguage(Locale("ru"))

            // Получаем ссылку на WeatherTileOverlayView
            val weatherOverlay = findViewById<WeatherTileOverlayView>(R.id.weather_overlay)

            // Пример: загружаем тайл clouds_new/5/17/10
            weatherOverlay.updateTile("clouds_new", 17, 10, 5)

            // Пример: обновлять тайл при изменении положения камеры
            tomTomMap.addCameraChangeListener { cameraPosition ->
                val z = cameraPosition.zoom.toInt().coerceIn(0, 19)
                if (z >= 3) {
                    val (x, y) = latLngToTileXY(cameraPosition.target.latitude, cameraPosition.target.longitude, z)
                    weatherOverlay.visibility = android.view.View.VISIBLE
                    weatherOverlay.updateTile("clouds_new", x, y, z)
                } else {
                    weatherOverlay.visibility = android.view.View.GONE
                }
            }
        }
    }

    // Переводит координаты широты/долготы в tile x, y для заданного zoom
    private fun latLngToTileXY(lat: Double, lon: Double, zoom: Int): Pair<Int, Int> {
        val latRad = Math.toRadians(lat)
        val n = 1 shl zoom
        val x = ((lon + 180.0) / 360.0 * n).toInt()
        val y = ((1.0 - Math.log(Math.tan(latRad) + 1 / Math.cos(latRad)) / Math.PI) / 2.0 * n).toInt()
        return x to y
    }
}