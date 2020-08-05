package com.github.nutyworks.yagugame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ready.*

class ReadyActivity : AppCompatActivity() {

    companion object {
        const val PLAYER_SIZE = "com.github.nutyworks.yagugame.PLAYER_SIZE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        YaguGame.init()
        YaguGame.playerSize = intent.getIntExtra(PLAYER_SIZE, -1)
        description.text = getString(R.string.input_player_info).format(0)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNumberConfirm(view: View) {

        val isValidNumber = number_input.text.toString().length == 4
                && number_input.text.toString().toByteArray().distinct().size == 4

        if (isValidNumber) {

            YaguGame.addPlayer(name_input.text.toString(), number_input.text.toString())
            val isConfirmFinished = YaguGame.players.size == YaguGame.playerSize

            if (isConfirmFinished) {
                val gameIntent = Intent(this, GameActivity::class.java)
                startActivity(gameIntent)
            } else {
                description.text =
                    getString(R.string.input_player_info).format(YaguGame.players.size)
                name_input.setText("")
                number_input.setText("")
            }
        } else {
            Toast.makeText(this, "다시 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }
}
