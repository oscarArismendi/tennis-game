package application.adapters

import application.ports.input.TennisAwardPointPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState
import utils.ValidationUtils
import utils.errors.ScoreLookUpError

class TennisAwardPointAdapter(
    val gameRepository: GamePort,
    val playerRepository: PlayerPort,
) : TennisAwardPointPort {
    override fun awardTennisPoint(gameRequest: GameRequest, toServer: Boolean): Game? {
        try {
            val player1 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.serverEmail, playerRepository)
            val player2 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.receiverEmail, playerRepository)
            val game = ValidationUtils.findGameByPlayersOrThrow(player1, player2, gameRepository)

            if (isGameEnded(game)) {
                return null
            }

            addPointToPlayer(game, gameRepository, toServer)
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

    fun isGameEnded(game: Game): Boolean {
        return game.state == GameState.ENDED
    }

    fun isMatchPoint(game: Game): Boolean {
        return if (game.state == GameState.ENDED) {
            false
        } else if (game.serverScore == 40 && game.receiverScore < 40) {
            true
        } else if (game.serverScore == 40 && game.receiverScore == 40) {
            game.advantage != Advantage.NONE
        } else {
            false
        }
    }

    fun getMatchPointPlayerId(game: Game): Long {
        return if (game.serverScore > game.receiverScore) {
            game.serverId
        } else if (game.serverScore == 40 && game.receiverScore == 40) {
            when (game.advantage) {
                Advantage.AD_IN -> game.serverId
                Advantage.AD_OUT -> game.receiverId
                Advantage.NONE -> 0
            }
        } else {
            game.receiverId
        }
    }

    fun isDeuce(game: Game): Boolean {
        val scoreAreEqual = game.serverScore == game.receiverScore
        val isScoreForty = game.serverScore == 40
        val noAdvantage = game.advantage == Advantage.NONE
        val bool = game.state != GameState.ENDED
        return scoreAreEqual && isScoreForty && noAdvantage && bool
    }

    fun addPointToPlayer(game: Game, gameRepository: GamePort, toServer: Boolean): Game? {
        val playerId = if (toServer) game.serverId else game.receiverId
        val advantageValue = if (toServer) Advantage.AD_IN else Advantage.AD_OUT
        val advantageString = if (toServer) "AD_IN" else "AD_OUT"
        if (isMatchPoint(game)) {
            if (getMatchPointPlayerId((game)) == playerId) {
                game.state = GameState.ENDED
                val columnsToUpdate = "state = 'ENDED'"
                if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
                    return game
                }
            }
        }
        if (isDeuce(game)) {
            game.advantage = advantageValue
            val columnsToUpdate = "advantage = '$advantageString'"
            if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
                return game
            }
        }
        var newScore = if (toServer) game.serverScore else game.receiverScore
        when (newScore) {
            30 -> newScore = 40
            else -> newScore += 15
        }

        if (toServer) {
            game.serverScore = newScore
        } else {
            game.receiverScore = newScore
        }

        val columnString = if (toServer) "server_score" else "receiver_score"
        val columnsToUpdate = "$columnString = $newScore"
        if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
            return game
        }
        return null
    }
}
