package application.ports.`in`

import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.dtos.GameScoreResponse

interface TennisScoreFormatPort {
    fun getFormattedScore(gameRequest: GameRequest, gameRepository: GamePort, playerRepository: PlayerPort): GameScoreResponse?
}