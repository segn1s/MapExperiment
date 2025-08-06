package org.segn1s.mapexperiment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class WeatherTileOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var tileBitmap: Bitmap? = null
    private val paint = Paint().apply { alpha = 255 } // Полностью непрозрачный
    private var currentJob: Job? = null
    private var lastTileUrl: String? = null

    // Ваш API-ключ OpenWeatherMap
    private val apiKey = ""

    fun updateTile(layer: String, x: Int, y: Int, z: Int, force: Boolean = false) {
        val url = "https://tile.openweathermap.org/map/$layer/$z/$x/$y.png?appid=$apiKey"
        if (!force && url == lastTileUrl) return // Не загружаем повторно тот же тайл, если не force
        lastTileUrl = url
        currentJob?.cancel()
        currentJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                withContext(Dispatchers.Main) {
                    tileBitmap = bitmap
                    invalidate()
                }
            } catch (e: Exception) {
                // Не удалось загрузить тайл (например, нет интернета)
                withContext(Dispatchers.Main) {
                    tileBitmap = null
                    invalidate()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        tileBitmap?.let {
            // Рисуем на весь экран (можно доработать для точного позиционирования)
            canvas.drawBitmap(it, null, canvas.clipBounds, paint)
        }
    }
} 
