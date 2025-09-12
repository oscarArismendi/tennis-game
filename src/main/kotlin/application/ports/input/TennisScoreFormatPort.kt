package application.ports.input

import domain.dtos.GameRequest
import domain.dtos.GameScoreResponse

interface TennisScoreFormatPort {
    fun getFormattedScore(gameRequest: GameRequest): GameScoreResponse?
}
