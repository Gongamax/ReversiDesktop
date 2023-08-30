package pt.ise.tds.reversi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.tds.reversi.model.Player
import pt.isel.tds.reversi.model.*
import pt.isel.tds.reversi.ui.CellView
import pt.isel.tds.reversi.ui.boardSize
import pt.isel.tds.reversi.ui.cellSize

val barSize = 30.dp
val labelSize = 17.sp
val textSize = 25.sp
val barPiecesSize = 25.dp

@Composable
fun PlayerView(
    label: String,
    currPlayer: Player,
    initialPlayer: Player?,
    score: Pair<Int, Int>? = Pair(0, 0),
    thinking: Boolean,
    modifier: Modifier = Modifier
) =
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        val boxModifier = Modifier.weight(0.05f)
        Column(modifier = boxModifier, horizontalAlignment = Alignment.Start) {
            turnBox(label, currPlayer, boxModifier)
        }
        Column(modifier = boxModifier) {
            if (initialPlayer != null) youBox(boxModifier, initialPlayer)
            if (thinking) {
                Row {
                    Text("Thinking  ", textAlign = TextAlign.Center, style = MaterialTheme.typography.h6)
                    CircularProgressIndicator(modifier = Modifier.size(cellSize * 0.4f), color = Color.Black)
                }
            }
        }
        Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
            scoreBox(boxModifier, score)
        }
    }

@Composable
fun StatusBar(info: StatusInfo, board: Board?) {
    val mod =
        Modifier.width(boardSize).height(barSize).background(Color.LightGray).padding(1.5.dp)
    Row(
        modifier = mod,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val score = board?.getScore()
        val (label, player, initialPlayer, refreshing, thinking) = info
        if (player != null)
            PlayerView(label, player, initialPlayer, score, thinking)
        else Column(
            modifier = mod,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = textSize, textAlign = TextAlign.Center)
        }
        if (refreshing) Row {
            Text("Refreshing  ", textAlign = TextAlign.Center)
            CircularProgressIndicator(modifier = Modifier.height(cellSize * 0.3f), color = Color.Blue)
        }
    }
}


@Composable
private fun scoreBox(boxModifier: Modifier, score: Pair<Int, Int>?) {
    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${score?.second}",
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            Text(
                "X",
                modifier = Modifier.padding(end = 2.dp),
                fontSize = textSize * 0.4f
            )
            CellView(Player.WHITE, modifier = Modifier.size(barPiecesSize), true)
            Text(
                " ${score?.first}",
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
            Text(
                "X",
                modifier = Modifier.padding(end = 2.dp),
                fontSize = textSize * 0.4f
            )
            CellView(Player.BLACK, modifier = Modifier.size(barPiecesSize), true)
        }
    }
}

@Composable
private fun youBox(boxModifier: Modifier, player: Player) {
    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "You",
                fontSize = labelSize,
                textAlign = TextAlign.Start,
            )
            CellView(player, modifier = Modifier.size(barPiecesSize))
        }
    }
}


@Composable
private fun turnBox(label: String, player: Player, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontSize = labelSize)
            CellView(player, modifier = Modifier.size(barPiecesSize), true)
        }
    }
}
