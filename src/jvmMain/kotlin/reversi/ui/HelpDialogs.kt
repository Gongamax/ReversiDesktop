package pt.ise.tds.reversi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.tds.reversi.ui.boardSize

/**
 * Help dialog.
 * @param onClose the function to be called when the dialog is closed.
 */

val titleSize = 20.sp
val spaceSize = 5.dp
val dialogContentSpaceHorizontally = 8.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenericDialog(
    onClose: () -> Unit,
    title: String,
    content: String,
    isJustify: Boolean = false,
    buttonText: String,

    buttonAction: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.width(boardSize * 0.8f),
        onDismissRequest = onClose,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = dialogContentSpaceHorizontally,
                        vertical = dialogContentSpaceHorizontally * 0.5f
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(vertical = spaceSize))
                    Text(
                        text = content,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = titleSize * 0.75f,
                        textAlign =  if(isJustify) TextAlign.Justify else TextAlign.Center,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.padding(vertical = spaceSize))
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = buttonAction,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = buttonText)
                }
            }
        }
    )
}


@Composable
fun HelpDialog(onClose: () -> Unit) {
    GenericDialog(
        onClose = onClose,
        title = "Reversi Game",
        content = "ISEL\nTécnicas de Desenvolvimento de Software\n\nDeveloped by:\n\nDaniel Carvalho\nFrancisco Saraiva\n Gonçalo Frutuoso\n\n2022/2023",
        buttonText = "Ok",
        buttonAction = onClose
    )
}

@Composable
fun AboutDialog(onClose: () -> Unit, title: String) {
    val uriHandler = LocalUriHandler.current
    GenericDialog(
        onClose = onClose,
        title = title,
        content = "Although it is a simple game, we felt it would be a good idea to provide you the greatest experience possible.\n" +
                "We will send a link with all of the information you need to know about this game.",
        buttonText = "Click here",
        buttonAction = {
            uriHandler.openUri("https://en.wikipedia.org/wiki/Reversi")
            onClose()
        },
        isJustify = true
    )
}