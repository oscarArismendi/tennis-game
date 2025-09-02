package org.example.application.ports.`in`

import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse

interface TennisScoreFormatPort {
    fun getFormattedScore(gameRequest: GameRequest, gameRepository: GamePort, playerRepository: PlayerPort): GameScoreResponse?
}