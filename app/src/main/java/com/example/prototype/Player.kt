package com.example.prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory

class Player(context: Context, private val screenX: Int, private val screenY: Int) {

    // bitmap to represent the player character
    var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player_temp)

    // width and height of player character (used in init to stretch bitmap)
    private val width = 80f
    private val height = 80f

    // set the default position
    val position = RectF(
        screenX / 2f - (width / 2),
        800f,
        screenX / 2 + (width / 2),
        screenY.toFloat())

    // pixels per second the player can move
    //private val speed = 450f

    // jump-related values
    private val jumpForce = 40f
    private var velocity = jumpForce
    private val gravity = 2f

    // data accessible using ClassName.propertyName
    companion object {
        const val grounded = 0
        const val jumping = 1
    }

    var jumpState = grounded

    // this method is called when the class is initialized
    init {
        // stretch the bitmap to set size
        bitmap = Bitmap.createScaledBitmap(bitmap, width.toInt(), height.toInt(),false)
    }

    // this method is called every frame
    fun update(fps: Long) {
        // jump in an arc
        if (jumpState == jumping && position.top <= 800) {
            position.top -= velocity
            velocity -= gravity
        }

        // reset position after jump
        if (position.top > 800) {
            jumpState = grounded
            position.top = 800f
        }
    }
}