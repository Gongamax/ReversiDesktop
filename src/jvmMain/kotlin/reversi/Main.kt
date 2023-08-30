package pt.ise.tds.reversi

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.WindowState
import pt.isel.tds.reversi.model.BoardSerializer
import pt.isel.tds.reversi.model.BoardStorage
import pt.isel.tds.reversi.storage.MongoStorage
import pt.isel.tds.reversi.ui.ReversiApp
import pt.isel.tds.storage.MongoDriver


fun main() {
    MongoDriver().use { driver ->
        val storage: BoardStorage = MongoStorage("games", driver, BoardSerializer)
        application(exitProcessOnExit = false) {
            val icon = painterResource("reversi.png")
            Window(
                onCloseRequest = ::exitApplication,
                title = "Reversi Game",
                state = WindowState(size = DpSize.Unspecified),
                icon = icon
            ) {
                ReversiApp(::exitApplication, storage)
            }
        }
    }
}


