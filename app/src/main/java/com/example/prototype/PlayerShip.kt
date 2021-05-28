package com.example.prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory

class PlayerShip(context: Context,
                 private val screenX: Int,
                 private val screenY: Int) {

    // The player ship will be represented by a Bitmap
    var bitmap: Bitmap = BitmapFactory.decodeResource(
            context.resources, R.drawable.playership)

    // How wide and high our ship will be
    val width = screenX / 20f
    private val height = screenY / 20f

    // This keeps track of where the ship is
    val position = RectF(
            screenX / 2f,
            screenY - height,
            screenX / 2 + width,
            screenY.toFloat())

    // This will hold the pixels per second speed that the ship will move
    private val speed = 450f

    // jump related values
    private val jumpForce = -500f
    private var velocity = 0f
    private val gravity = 5f

    // This data is accessible using ClassName.propertyName
    companion object {
        // Which ways can the ship move
        const val stopped = 0
        const val left = 1
        const val right = 2

        //jumping related values
        const val noJump = 0
        const val jumping = 1

        const val grounded = 0
        const val floating = 1
    }

    // Is the ship moving and in which direction
    // Start off stopped
    var moving = stopped

    var jumpState = noJump
    var touchGround = grounded


    init {
        // stretch the bitmap to a size
        // appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                width.toInt(),
                height.toInt(),
                false)
    }

    // This update method will be called from update in
    // KotlinInvadersView It determines if the player's
    // ship needs to move and changes the coordinates

    /*
    fun update(fps: Long) {
        // Move as long as it doesn't try and leave the screen
        if (moving == left && position.left > 0) {
            position.left -= speed / fps
        }

        else if (moving == right && position.left < screenX - width) {
            position.left += speed / fps
        }

        position.right = position.left + width
    }

     */


    // jump on touch

    /*
    fun update(fps: Long) {
        if(jumping == jumping && position.bottom >= 0) {
            position.top += velocity
            position.bottom += velocity
            velocity += gravity
        }

        if(position.bottom < 0f) {
            jumping = grounded
            velocity = jumpForce

        }
    }

     */
    // new jump physics, adapted from https://www.javacodegeeks.com/2013/03/android-game-development-with-libgdx-jumping-gravity-and-improved-movement-part-3.html
    /*
    fun update(fps: Long) {
        if(jumping == grounded){
            jumping = jumping
            velocity = jumpForce
        } else {

        }

    }

     */

    /**
     *  This method is run every frame, updating the position of the
     *  player object on the screen, calculating collisions, and
     *  determining whether the player can jump at the current moment
     */

    fun update(fps: Long) {
        if (jumpState == jumping) {
            if (touchGround == grounded) {
                velocity = jumpForce
                touchGround = floating
            }
        }

        if (!checkCollisions()) {
            position.top += velocity / fps
            position.bottom += velocity / fps
            velocity += gravity
        } else {
            velocity = 0f
            jumpState = noJump
            touchGround = grounded
        }




    }


    fun checkCollisions(): Boolean {
        return position.bottom > screenY

    }

        /*
        var currentVelocity = gravityShift(velocity, this.gravity)
        position.top += currentVelocity
        position.bottom += currentVelocity

        return currentVelocity

         */

    /*
    private fun gravityShift(velocity: Float, gravity: Float): Float {
        var newVelocity: Float = velocity
        newVelocity += gravity

        return newVelocity



    } */
}