package pt.isel.tds.reversi.model

import pt.isel.tds.reversi.storage.Storage

typealias BoardStorage = Storage<String, Board>

/**
 * Represents a game of Reversi.
 * @property board the board of the game.
 * @constructor Creates a game with the given board and showTargets.
 * There are two possible states of game: [SinglePlayer] and [MultiPlayer].
 * These hierarchies are to be used by pattern matching.
 */
sealed class Game(val board: Board)
class SinglePlayer(board: Board, val firstPlayer: Player) : Game(board)
class MultiPlayer(board: Board, val player: Player, val id: String) : Game(board)

/**
 * Creates a new game with the given [player] and [id].
 * If the [id] is null, then a single player game is created.
 * Otherwise, a multiplayer game is created.
 */
fun createGame(player: Player, id: String?, st: BoardStorage) =
    if (id == null) SinglePlayer(createBoard(player), player)
    else MultiPlayer(createBoard(player), player, id).also {
        st.create(id, it.board)
    }

/**
 * Joins a game with the given [id].
 * @throws IllegalStateException if the game is not available.
 */
suspend fun joinGame(id: String, st: BoardStorage): Game {
    val board = checkNotNull(st.read(id)) { "Game not found" }
    val movesSize = board.moves.size
    check(board is BoardRun && movesSize in 4..5) { "Game is not available" }
    return if (movesSize == 4) MultiPlayer(board, board.turn.other(), id) else MultiPlayer(board, board.turn, id)
}

/**
 * Plays a move in [cell] cell by the current player.
 * @throws IllegalStateException if it is not the current player turn.
 */
fun Game.play(cell: Cell, st: BoardStorage) =
    when (this) {
        is MultiPlayer -> {
            if (board is BoardRun)
                check(player == board.turn) { "Is not your turn" }
            MultiPlayer(board.play(cell), player, id).also { st.update(id, it.board) }
        }

        is SinglePlayer -> SinglePlayer(board.play(cell), firstPlayer)
    }

/**
 * Refreshes the game for the opponent player, showing the board updated.
 * If the game is over, then an exception is thrown.
 * @throws IllegalStateException if the game is over.
 * @throws IllegalStateException if the game is not multiplayer.
 */
suspend fun Game.refreshGame(st: BoardStorage, checked: Boolean = true): Game =
    when (this) {
        is MultiPlayer -> {
            val newBoard = st.read(id) ?: throw Exception("Game not found")
            if (checked) check(newBoard != this.board) { "No changes" }
            if (newBoard != board) MultiPlayer(newBoard, player, id)
            else MultiPlayer(board, player, id)
        }

        else -> throw IllegalStateException("Command only available on multiplayer game")
    }


/**
 * Passes the turn to the opponent player.
 * @return the game with the turn passed.
 */
fun Game.pass(st: BoardStorage): Game =
    when (this) {
        is MultiPlayer -> MultiPlayer(board.passOrRunBoard(), player, id).also { st.update(id, it.board) }
        is SinglePlayer -> SinglePlayer(board.passOrRunBoard(), firstPlayer)
    }
