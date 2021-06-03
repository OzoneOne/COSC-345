package com.example.prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory

/**
 * TRACKS
 * this class needs to:
 *      control obstacles
 *          +start obstacles in a controlled position [ -]
 *          +move them left at a controlled speed [<- ]
 *          if they have left screen deactivate, add point [' ]
 */

class Tracks(context: Context, private val screenX: Int, val screenY: Int) {
    // bitmap for track
    var trackBitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.background
    )

    // sizing of obstacle
    val width = screenX / 20f
    private val height = screenY / 20f

    // size of track
    val trackWidth = screenX
    private val trackHeight = screenY

    // position of BR
    val trackPosition = RectF(
        0f,
        0f,
        screenX.toFloat(),
        screenY.toFloat()
    )

    // track position of obstacle
    val obstaclePosition = RectF(
        screenX.toFloat(),
        screenY / 2f,
        screenX - width,
        screenY.toFloat()
    )

    // This will hold the pixels per second speed
    // that the obstacle can move
    private val speed = 450f

    // (This data is accessible using ClassName.propertyName)
    companion object {
        // Which ways can the obstacle move
        const val left = 0
    }

    // Is the obstacle moving
    var moving = left

    // size the bitmap to the screen resolution
    init {
        trackBitmap = Bitmap.createScaledBitmap(
            trackBitmap,
            trackWidth.toInt(),
            trackHeight.toInt(),
            false
        )
    }
    ///

    // This update method will be called from update in "View"
    // It determines if the obstacle
    // needs to move and changes the coordinates
    fun update(fps: Long) {
        // Move
        if (moving == left) {
            obstaclePosition.left -= speed / fps
        }
    }

    fun tryStartObstacle(waves: Int): Boolean {
        var maxRandomNumber = 100
        if (waves == 2) {
            maxRandomNumber = 80
        } else if (waves == 3) {
            maxRandomNumber = 50
        }
        val randomNumber = (0..maxRandomNumber).random()
        return randomNumber == 0
    }
}
