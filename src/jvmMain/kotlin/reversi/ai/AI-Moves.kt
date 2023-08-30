package reversi.ai

import pt.isel.tds.reversi.model.*

//Should be the EASY to Normal mode
fun BoardRun.firstRandomMove() =
    Cell.values.shuffled().firstOrNull { canPlay(it) }

//Should be the NORMAL MODE
fun BoardRun.mostValuableAvailableMove() =
    Cell.values
        .filter { canPlay(it) }
        .map { getMoveValue(it) to it }
            .maxByOrNull { it.first }
        ?.second

//TODO: To be implemented together with the game class
enum class Difficulty {
    EASY, NORMAL, HARD
}

//TODO: FIX MULTIPLAYER RANDOM NO FLIP BUG
//TODO: DEVELOP MINIMAX ALGORITHM