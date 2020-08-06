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
        const val ALLOW_DUPLICATE = "com.github.nutyworks.yagugame.ALLOW_DUPLICATE"
        const val NUMBER_LENGTH = "com.github.nutyworks.yagugame.NUMBER_LENGTH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)

        YaguGame.init()
        YaguGame.playerSize = intent.getIntExtra(PLAYER_SIZE, 1)
        YaguGame.allowDuplicate = intent.getBooleanExtra(ALLOW_DUPLICATE, false)
        YaguGame.numberLength = intent.getIntExtra(NUMBER_LENGTH, 1)

        println(intent.getBooleanExtra(ALLOW_DUPLICATE, false))

        if (YaguGame.playerSize == 1) {

            YaguGame.addPlayer("컴퓨터", YaguGame.generateRandomNumber())
            val gameIntent = Intent(this, GameActivity::class.java)
            startActivity(gameIntent)
        }
        description.text = getString(R.string.input_player_info).format(0)
    }


    @Suppress("UNUSED_PARAMETER")
    fun onNumberConfirm(view: View) {

        val isValidNumber = number_input.text.toString().length == YaguGame.numberLength
                && (YaguGame.allowDuplicate || number_input.text.toString().toByteArray()
            .distinct().size == YaguGame.numberLength)

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
