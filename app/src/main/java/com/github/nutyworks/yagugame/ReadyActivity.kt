package com.github.nutyworks.yagugame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ready.*
import java.io.Serializable

class ReadyActivity : AppCompatActivity() {

    lateinit var yaguGame: YaguGame

    companion object {
        const val PLAYER_SIZE = "com.github.nutyworks.yagugame.PLAYER_SIZE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)
        val size = intent.getIntExtra(PLAYER_SIZE, -1)
        yaguGame = YaguGame(size)
    }

    fun onNumberConfirm(view: View) {

        val isValidNumber = number_input.text.toString().length == 4
                && number_input.text.toString().toByteArray().distinct().size == 4

        if (isValidNumber) {
            yaguGame.playerNumber.add(number_input.text.toString())
            val isConfirmFinished = yaguGame.playerNumber.size == yaguGame.playerSize

            if (isConfirmFinished) {
                val gameIntent = Intent(this, GameActivity::class.java).apply {
                    putExtra("test", yaguGame)
                }

                startActivity(gameIntent)
            } else {
                description.text = "Player ${yaguGame.playerNumber.size} 번호 입력"
                number_input.setText("")
            }
        } else {
            Toast.makeText(this, "다시 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }
}

class YaguGame(val playerSize: Int) : Serializable {
    val playerNumber = arrayListOf<String>()
    val playerHistory = ArrayList<ArrayList<Guess>>()
    var status = Status.GUESSING

    init {
        for (i in 1..playerSize)
            playerHistory.add(arrayListOf())
    }

    class Guess(val index: Int, val answer: String, val guess: String) {
        fun getStrikes(): Int {

            var strikes = 0

            for (i in 0..3) {
                strikes += if (answer[i] == guess[i]) 1 else 0
            }

            return strikes
        }

        fun getBalls(): Int {
            var balls = 0

            for (i in 0..3) {
                balls += if (answer.contains(guess[i])) 1 else 0
            }

            return balls - getStrikes()
        }
    }

    enum class Status {
        GUESSING, CHECK
    }
}