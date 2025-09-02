package org.example.application.ports.`in`

import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.models.Game

interface TennisAwardPointPort {
    fun awardTennisPoint(gameRequest: GameRequest, gameRepository: GamePort, playerRepository: PlayerPort, toServer: Boolean): Game?
}