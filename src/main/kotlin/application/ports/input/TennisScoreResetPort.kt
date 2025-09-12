package application.ports.input

import domain.dtos.GameRequest
import domain.models.Game

interface TennisScoreResetPort {
    fun resetTennisScore(gameRequest: GameRequest): Game?
}
