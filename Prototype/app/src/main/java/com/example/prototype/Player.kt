package com.example.prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory

class Player(context: Context, private val screenX: Int,val screenY: Int) {

    // bitmap for player
    var bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.player)
    // sizing of player
    val width = screenX / 10f
    private val height = screenY / 10f

    // track position of player
    val position = RectF(
        screenX/2f,
        screenY/2f,
        screenX/2 + width,
        screenY/2f + height)

    // pixels per second speed
    // that the player can move
    private val speed  = 450f
    // jump-related values
    private val jumpForce = 40f
    private var velocity = jumpForce
    private val gravity = 2f

    // (This data is accessible using ClassName.propertyName)
    companion object {
        // Which ways can the player move
        const val stopped = 0
        const val up = 1
        const val down = 2
    }
    // Is the player moving
    var moving = stopped

    // size the bitmap to the screen resolution
    init {
        bitmap = Bitmap.createScaledBitmap(bitmap,
            width.toInt() ,
            height.toInt() ,
            false)
    }
    // This update method will be called from update in "View"
    // It determines if the player's
    // needs to move and changes the coordinates
    fun update(fps: Long) {
        // Move as long as it doesn't go out of range:
        val unit = screenX/5
        if (moving == up && position.top > unit) {
            position.top -= speed / fps
        }
        // move down, needs condition added.
        else if (moving == down && position.top < 2*unit){
            position.top += speed / fps
        }
        else if (moving == stopped){
            position.top = screenY/2f
            position.left = screenX/2f
        }
        // update position
        position.right = position.left + width
        position.bottom = position.top + height
    }
}