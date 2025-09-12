package application.ports.out

import domain.models.Game

interface GamePort {
    fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game?
    fun saveGame(game: Game): Game?
    fun updateGameById(updateColumns: String, gameId: Long): Boolean
}
