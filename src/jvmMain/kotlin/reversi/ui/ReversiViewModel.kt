package pt.ise.tds.reversi.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import pt.isel.tds.reversi.model.*
import reversi.ai.firstRandomMove

class ReversiViewModel(
    private val scope: CoroutineScope,      // Scope of composed coroutines
    private val storage: BoardStorage       // Storage to use in game operations
) {

    //region Auxiliary Functions
    private inline fun tryRun(block: () -> Unit) =
        try {
            block()
        } catch (e: IllegalStateException) {
            openMessageDialog(e)
        } catch (e: IllegalArgumentException) {
            openMessageDialog(e)
        }

    private fun CoroutineScope.tryLaunch(block: suspend () -> Unit) =
        launch {
            try {
                block()
            } catch (e: IllegalStateException) {
                openMessageDialog(e)
            } catch (e: IllegalArgumentException) {
                openMessageDialog(e)
            }
        }

    private inline fun tryIgnore(block: () -> Unit) =
        try {
            block()
        } catch (e: IllegalStateException) {
            //Ignore message
        } catch (e: IllegalArgumentException) {
            //Ignore message
        }

    var inInitialState by mutableStateOf<Boolean>(true)
        private set

    fun redirectToMainWindow() = tryRun { inInitialState = false }
    //endregion

    //region Dialogs
    var open by mutableStateOf<Dialog?>(null)
        private set

    var message: String = "No message"
        private set

    fun openDialog(dialog: Dialog) {
        open = dialog
    }

    fun closeDialog() {
        open = null
    }

    private fun openMessageDialog(exception: Throwable) {
        message = exception.message ?: "Unknown error"
        openDialog(Dialog.MESSAGE)
    }
    //endregion

    //region Flip
    private var flipCells by mutableStateOf(emptySet<Cell>())

    fun cellsToFlip(prevBoard: Board, currBoard: Board) =
        prevBoard.moves.filter { (cell, player) -> currBoard.moves[cell] != player }.keys

    fun toFlip(cell: Cell): Boolean = cell in flipCells
    //endregion

    //region Game Section

    private var initialPlayer: Player? = null

    var game: Game? by mutableStateOf(null)
        private set

    fun joinGame(name: String) = tryRun {
        scope.launch {
            tryRun {
                game = joinGame(name, storage)
                val g = game
                check(g != null && g is MultiPlayer)
                initialPlayer = g.player
                closeDialog()
                autoRefreshLoop()
            }
        }
    }


    fun newGame(name: String, player: Player) = tryRun {
        game = if (name == "") {
            createGame(player, null, storage)
        } else
            createGame(player, name, storage).also { initialPlayer = player }
        closeDialog()
    }


    fun play(cell: Cell) = tryIgnore {
        game?.let { g ->
            val b = g.board
            game = g.play(cell, storage)
            flipCells = cellsToFlip(b, game?.board ?: b)
            autoRefreshLoop()
            val currGame = game
            if (currGame is SinglePlayer && currGame.board is BoardRun) aiPlay(currGame) //Immediately calls the AI play
        // if it's a single player game
        }
    }

    private fun aiPlay(currGame: Game?) {
        scope.launch {
            tryLaunch {
                currGame?.let { g ->
                    val aiMove = (g.board as BoardRun).firstRandomMove()
                    aiThinking = true
                    delay(2000)
                    if (aiMove == null) pass()
                    else {
                        val prevBoard = currGame.board
                        game = g.play(aiMove, storage)
                        flipCells = cellsToFlip(prevBoard, game?.board ?: currGame.board)
                    }
                    aiThinking = false
                }
            }
        }
    }

    private var aiThinking: Boolean = false


//endregion

    //region Information
    val status: StatusInfo
        get() = when (val b = game?.board) {
            is BoardPass -> StatusInfo("Turn", b.turn, initialPlayer, refreshJob != null)
            is BoardRun -> StatusInfo("Turn", b.turn, initialPlayer, refreshJob != null, aiThinking)
            is BoardWin -> StatusInfo("Winner", b.winner, initialPlayer)
            is BoardDraw -> StatusInfo("Draw")
            null -> StatusInfo("")
        }
//endregion

    //region Pass
    fun pass() = tryRun {
        game?.let { g ->
            game = g.pass(storage)
            if (game.let { it is SinglePlayer && it.board is BoardRun && it.firstPlayer != it.board.turn })
                aiPlay(game)
            autoRefreshLoop()
        }
    }

    fun isPass(): Boolean {
        val g = game ?: return false
        return if (g is MultiPlayer)
            g.board.let { b -> b is BoardRun && b.checkAvailablePlays() == null && g.player == b.turn }
        else
            g.board.let { b -> b is BoardRun && b.checkAvailablePlays() == null }
    }

//endregion

    //region Refresh
    var autoRefresh by mutableStateOf(false)
        private set

    fun toggleAutoRefresh() {
        autoRefresh = !autoRefresh
        if (autoRefresh) autoRefreshLoop()
        else refreshJob?.let { it.cancel(); println("+"); refreshJob = null }
    }

    private var refreshJob: Job? by mutableStateOf(null)

    private fun autoRefreshLoop() {
        if (autoRefresh && mayRefresh())
            refreshJob = scope.launch {
                while (true) {
                    game = game?.refreshGame(storage, checked = false)
                    if (!mayRefresh()) break
                    delay(2000)
                }
                refreshJob = null
            }
    }

    fun refreshGame() = scope.tryLaunch {
        game = game?.refreshGame(storage)
    }

    val canRefresh: Boolean
        get() = mayRefresh() && !autoRefresh

    private fun mayRefresh(): Boolean =
        game?.run { this is MultiPlayer && board is BoardRun && board.turn != player } ?: false

//endregion

    //region Targets
    fun targets(cond: Boolean) {
        targetsOn = !cond
    }

    fun targets(cell: Cell): Boolean {
        val g = game
        val b = g?.board
        return b is BoardRun && b.canPlay(cell) && targetsOn
                && (g is MultiPlayer && g.player == b.turn || g is SinglePlayer && b.turn == g.firstPlayer)
    }

    var targetsOn by mutableStateOf(false)
        private set
//endregion

}

//region Utils
data class StatusInfo(
    val label: String,
    val player: Player? = null,
    val initialPlayer: Player? = null,
    val refreshing: Boolean = false,
    val aiThinking: Boolean = false
) {
    val component1: String get() = label
    val component2: Player? get() = player
}

enum class Dialog { HELP, NEW_GAME, JOIN_GAME, MULTIPLAYER_OPTION, MESSAGE, ABOUT }
//endregion