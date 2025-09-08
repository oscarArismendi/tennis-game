package application.handlers

import application.ports.`in`.TennisScoreFormatPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.dtos.GameScoreResponse

class TennisScoreFormatHandler(val tennisScoreFormatService: TennisScoreFormatPort,
                               val gameRepository: GamePort,
                               val playerRepository: PlayerPort
) {
    fun getFormattedScore(gameRequest: GameRequest): GameScoreResponse? {
        val gameScore = tennisScoreFormatService.getFormattedScore(gameRequest, gameRepository, playerRepository)
        println(gameScore?.formatScore)
        return gameScore
    }
}