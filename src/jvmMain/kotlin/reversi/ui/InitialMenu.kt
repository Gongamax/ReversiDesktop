package reversi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ise.tds.reversi.ui.Dialog
import pt.ise.tds.reversi.ui.ReversiViewModel
import pt.ise.tds.reversi.ui.barSize
import pt.isel.tds.reversi.model.Player
import pt.isel.tds.reversi.ui.boardSize

@Composable
fun ReversiInitialMenu(vm: ReversiViewModel) {
    val menuImage = painterResource("reversiBoardMenu.png")
    val singleIcon = painterResource("player.png")
    val multiPlayerIcon = painterResource("player-versus-player.png")
    Column(
        modifier = Modifier.height(boardSize + barSize).width(boardSize).background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {

            Image(
                painter = menuImage,
                contentDescription = "InitialMenu",
                modifier = Modifier,
                alignment = Alignment.Center
            )
        }
        Text(
            modifier = Modifier.absolutePadding(10.dp),
            text = "Welcome to Reversi Game",
            textAlign = TextAlign.Center,
            color = Color.White,
            style = MaterialTheme.typography.h6,
        )
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(boardSize / 2).padding(horizontal = 25.dp)
                    .clickable(onClick = { vm.newGame("", Player.BLACK) })
            ) {
                Image(painter = singleIcon, contentDescription = "SingleIcon")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(boardSize / 2).padding(horizontal = 25.dp)
                    .clickable(onClick = { vm.openDialog(Dialog.NEW_GAME) })
            ) {
                Image(painter = multiPlayerIcon, contentDescription = "MultiIcon")
            }
        }
    }
}