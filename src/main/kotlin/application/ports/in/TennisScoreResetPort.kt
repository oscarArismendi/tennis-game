package application.ports.`in`

import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Game

interface TennisScoreResetPort {
    fun resetTennisScore(gameRequest: GameRequest, gameRepository: GamePort, playerRepository: PlayerPort): Game?
}