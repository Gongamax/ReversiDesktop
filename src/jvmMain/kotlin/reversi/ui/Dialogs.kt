package pt.ise.tds.reversi.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import kotlinx.coroutines.delay
import pt.isel.tds.reversi.model.Player
import pt.isel.tds.reversi.ui.CellView
import pt.isel.tds.reversi.ui.boardSize

val dialogWidth = 330.dp
val dialogHeight = 240.dp
val textFieldSize = 200.dp

@Composable
fun ReversiDialog(viewModel: ReversiViewModel): Unit =
    when (viewModel.open) {
        Dialog.NEW_GAME -> NewDialog(viewModel::closeDialog, viewModel::newGame, "New Game")
        Dialog.JOIN_GAME -> JoinDialog(viewModel::closeDialog, viewModel::joinGame, "Join Game")
        Dialog.HELP -> HelpDialog(viewModel::closeDialog)
        Dialog.MESSAGE -> MessageDialog(viewModel::closeDialog, viewModel.message)
        Dialog.ABOUT -> AboutDialog(viewModel::closeDialog, "Rules of the reversi")
        null -> Unit
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageDialog(onClose: () -> Unit, message: String) {
    AlertDialog(
        modifier = Modifier.width(boardSize * 0.8f),
        onDismissRequest = onClose,
        title = {
            Text(message)
            LaunchedEffect(Unit) {
                delay(3000)
                onClose()
            }
        },
        confirmButton = { Button(modifier = Modifier, onClick = onClose) { Text("OK") } }
    )
}

@Composable
fun NewDialog(onClose: () -> Unit, onOk: (String, Player) -> Unit, title: String) {
    DialogTemplate(onClose = onClose, onOk = onOk, title = title)
}

@Composable
fun JoinDialog(onClose: () -> Unit, onOk: (String) -> Unit, title: String) {
    DialogTemplate(
        onClose = onClose,
        onOk = { name, _ -> onOk(name) },
        title = title,
        showCheckbox = false,
        showPlayerSelection = false
    )
}

@Composable
private fun DialogTemplate(
    onClose: () -> Unit,
    onOk: (String, Player) -> Unit,
    title: String,
    showCheckbox: Boolean = true,
    showPlayerSelection: Boolean = true
) {
    var name by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(true) }
    var player by remember { mutableStateOf(Player.WHITE) }

    Dialog(
        onCloseRequest = onClose,
        title = title,
        state = DialogState(
            WindowPosition(Alignment.Center),
            width = if (showCheckbox) dialogWidth else dialogWidth * 0.9f,
            height = if (showPlayerSelection) dialogHeight else dialogHeight * 0.75f
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.padding(
                    horizontal = dialogContentSpaceHorizontally,
                    vertical = dialogContentSpaceHorizontally * 0.5f
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showCheckbox) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "Multiplayer",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontSize = titleSize * 0.75
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = spaceSize))
                }
                if (isChecked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Game name:",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontSize = titleSize * 0.75
                        )
                        Spacer(modifier = Modifier.width(spaceSize))
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.width(textFieldSize).align(Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = spaceSize))
                if (showPlayerSelection) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { player = player.other() }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Player:",
                                fontSize = titleSize * 0.75
                            )
                            CellView(player, modifier = Modifier.size(barPiecesSize), toFlip = true)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(if (showPlayerSelection) 0.75f else 1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Button(
                        onClick = { onOk(name, player) },
                        enabled = isChecked && name.isNotEmpty() || !isChecked,
                    ) {
                        Text(text = title)
                    }
                }
            }
        }
    }
}

