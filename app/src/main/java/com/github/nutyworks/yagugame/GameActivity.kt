package com.github.nutyworks.yagugame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.item_history.view.*
import slush.ItemListEditor
import slush.Slush

class GameActivity : AppCompatActivity() {

    companion object {
        const val PLAYER_SIZE = "com.github.nutyworks.yagugame.PLAYER_SIZE"
    }

    lateinit var itemHistoryEditor: ItemListEditor<YaguGame.Player.Guess>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        guess_description.text = getString(R.string.guess_the_number)
            .format(YaguGame.getCurrentPlayer().name)

        itemHistoryEditor = Slush.SingleType<YaguGame.Player.Guess>()
            .setLayoutManager(LinearLayoutManager(this))
            .setItemLayout(R.layout.item_history)
            .onBind { view, guess ->
                view.guessing_number.text = guess.guess
                view.strikes_text.text = guess.getStrikes().toString()
                view.balls_text.text = guess.getBalls().toString()
                println("guess ${guess.guess} ${guess.getAnswer()}${guess.getStrikes()} ${guess.getBalls()}")
            }
            .setItems(YaguGame.getCurrentPlayer().history)
            .into(findViewById(R.id.yagu_history_wrapper))
            .itemListEditor
    }

    @Suppress("UNUSED_PARAMETER")
    fun onGuessOrConfirm(view: View) {

        val isValidNumber = guess_number.text.toString().length == 4
                && guess_number.text.toString().toByteArray().distinct().size == 4

        if (isValidNumber && YaguGame.status == YaguGame.Status.GUESSING) {
            YaguGame.getCurrentPlayer().addHistory(guess_number.text.toString())

            guess_description.text = "넘어가려면 확인 버튼을 누르세요."

            itemHistoryEditor.changeAll(YaguGame.getCurrentPlayer().history)

            YaguGame.status = YaguGame.Status.CHECK
        } else if (YaguGame.status == YaguGame.Status.CHECK) {
            if (YaguGame.nextTurn() == -1) {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            } else {
                guess_description.text = getString(R.string.guess_the_number)
                    .format(YaguGame.getCurrentPlayer().name)
                guess_number.setText("")
                itemHistoryEditor.changeAll(YaguGame.getCurrentPlayer().history)
                YaguGame.status = YaguGame.Status.GUESSING
            }
        } else {
            Toast.makeText(this, "다시 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }
}