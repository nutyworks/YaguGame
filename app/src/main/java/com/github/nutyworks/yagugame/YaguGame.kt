package com.github.nutyworks.yagugame

object YaguGame {
    var playerSize = 0
    val players = ArrayList<Player>()
    var status = Status.GUESSING
    var turn = 0
    var numberLength = 0
    var allowDuplicate = false

    fun generateRandomNumber(): String {
        var str = ""
        val num = (0..9).toMutableList()
        for (i in 1..numberLength) {
            val selected = num.random()
            if (!allowDuplicate)
                num.remove(selected)

            str += selected.toString()
        }

        return str
    }

    fun init() {
        playerSize = 0
        players.clear()
        status = Status.GUESSING
        turn = 0
        numberLength = 0
        allowDuplicate = true
    }

    fun nextTurn(): Int {
        var tries = 0
        // 플레이어가 죽었다면 스킵
        do {
            turn = (turn + 1) % playerSize
            tries++

            if (tries > playerSize) {
                turn = -1
                break
            }

        } while (players[turn].isEliminated)
        return turn
    }

    fun getCurrentPlayer(): Player {
        return players[turn]
    }

    fun addPlayer(name: String, answer: String) {
        val p = Player(name, answer)
        players.add(p)
    }

    class Player(val name: String, val answer: String) {
        val history = ArrayList<Guess>()
        var isEliminated = false

        fun addHistory(guessed: String) {
            history.add(Guess(guessed))
        }

        inner class Guess(val guess: String) {
            fun getStrikes(): Int {

                var strikes = 0

                for (i in 0 until numberLength) {
                    strikes += if (answer[i] == guess[i]) 1 else 0
                }

                if (strikes == numberLength) isEliminated = true

                return strikes
            }

            fun getBalls(): Int {
                var balls = 0

                for (i in 0 until numberLength) {
                    balls += if (answer.contains(guess[i])) 1 else 0

//                    println("$i $answer ${guess[i]} ${answer.contains(guess[i])} $balls")
                }

                return balls - getStrikes()
            }

            fun getAnswer(): String = answer
        }
    }


    enum class Status {
        GUESSING, CHECK
    }
}
