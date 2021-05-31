package com.example.prototype

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent

class MainView(context: Context,
               private val size: Point)
    : SurfaceView(context),
        Runnable {


    //Create an obstacle
    private var obstacle: Obstacle = Obstacle(size.x, size.y)

    // This is our thread
    private val gameThread = Thread(this)

    // A boolean which we will set and unset
    private var playing = false

    // Game is paused at the start
    private var paused = true

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // The player character
    private var player: Player = Player(context, size.x, size.y)

    // The score
    private var score = 0

    // The wave number
    private var waves = 1

    // Lives
    private var lives = 3

    // To remember the high score
    private val prefs: SharedPreferences = context.getSharedPreferences(
            "Kotlin Invaders",
            Context.MODE_PRIVATE)

    private var highScore =  prefs.getInt("highScore", 0)

    override fun run() {
        // This variable tracks the game frame rate
        var fps: Long = 0

        while (playing) {

            // Capture the current time
            val startFrameTime = System.currentTimeMillis()

            // Update the frame
            if (!paused) {
                update(fps)
            }

            // Draw the frame
            draw()

            // Calculate the fps rate this frame
            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    private fun update(fps: Long) {
        // Update the state of all the game objects

        // Update the player character
        player.update(fps)

        // move the obstacle
        obstacle.update(fps)

        // Has the player lost
        var lost = false

        if (lost) {
            paused = true
            lives = 3
            score = 0
            waves = 1
        }
    }

    private fun draw() {
        // Make sure our drawing surface is valid or the game will crash
        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            // Now draw the player character
            canvas.drawBitmap(player.bitmap, player.position.left, player.position.top, paint)

            //Draw the obstacle
            canvas.drawRect(obstacle.position, paint)

            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 70f
            canvas.drawText("Score: $score   Lives: $lives Wave: " +
                    "$waves HI: $highScore", 20f, 75f, paint)

            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas)
        }
    }

    // If SpaceInvadersActivity is paused/stopped
    // then shut down our thread.
    fun pause() {
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }

        val prefs = context.getSharedPreferences(
                "Kotlin Invaders",
                Context.MODE_PRIVATE)

        val oldHighScore = prefs.getInt("highScore", 0)

        if(highScore > oldHighScore) {
            val editor = prefs.edit()

            editor.putInt(
                    "highScore", highScore)

            editor.apply()
        }
    }

    // If MainActivity is started then
    // start our thread.
    fun resume() {
        playing = true
        gameThread.start()
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val motionArea = size.y - (size.y / 8)
        when (motionEvent.action and MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            // Or moved their finger while touching screen
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE-> {
                paused = false

                // player character jumps on screen touch
                if (motionEvent.y < motionArea) {
                    if(player.jumpState == Player.grounded){
                        player.jumpState = Player.jumping
                    }
                }
            }

            // Player has removed finger from screen
            //MotionEvent.ACTION_POINTER_UP,
            //MotionEvent.ACTION_UP -> {
            //    if (motionEvent.y > motionArea) {

            //    }
            //}

        }
        return true
    }
}