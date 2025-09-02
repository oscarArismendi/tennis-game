package org.example.application.handlers

import org.example.application.ports.`in`.TennisScoreFormatPort
import org.example.application.ports.`in`.TennisScoreResetPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse
import org.example.domain.models.Game

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