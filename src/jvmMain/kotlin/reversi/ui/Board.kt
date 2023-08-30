package pt.isel.tds.reversi.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import pt.isel.tds.reversi.model.*
import pt.isel.tds.view.Sprites
import reversi.ui.ReversiInitialMenu

// Dimensions of the board presentation.
val cellSize = 64.dp
val lineSize = 2.dp
val subTitleSize = 16.dp
val boardSize = cellSize * BOARD_DIM + lineSize * (BOARD_DIM - 1) + subTitleSize


const val SPRITE_SIZE = 64
val sprites = Sprites("sprites1.png")
val animationList = listOf(
    sprites[IntOffset(SPRITE_SIZE * 6, 0)],
    sprites[IntOffset(SPRITE_SIZE * 7, 0)]
) + List(6) { sprites[IntOffset((it + 1) * SPRITE_SIZE, SPRITE_SIZE)] }


@Composable
fun BoardView(
    board: Board?,
    targetsOn: Boolean = false,
    canPlay: (Cell) -> Boolean,
    toFlip: (Cell) -> Boolean,
    onClick: (Cell) -> Unit
) {
    var delayTargets by remember(board) { mutableStateOf(false) }
    if (!delayTargets && targetsOn) {
        LaunchedEffect(delayTargets) {
            delay(1000)
            delayTargets = true
        }
    }
    Row(
        modifier = Modifier.width(boardSize).background(Color.DarkGray),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.size(subTitleSize))
        repeat(BOARD_DIM) {
            Box(Modifier.width(cellSize).height(subTitleSize), contentAlignment = Alignment.Center) {
                Text("${'A' + it}", color = Color.White, textAlign = TextAlign.Center)
            }
        }
    }
    Column(
        modifier = Modifier.width(boardSize).background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(BOARD_DIM) { row ->
            if (row > 0) Spacer(Modifier.width(subTitleSize).height(lineSize).background(Color.DarkGray))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    Modifier.width(subTitleSize).height(cellSize).background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${row + 1}", color = Color.White)
                }
                repeat(BOARD_DIM) { col ->
                    if (col > 0) Spacer(Modifier.width(lineSize).height(cellSize).background(Color.Black))
                    val pos = Cell(row, col)
                    if (board == null) CellView(null)
                    else CellView(
                        board.moves[pos],
                        toFlip = toFlip(pos),
                        targets = canPlay(pos) && delayTargets,
                        targetsOn = targetsOn,
                        onClick = { onClick(pos) })
                }
            }
        }
    }
}


@Composable
fun CellView(
    player: Player?,
    modifier: Modifier = Modifier.size(cellSize).background(Color.Green),
    toFlip: Boolean = false,
    targets: Boolean = false,
    targetsOn: Boolean = false,
    onClick: () -> Unit = {}
) {
    if (player == null) {
        val mod = if (targets || !targetsOn) modifier.clickable(onClick = onClick) else modifier
        Box(modifier = mod, contentAlignment = Alignment.Center) {
            if (targets) TargetView()
        }
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            var index by remember(player) { mutableStateOf(animationList.size - 1) }
            val image = when (player) {
                Player.BLACK -> animationList.reversed()[index]
                Player.WHITE -> animationList[index]
            }
            Image(bitmap = image, modifier = modifier, contentDescription = image.toString())
            LaunchedEffect(player) {
                if (toFlip) {
                    while (index > 0) {
                        delay(30)
                        index -= 1
                    }
                    index = 0
                } else index = 0
            }
        }
    }
}

@Composable
fun TargetView() =
    Box(Modifier.clip(CircleShape).fillMaxSize(0.25f).background(Color.Yellow))


