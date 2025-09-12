package application.ports.input

import domain.dtos.GameRequest
import domain.models.Game

interface TennisAwardPointPort {
    fun awardTennisPoint(gameRequest: GameRequest, toServer: Boolean): Game?
}
