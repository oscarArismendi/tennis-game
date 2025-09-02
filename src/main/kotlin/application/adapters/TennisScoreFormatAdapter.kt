package org.example.application.adapters

import org.example.application.ports.`in`.TennisScoreFormatPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse
import org.example.domain.models.Advantage
import org.example.domain.models.Game
import org.example.domain.models.GameState
import org.example.domain.models.Player

class TennisScoreFormatAdapter: TennisScoreFormatPort {
    override fun getFormattedScore(
        gameRequest: GameRequest,
        gameRepository: GamePort,
        playerRepository: PlayerPort
    ): GameScoreResponse? {
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
            val formattedScore = formatScore(player1,player2,game)
            return GameScoreResponse(game.serverScore, game.receiverScore,formattedScore)
        }catch (e: Exception) {
            println("Error at formatting the score: " + e.message)
        }

        return null
    }

    fun formatScore(serverPlayer:Player, receiverPlayer: Player,game: Game): String{
        if(game.serverScore == game.receiverScore){
            return if(game.serverScore == 40 && game.state == GameState.ENDED){
                when(game.advantage){
                    Advantage.AD_IN ->  "Winner ${serverPlayer.firstname} ${serverPlayer.lastname}"
                    Advantage.AD_OUT ->  "Winner ${receiverPlayer.firstname} ${receiverPlayer.lastname}"
                    Advantage.NONE ->  "Game ended without a winner"
                }
            }else if(game.serverScore == 40 && game.state == GameState.IN_PROGRESS){
                when(game.advantage){
                    Advantage.AD_IN -> "Advantage ${serverPlayer.firstname} ${serverPlayer.lastname}"
                    Advantage.AD_OUT -> "Advantage ${receiverPlayer.firstname} ${receiverPlayer.lastname}"
                    Advantage.NONE -> "Deuce"
                }
            }else{
                return when(game.serverScore){
                    0 -> "Love-all"
                    15 -> "15-all"
                    30 -> "30-all"
                    else ->"Deuce"
                }
            }
        }else{
            "${game.serverScore}-${game.receiverScore}"
        }
        return "Couldn't format ${game.serverScore}-${game.receiverScore} with advantage ${game.advantage} and state ${game.state}"
    }
}