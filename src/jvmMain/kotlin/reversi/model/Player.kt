package pt.isel.tds.reversi.model


/**
 * Represents a player in the game.
 * @property symbol the symbol that represents the player.
 */
enum class Player(val symbol : Char)  {
    BLACK('#'),
    WHITE('@');
    fun other() = if (this==BLACK) WHITE else BLACK
}

/**
 * Converts a string to a player.
 * @return the player represented by the string or null if the string does not represent a player.
 */
fun String.toPlayerOrNull(): Player? {
    return when (this) {
        Player.BLACK.symbol.toString() -> Player.BLACK
        Player.WHITE.symbol.toString() -> Player.WHITE
        else -> null
    }
}

/**
 * Converts a string to a player.
 * @return the player represented by the string or throws an exception if the string does not represent a player.
 */
fun String.toPlayer() =
    checkNotNull(toPlayerOrNull())