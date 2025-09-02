package org.example.application.adapters

import org.example.application.ports.`in`.TennisAwardPointPort
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.models.Advantage
import org.example.domain.models.Game
import org.example.domain.models.GameState

class TennisAwardPointAdapter: TennisAwardPointPort {
    override fun awardTennisPoint(
        gameRequest: GameRequest,
        gameRepository: GamePort,
        playerRepository: PlayerPort,
        toServer: Boolean
    ): Game? {
        try {
            val player1 = playerRepository.findPlayerByEmail(gameRequest.serverEmail)
            if(player1 == null){
                throw Exception("Player with email ${gameRequest.serverEmail} not found")
            }
            val player2 = playerRepository.findPlayerByEmail(gameRequest.receiverEmail)
            if(player2 == null){
                throw Exception("Player with email ${gameRequest.receiverEmail} not found")
            }
            val game =gameRepository.findGameByPlayersIds(player1.id,player2.id)
            if(game == null){
                throw Exception("Game not found for players with emails  ${player1.email} and ${player2.email}")
            }

            if(isGameEnded(game)){
                return null
            }

            if(toServer){
                return addPointToTheServer(game,gameRepository)
            }else{
                return addPointToTheReceiver(game,gameRepository)
            }
        }catch (e: Exception) {
            println("Error at formatting the score: " + e.message)
        }

        return null
    }

    fun isGameEnded(game: Game): Boolean{
        return game.state == GameState.ENDED
    }

    fun isMatchPoint(game: Game): Boolean{
        return if(game.state == GameState.ENDED){
             false
        }else if(game.serverScore == 40 && game.receiverScore < 40){
             true
        }else if(game.serverScore == 40 && game.receiverScore == 40){
             game.advantage != Advantage.NONE
        }else{
             false
        }
    }

    fun getMatchPointPlayerId(game: Game): Long{
        return if(game.serverScore > game.receiverScore){
             game.serverId
        }else if(game.serverScore == 40 && game.receiverScore == 40){
             when(game.advantage){
                Advantage.AD_IN -> game.serverId
                Advantage.AD_OUT -> game.receiverId
                Advantage.NONE -> 0
            }
        }else{
             game.receiverId
        }
    }

    fun isDeuce(game: Game): Boolean{
        return game.serverScore == game.receiverScore && game.serverScore == 40 && game.advantage == Advantage.NONE && game.state != GameState.ENDED
    }

    fun addPointToTheServer(game:Game, gameRepository: GamePort): Game? {
        if (isMatchPoint(game)) {
            if (getMatchPointPlayerId((game)) == game.serverId) {
                game.state = GameState.ENDED
                val columnsToUpdate = "state = 'ENDED'"
                if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
                    return game
                }
            }
        }
        if (isDeuce(game)) {
            game.advantage = Advantage.AD_IN
            val columnsToUpdate = "advantage = 'AD_IN'"
            if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
                return game
            }
        }
        var newScore = game.serverScore
        when (game.serverScore) {
            30 -> newScore = 40
            else -> newScore += 15
        }
        game.serverScore = newScore
        val columnsToUpdate = "server_score = $newScore"
        if (gameRepository.updateGameById(columnsToUpdate, game.id)) {
            return game
        }
        return null
    }

        fun addPointToTheReceiver(game:Game, gameRepository: GamePort): Game?{
            if(isMatchPoint(game)){
                if(getMatchPointPlayerId((game)) == game.receiverId){
                    game.state = GameState.ENDED
                    val columnsToUpdate = "state = 'ENDED'"
                    if(gameRepository.updateGameById(columnsToUpdate,game.id)){
                        return game
                    }
                }
            }
            if(isDeuce(game)){
                game.advantage = Advantage.AD_OUT
                val columnsToUpdate = "advantage = 'AD_OUT'"
                if(gameRepository.updateGameById(columnsToUpdate,game.id)){
                    return game
                }
            }
            var newScore = game.receiverScore
            when(game.receiverScore){
                30 -> newScore = 40
                else -> newScore += 15
            }
            game.receiverScore = newScore
            val columnsToUpdate = "receiver_score = $newScore"
            if(gameRepository.updateGameById(columnsToUpdate,game.id)){
                return game
            }
            return null
    }


}