package application.handlers

import application.ports.`in`.TennisScoreResetPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Game

class TennisScoreResetHandler(val tennisScoreResetService: TennisScoreResetPort,
                              val gameRepository: GamePort,
                              val playerRepository: PlayerPort
) {
    fun resetGame(gameRequest: GameRequest): Game?{
        val game = tennisScoreResetService.resetTennisScore(gameRequest, gameRepository, playerRepository)
        if(game != null){
            println("The game has been reset!")
            return game
        }
        println("The game couldn't be reset!")
        return null
    }
}