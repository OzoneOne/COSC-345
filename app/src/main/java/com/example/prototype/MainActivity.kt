package com.example.prototype

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

// This class is the entry point to the game
// and will initialize an instance of the class
// that does all the work as well as
// start and stops the game loop/thread when
// the player starts and stops the app.

class MainActivity : Activity() {
    // alpha03View will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    private var gameView: MainView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide the Android status bar at the top of the screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Get a Display object to access screen details
        val display = windowManager.defaultDisplay
        // Load the resolution into a Point object
        val size = Point()
        display.getSize(size)

        // Initialize gameView and set it as the view
        gameView = MainView(this, size)
        setContentView(gameView)
    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        gameView?.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        gameView?.pause()
    }
}
