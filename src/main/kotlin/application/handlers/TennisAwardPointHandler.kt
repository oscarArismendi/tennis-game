package org.example.application.handlers

import org.example.application.ports.`in`.TennisAwardPointPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.models.Game

class TennisAwardPointHandler (val tennisAwardPointService: TennisAwardPointPort,
                               val gameRepository: GamePort,
                               val playerRepository: PlayerPort,
) {
    fun toServer(gameRequest: GameRequest): Game?{
        val game = tennisAwardPointService.awardTennisPoint(gameRequest, gameRepository, playerRepository,true)
        if(game != null){
            return game
        }
        println("Error awarding the point!")
        return null
    }

    fun toReceiver(gameRequest: GameRequest): Game?{
        val game = tennisAwardPointService.awardTennisPoint(gameRequest, gameRepository, playerRepository,false)
        if(game != null){
            return game
        }
        println("Error awarding the point!")
        return null
    }

}