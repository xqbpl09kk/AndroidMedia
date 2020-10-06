package me.pl09kk.learn.android.media.book02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player.*
import me.pl09kk.learn.android.media.R

class PlayerActivity : AppCompatActivity() {

    private val playerAgent = MediaPlayerDemo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playButton.setOnClickListener {
            playerAgent.togglePlay()
        }
    }

    override fun onStop() {
        super.onStop()
        playerAgent.stop()
    }

    override fun onResume() {
        super.onResume()
        playerAgent.togglePlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerAgent.release()
    }
}