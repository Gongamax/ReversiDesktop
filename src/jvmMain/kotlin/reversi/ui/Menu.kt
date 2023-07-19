package pt.isel.tds.reversi.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import pt.ise.tds.reversi.ui.Dialog
import pt.ise.tds.reversi.ui.ReversiViewModel
import pt.isel.tds.reversi.model.MultiPlayer

@Composable
fun FrameWindowScope.ReversiMenu(vm: ReversiViewModel, onExit: () -> Unit) = MenuBar {
    Menu("Game") {
        Item("New", onClick = { vm.openDialog(Dialog.NEW_GAME) })
        Item("Join", onClick = { vm.openDialog(Dialog.JOIN_GAME) })
        Item("Refresh", enabled = vm.canRefresh, onClick = vm::refreshGame)
        Item("Exit", onClick = onExit)
    }
    Menu("Play") {
        Item("Pass", onClick = vm::pass, enabled = vm.isPass())
    }
    Menu("Options") {
        CheckboxItem("Show targets", checked = vm.targetsOn, onCheckedChange = { vm.targets(vm.targetsOn) })
        if (vm.game is MultiPlayer)
            CheckboxItem("Auto-refresh", checked = vm.autoRefresh, onCheckedChange = { vm.toggleAutoRefresh() })
    }
    Menu("Help") {
        Item("Rules", onClick = { vm.openDialog(Dialog.ABOUT) })
        Item("About", onClick = { vm.openDialog(Dialog.HELP) })
    }
}