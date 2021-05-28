package com.example.kotlininvaders

import android.graphics.RectF

class Obstacle(screenX: Int, screenY: Int) {

    var isVisible = true

    private val width = screenX / 180f
    private val height = screenY / 80f
    private val speed = 200f

    private var currentX = screenX

    fun update(fps: Long){
        position.left -= speed / fps
        position.right -= speed / fps

    }

    val position = RectF(
            screenX / 2f,
            screenY-height,
            screenX/2 + width,
            screenY.toFloat()
    )
}