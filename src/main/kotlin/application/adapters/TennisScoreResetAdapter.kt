package application.adapters

import application.ports.input.TennisScoreResetPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState
import utils.ValidationUtils
import utils.errors.ScoreLookUpError

class TennisScoreResetAdapter(
    val gameRepository: GamePort,
    val playerRepository: PlayerPort,
) : TennisScoreResetPort {
    override fun resetTennisScore(gameRequest: GameRequest): Game? {
        try {
            val player1 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.serverEmail, playerRepository)
            val player2 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.receiverEmail, playerRepository)
            val game = ValidationUtils.findGameByPlayersOrThrow(player1, player2, gameRepository)
            val columnsToReset = "server_score = 0, receiver_score = 0, advantage = 'NONE', state = 'NOT_STARTED'"
            if (gameRepository.updateGameById(columnsToReset, game.id)) {
                game.serverScore = 0
                game.receiverScore = 0
                game.advantage = Advantage.NONE
                game.state = GameState.NOT_STARTED
                return game
            }
        } catch (e: ScoreLookUpError) {
            when (e) {
                is ScoreLookUpError.PlayerNotFound -> {
                    println("Player not found: ${e.message}")
                }

                is ScoreLookUpError.GameNotFound -> {
                    println("Game not found: ${e.message}")
                }
            }
        }

        return null
    }
}
