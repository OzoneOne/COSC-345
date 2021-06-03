package com.example.prototype

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent

/**
 * This class is the class that does all the work.
 * In fact, it probably does too much work.
 * It will hold instances of all the other classes and
 * control their updating, interaction and drawing to the screen.
 * It will also handle the player’s screen touches.
 */


class MainView(context: Context, private val size: Point) : SurfaceView(context), Runnable {
    // Game thread
    private val gameThread = Thread(this)
    // Playing states
    private var playing = false
    private var paused = true
    // Canvas and paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // Initialise the player game object
    private var player: Player = Player(context, size.x, size.y)

    // The tracks you can play
    // tracks to do:
    // - give it a random generator, make obstacles activate when it returns true
    // - do collision detection between obstacles n player
    
    private var track: Tracks = Tracks(context, size.x, size.y)

    private val trackObstacles = ArrayList<Obstacle>()
    private var nextObstacle = 0
    private val maxObstacles = 10

    // Score
    private var score = 0
    private var waves = 1
    private var lives = 1
    private var highScore = 0


    // Initialises the game objects for a level
    private fun prepareLevel() {
        for (i in 0 until maxObstacles ){
            trackObstacles.add(Obstacle(context, size.y))

        }
    }

    override fun run() {
        // Track frame rate
        var fps: Long= 0
        while (playing){
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
        /**
         * Updates the state of all the game objects on every frame
         */

        var lost = false
        // Update player and move them
        player.update(fps)
        // Update track
        track.update(fps)
        
        if (track.tryStartObstacle(waves)) {
            if (trackObstacles[nextObstacle].startObstacle(size.x.toFloat(), size.y / 2f, 0)) {
                // obstacle activated
                nextObstacle++
            }
        }
        for (obstacle in trackObstacles) {
            if (obstacle.obstacleIsActive) {
                obstacle.updateObstacle(fps)
            }
        }
        // Has an obstacle hit the left of the screen
        for (obstacle in trackObstacles) {
            if (obstacle.obstacleIsActive) {
                if (obstacle.obstaclePosition.left < 0) {
                    score++
                    obstacle.obstacleIsActive = false
                }
            }
        }

        for (obstacle in trackObstacles) {
            if (obstacle.obstacleIsActive) {
                // Has it hit player?

                if (RectF.intersects(player.position, obstacle.obstaclePosition)) {
                    obstacle.obstacleIsActive = false
                    lost = true
                    break
                }
            }
        }

        if (lost) {
            paused = true
            lives = 0
            score = 0
            waves = 1
            trackObstacles.clear()
            prepareLevel()
        }
    }

    private fun draw(){
        // Make sure our drawing surface is valid or the game will crash
        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            // BR
            canvas.drawBitmap(track.trackBitmap, track.trackPosition.left,
                track.trackPosition.top, paint)

            // Obstacles
  
            for (obstacle in trackObstacles){
                if(obstacle.obstacleIsActive){
                    canvas.drawRect(obstacle.obstaclePosition, paint)
                }
            }
            // Draw the player
            canvas.drawBitmap(player.bitmap, player.position.left,
                player.position.top
                , paint)
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
    fun pause() {
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
    }
    fun resume() {
        playing = true
        prepareLevel()
        gameThread.start()
    }
    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            // Or moved their finger while touching screen
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                paused = false
                if (motionEvent.y > size.y - size.y / 8) {
                    if (motionEvent.x > size.x / 2) {
                        player.moving = Player.up
                    } else {
                        player.moving = Player.down
                    }
                }
            }
            // Player has removed finger from screen
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                if (motionEvent.y > size.y - size.y / 10) {
                    player.moving = Player.stopped
                }
            }
        }
        return true
    }
}
