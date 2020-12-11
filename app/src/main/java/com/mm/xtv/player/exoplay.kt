package com.mm.xtv.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mm.xtv.R
import kotlinx.android.synthetic.main.activity_exoplay.*


class exoplay : AppCompatActivity() {

    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playBackPosition: Long = 0

//    private lateinit var playerView: VideoView
//    private var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplay)
        val urlM3U8 = intent.extras?.getString("tv")
        val userAgent =
            Util.getUserAgent(playerView.context, playerView.context.getString(R.string.app_name))
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        val hlsMediaSource =
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("$urlM3U8"))
        supportActionBar?.hide()
        hideSystemUi()
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Bind the player to the view
        playerView.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.seekTo(currentWindow, playBackPosition)
        mPlayer!!.prepare(hlsMediaSource, false, false)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playBackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }
    override fun onBackPressed() {
        AudienceNetworkAds.initialize(this)
        val interstitialAd = InterstitialAd(this,getString(R.string.interstitial_layout))//getString(R.string.interstitial_layout)
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
            }
            override fun onInterstitialDismissed(ad: Ad) {
            }
            override fun onError(ad: Ad, adError: AdError) {
            }
            override fun onAdLoaded(ad: Ad) {
                interstitialAd.show()
            }
            override fun onAdClicked(ad: Ad) {
            }
            override fun onLoggingImpression(ad: Ad) {
            }
        }
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig()
            .withAdListener(interstitialAdListener)
            .build()
        )
        super.onBackPressed()
        finish()
    }
}



