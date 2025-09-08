package application.ports.`in`

import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Game

interface TennisAwardPointPort {
    fun awardTennisPoint(gameRequest: GameRequest, gameRepository: GamePort, playerRepository: PlayerPort, toServer: Boolean): Game?
}