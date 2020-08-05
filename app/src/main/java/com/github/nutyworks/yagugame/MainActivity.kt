package com.github.nutyworks.yagugame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        player_size.minValue = 1
        player_size.maxValue = 35
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStartClicked(view: View) {

        val readyIntent = Intent(this, ReadyActivity::class.java).apply {
            putExtra(ReadyActivity.PLAYER_SIZE, player_size.value)
        }

        startActivity(readyIntent)
    }
}