package pt.ise.tds.reversi

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import pt.isel.tds.reversi.model.BoardSerializer
import pt.isel.tds.reversi.model.BoardStorage
import pt.isel.tds.reversi.ui.ReversiApp
import pt.isel.tds.storage.TextFileStorage

fun main() {
    val storage: BoardStorage = TextFileStorage("games", BoardSerializer)
    application(exitProcessOnExit = false) {
        val icon = painterResource("reversi.png")
        Tray(
            icon = icon,
            menu = {
                Item("Quit App", onClick = ::exitApplication)
            })
        Window(
            onCloseRequest = ::exitApplication,
            title = "TDS Reversi",
            state = WindowState(size = DpSize.Unspecified),
            icon = icon
        ) {
            ReversiApp(::exitApplication, storage)
        }
    }
}