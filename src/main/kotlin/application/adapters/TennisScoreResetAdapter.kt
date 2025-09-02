package org.example.application.adapters

import org.example.application.ports.`in`.TennisScoreResetPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.models.Advantage
import org.example.domain.models.Game
import org.example.domain.models.GameState

class TennisScoreResetAdapter: TennisScoreResetPort {
    override fun resetTennisScore(
        gameRequest: GameRequest,
        gameRepository: GamePort,
        playerRepository: PlayerPort
    ): Game? {
        try {
            val player1 = playerRepository.findPlayerByEmail(gameRequest.serverEmail)
            if(player1 == null){
                throw Exception("Player with email ${gameRequest.serverEmail} not found")
            }
            val player2 = playerRepository.findPlayerByEmail(gameRequest.receiverEmail)
            if(player2 == null){
                throw Exception("Player with email ${gameRequest.receiverEmail} not found")
            }
            val game =gameRepository.findGameByPlayersIds(player1.id,player2.id)
            if(game == null){
                throw Exception("Game not found for players with emails  ${player1.email} and ${player2.email}")
            }
            val columnsToReset = "server_score = 0, receiver_score = 0, advantage = 'NONE', state = 'NOT_STARTED'"
            if(gameRepository.updateGameById(columnsToReset,game.id)){
                game.serverScore = 0
                game.receiverScore = 0
                game.advantage = Advantage.NONE
                game.state = GameState.NOT_STARTED
                return  game
            }
        }catch (e: Exception) {
            println("Error at formatting the score: " + e.message)
        }

        return null
    }
}