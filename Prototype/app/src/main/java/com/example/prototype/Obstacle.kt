package com.example.prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF

class Obstacle(context: Context, screenY: Int, private val speed: Float = 350f, heightModifier: Float = 20f) {

    val obstaclePosition = RectF()
    val left = 0

    private var heading = -1
    private val width = screenY / heightModifier
    private var height = screenY / heightModifier

    var obstacleIsActive = false

    fun startObstacle(startX: Float, startY: Float, direction: Int): Boolean {
        if (!obstacleIsActive) {
            obstaclePosition.left = startX
            obstaclePosition.top = startY
            obstaclePosition.right = obstaclePosition.left + width
            obstaclePosition.bottom = obstaclePosition.top + height
            heading = direction
            obstacleIsActive = true
            return true
        }
        return false
    }

    fun updateObstacle(fps: Long) {
        // Just move left
        if (heading == left) {
            obstaclePosition.left -= speed / fps
        }
        // Update the bottom position
        obstaclePosition.right = obstaclePosition.left + width
        obstaclePosition.bottom = obstaclePosition.top + height
    }
}