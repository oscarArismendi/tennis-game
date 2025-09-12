package utils

import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.models.Game
import domain.models.Player
import utils.errors.ScoreLookUpError

object ValidationUtils {
    fun findPlayerByEmailOrThrow(email: String, playerRepository: PlayerPort): Player  {
        val player = playerRepository.findPlayerByEmail(email)
        if (player == null) {
            throw ScoreLookUpError.PlayerNotFound(email)
        }
        return player
    }

    fun findGameByPlayersOrThrow(server: Player, receiver: Player, gameRepository: GamePort): Game  {
        val game = gameRepository.findGameByPlayersIds(server.id, receiver.id)
        if (game == null) {
            throw ScoreLookUpError.GameNotFound(server.email, receiver.email)
        }
        return game
    }
}
