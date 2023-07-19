package pt.isel.tds.reversi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import pt.ise.tds.reversi.ui.*
import pt.isel.tds.reversi.model.BoardStorage

@Composable
fun FrameWindowScope.ReversiApp(onExit: () -> Unit, storage: BoardStorage) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { ReversiViewModel(scope, storage) } //The ViewModelffe
    ReversiMenu(viewModel, onExit)              // The App menu.
    ReversiDialog(viewModel)        // The Dialogs.
    Column {
        BoardView(
            viewModel.game?.board,
            viewModel.targetsOn,
            viewModel::targets,
            viewModel::toFlip,
            onClick = viewModel::play
        )
        StatusBar(viewModel.status, viewModel.game?.board)
    }
}

