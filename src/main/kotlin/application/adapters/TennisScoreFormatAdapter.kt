package application.adapters

import application.ports.`in`.TennisScoreFormatPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import utils.errors.ScoreLookUpError
import domain.dtos.GameRequest
import domain.dtos.GameScoreResponse
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState
import domain.models.Player
import utils.ValidationUtils

class TennisScoreFormatAdapter(
                               val gameRepository: GamePort,
                               val playerRepository: PlayerPort
): TennisScoreFormatPort {
    override fun getFormattedScore(
        gameRequest: GameRequest
    ): GameScoreResponse? {
        try {
            val player1 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.serverEmail,playerRepository)
            val player2 = ValidationUtils.findPlayerByEmailOrThrow(gameRequest.receiverEmail,playerRepository)
            val game = ValidationUtils.findGameByPlayersOrThrow(player1,player2,gameRepository)
            val formattedScore = formatScore(player1,player2,game)
            return GameScoreResponse(game.serverScore, game.receiverScore,formattedScore)
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

    fun formatScore(serverPlayer:Player, receiverPlayer: Player,game: Game): String{
        if(game.serverScore == game.receiverScore){
            return if(game.serverScore == 40 && game.state == GameState.ENDED){
                when(game.advantage){
                    Advantage.AD_IN ->  "Winner ${serverPlayer.firstname} ${serverPlayer.lastname}"
                    Advantage.AD_OUT ->  "Winner ${receiverPlayer.firstname} ${receiverPlayer.lastname}"
                    Advantage.NONE ->  "Game ended without a winner"
                }
            }else if(game.serverScore == 40 && game.state != GameState.ENDED){
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
            return "${game.serverScore}-${game.receiverScore}"
        }
    }
}