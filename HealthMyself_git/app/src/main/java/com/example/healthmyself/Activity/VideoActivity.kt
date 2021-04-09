package com.example.healthmyself.Activity

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import com.example.healthmyself.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class VideoActivity : YouTubeBaseActivity(){
    var videoId : String? = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        if(intent.hasExtra("video_id"))
        {
            Log.d("e", videoId.toString())
            videoId = intent.getStringExtra(("video_id"))
        }
        else
        {
            Log.d("e", videoId.toString())
            videoId = "FFWklphDy8A"
        }

        val youtubeView = findViewById<YouTubePlayerView>(R.id.video)
        youtubeView.initialize("develop", object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                    proveder: YouTubePlayer.Provider,
                    player: YouTubePlayer,
                    wasRestored: Boolean
            ) {
                if (!wasRestored) {

                    player.cueVideo(videoId)
                }
            }

            override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider?,
                    result: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }
        })
    }

}