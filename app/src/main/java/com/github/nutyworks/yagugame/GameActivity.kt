package com.github.nutyworks.yagugame

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

    var turn = 0
    lateinit var itemHistoryEditor: ItemListEditor<YaguGame.Guess>

    lateinit var yaguGame: YaguGame
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        yaguGame = intent.getSerializableExtra("test") as YaguGame

        itemHistoryEditor = Slush.SingleType<YaguGame.Guess>()
            .setLayoutManager(LinearLayoutManager(this))
            .setItemLayout(R.layout.item_history)
            .onBind { view, guess ->
                view.guessing_number.text = guess.guess
                view.strikes_text.text = guess.getStrikes().toString()
                view.balls_text.text = guess.getBalls().toString()
            }
            .setItems(yaguGame.playerHistory[turn])
            .into(findViewById(R.id.yagu_history_wrapper))
            .itemListEditor
    }

    fun onGuessOrConfirm(view: View) {

        val isValidNumber = guess_number.text.toString().length == 4
                && guess_number.text.toString().toByteArray().distinct().size == 4

        if (isValidNumber && yaguGame.status == YaguGame.Status.GUESSING) {
            yaguGame.playerHistory[turn].add(
                YaguGame.Guess(
                    turn,
                    yaguGame.playerNumber[turn],
                    guess_number.text.toString()
                )
            )

            guess_description.text = "넘어가려면 확인 버튼을 누르세요."

            itemHistoryEditor.changeAll(yaguGame.playerHistory[turn])

            yaguGame.status = YaguGame.Status.CHECK
        } else if (yaguGame.status == YaguGame.Status.CHECK) {
            turn = (turn + 1) % yaguGame.playerSize

            guess_description.text = "Player ${turn}의 번호를 추측하세요."
            guess_number.setText("")
            itemHistoryEditor.changeAll(yaguGame.playerHistory[turn])
            yaguGame.status = YaguGame.Status.GUESSING
        } else {
            Toast.makeText(this, "다시 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }


}