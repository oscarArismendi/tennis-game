package application.handlers

import application.ports.`in`.TennisAwardPointPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import domain.dtos.GameRequest
import domain.models.Game

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