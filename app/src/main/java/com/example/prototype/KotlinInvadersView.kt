package com.example.prototype

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent

class KotlinInvadersView(context: Context,
                         private val size: Point)
    : SurfaceView(context),
        Runnable {


    //Create an obstacle
    private var obstacle: Obstacle = Obstacle(size.x, size.y)

    // For making a noise
    private val soundPlayer = SoundPlayer(context)

    // This is our thread
    private val gameThread = Thread(this)

    // A boolean which we will set and unset
    private var playing = false

    // Game is paused at the start
    private var paused = true

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // The players ship
    private var playerShip: PlayerShip = PlayerShip(context, size.x, size.y)

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

    // How menacing should the sound be?
    private var menaceInterval: Long = 1000

    // Which menace sound should play next
    private var uhOrOh: Boolean = false
    // When did we last play a menacing sound
    private var lastMenaceTime = System.currentTimeMillis()





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

            // Play a sound based on the menace level
            if (!paused && ((startFrameTime - lastMenaceTime) > menaceInterval))
                menacePlayer()
        }
    }

    private fun menacePlayer() {
        if (uhOrOh) {
            // Play Uh
            soundPlayer.playSound(SoundPlayer.uhID)

        } else {
            // Play Oh
            soundPlayer.playSound(SoundPlayer.ohID)
        }

        // Reset the last menace time
        lastMenaceTime = System.currentTimeMillis()
        // Alter value of uhOrOh
        uhOrOh = !uhOrOh

    }

    private fun update(fps: Long) {
        // Update the state of all the game objects

        // Move the player's ship
        playerShip.update(fps)

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
            // Now draw the player spaceship
            canvas.drawBitmap(playerShip.bitmap, playerShip.position.left,
                    playerShip.position.top
                    , paint)

            // Draw the invaders
            /*for (invader in invaders) {
                if (invader.isVisible) {
                    if (uhOrOh) {
                        canvas.drawBitmap(Invader.bitmap1,
                                invader.position.left,
                                invader.position.top,
                                paint)
                    } else {
                        canvas.drawBitmap(Invader.bitmap2,
                                invader.position.left,
                                invader.position.top,
                                paint)
                    }
                }
            }

            */

            //Draw the obstacle
            canvas.drawRect(obstacle.position, paint)



            /*
            // Draw the bricks if visible
            for (brick in bricks) {
                if (brick.isVisible) {
                    canvas.drawRect(brick.position, paint)
                }
            }

            // Draw the players playerBullet if active
            if (playerBullet.isActive) {
                canvas.drawRect(playerBullet.position, paint)
            }

            // Draw the invaders bullets
            for (bullet in invadersBullets) {
                if (bullet.isActive) {
                    canvas.drawRect(bullet.position, paint)
                }
            } */

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

    // If SpaceInvadersActivity is started then
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


                if (motionEvent.y > motionArea) {
                    if (motionEvent.x > size.x / 2) {
                        playerShip.moving = PlayerShip.right
                    } else {
                        playerShip.moving = PlayerShip.left
                    }

                }

                if (motionEvent.y < motionArea) {
                    if(playerShip.touchGround == PlayerShip.grounded){
                        playerShip.jumpState = PlayerShip.jumping
                    }


                // Shots fired


                        /*
                    if (playerBullet.shoot(
                                    playerShip.position.left + playerShip.width / 2f,
                                    playerShip.position.top,
                                    playerBullet.up)) {

                        soundPlayer.playSound(SoundPlayer.shootID)
                    }   */

                   /* playerShip.jumpState = PlayerShip.jumping */


                   /*  Jumping physics that don't work
                    if(playerShip.jumpState == 0){
                        playerShip.velocity = 50f
                        playerShip.position.top += playerShip.velocity
                        playerShip.position.bottom += playerShip.velocity
                        playerShip.velocity -= 5f
                        playerShip.jumpState = 1
                    } else if(playerShip.jumpState == 1){
                        playerShip.position.top += playerShip.velocity
                        playerShip.position.bottom += playerShip.velocity
                        playerShip.velocity -= 5f
                    }

                    */
                }
            }

            // Player has removed finger from screen
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                if (motionEvent.y > motionArea) {
                    playerShip.moving = PlayerShip.stopped
                }
            }

        }
        return true
    }

}