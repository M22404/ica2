package com.example.ica2

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.ica2.databinding.ActivityVideoBinding


class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding

    private lateinit var videoView: VideoView
    private lateinit var pipButton: Button
    private lateinit var backButton: Button
    private lateinit var mediaController: MediaController

    private lateinit var oldLayoutParams: ViewGroup.LayoutParams
    private lateinit var borderlessParams: ViewGroup.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = binding.videoView
        pipButton = binding.pipButton
        backButton = binding.videoBackButton

        borderlessParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        oldLayoutParams = videoView.layoutParams

        backButton.setOnClickListener {
            onBackPressed()
        }

        videoView.setVideoURI(resourceUri(R.raw.video))

        mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Listener is called immediately after the user exits PiP but before animating.
        videoView.addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (left != oldLeft || right != oldRight || top != oldTop || bottom != oldBottom) {
                // The playerView's bounds changed, update to reflect its new bounds
                val sourceRectHint = Rect()
                videoView.getGlobalVisibleRect(sourceRectHint)
                setPictureInPictureParams(
                    PictureInPictureParams.Builder().setSourceRectHint(sourceRectHint).build()
                )
            }
        }

        pipButton.setOnClickListener {
            enterPipMode()
        }
    }

    private fun enterPipMode() {
        val rational = Rational(videoView.width, videoView.height)
        val params =
            PictureInPictureParams.Builder().setAspectRatio(rational).setSourceRectHint(Rect())
                .build()

        supportActionBar?.hide()

        pipButton.visibility = View.INVISIBLE
        backButton.visibility = View.INVISIBLE
        videoView.layoutParams = borderlessParams
        videoView.setMediaController(null)
        enterPictureInPictureMode(params)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if (!isInPictureInPictureMode) {
            pipButton.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE
            supportActionBar?.show()
            videoView.layoutParams = oldLayoutParams
            videoView.setMediaController(mediaController)
        }
    }


}