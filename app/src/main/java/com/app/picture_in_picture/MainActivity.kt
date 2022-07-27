package com.app.picture_in_picture

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_exo_layout.*

class MainActivity : AppCompatActivity(), Player.Listener {

    var player: ExoPlayer ?= null
    var isPipMode: Boolean ?= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()

        viewShown.setOnClickListener {
            Handler().postDelayed({
                hideSystemUI()
            }, 2000)
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
    }

    override fun onResume() {
        super.onResume()
        if(player!!.isPlaying) {
            isPipMode = false
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(applicationContext).build()
        exoPlayer.player = player
        exoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        val mediaItem: MediaItem = MediaItem.fromUri("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4")
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.play()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if(!isPipMode!!) {
            enterPictureInPictureMode()
            isPipMode = true
        } else {
            super.onBackPressed()
        }
    }
  
    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
    }

}