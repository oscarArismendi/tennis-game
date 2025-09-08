package application.adapters

import application.ports.`in`.TennisScoreResetPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import application.support.ScoreLookUpError
import domain.dtos.GameRequest
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState

class TennisScoreResetAdapter: TennisScoreResetPort {
    override fun resetTennisScore(
        gameRequest: GameRequest,
        gameRepository: GamePort,
        playerRepository: PlayerPort
    ): Game? {
        try {
            val player1 = playerRepository.findPlayerByEmail(gameRequest.serverEmail)
            if(player1 == null){
                throw ScoreLookUpError.PlayerNotFound(gameRequest.serverEmail)
            }
            val player2 = playerRepository.findPlayerByEmail(gameRequest.receiverEmail)
            if(player2 == null){
                throw ScoreLookUpError.PlayerNotFound(gameRequest.receiverEmail)
            }
            val game =gameRepository.findGameByPlayersIds(player1.id,player2.id)
            if(game == null){
                throw ScoreLookUpError.GameNotFound(player1.email,player2.email)
            }
            val columnsToReset = "server_score = 0, receiver_score = 0, advantage = 'NONE', state = 'NOT_STARTED'"
            if(gameRepository.updateGameById(columnsToReset,game.id)){
                game.serverScore = 0
                game.receiverScore = 0
                game.advantage = Advantage.NONE
                game.state = GameState.NOT_STARTED
                return  game
            }
        }catch (e: ScoreLookUpError) {
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