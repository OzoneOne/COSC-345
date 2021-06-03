package com.example.prototype

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class MainActivity : Activity() {

    // kotlinInvadersView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    private var mainView: MainView? = null

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
        mainView = MainView(this, size)
        setContentView(mainView)
    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        mainView?.resume()
    }

    // This method executes when the player quits the game
    override fun onPause() {
        super.onPause()

        // Tell the gameView pause method to execute
        mainView?.pause()
    }
}