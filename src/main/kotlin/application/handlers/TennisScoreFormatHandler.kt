package org.example.application.handlers

import org.example.application.ports.`in`.TennisScoreFormatPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse

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