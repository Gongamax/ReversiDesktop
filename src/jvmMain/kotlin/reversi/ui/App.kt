package pt.isel.tds.reversi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import pt.ise.tds.reversi.ui.*
import pt.isel.tds.reversi.model.BoardStorage
import reversi.ui.ReversiInitialMenu

@Composable
fun FrameWindowScope.ReversiApp(onExit: () -> Unit, storage: BoardStorage) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { ReversiViewModel(scope, storage) } //The ViewModelffe
    ReversiMenu(viewModel, onExit)              // The App menu.
    ReversiDialog(viewModel)        // The Dialogs.
    ReversiInitialMenu(viewModel)
    Column {
        BoardView(
            viewModel.game?.board,
            viewModel.targetsOn,
            viewModel::targets,
            viewModel::toFlip,
            onClick = viewModel::play,
            onNewGame = viewModel::newGame
        )
        if (viewModel.game != null)
            StatusBar(viewModel.status, viewModel.game?.board)
    }
}

