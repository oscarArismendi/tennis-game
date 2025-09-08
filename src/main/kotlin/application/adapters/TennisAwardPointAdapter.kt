package application.adapters

import application.ports.`in`.TennisAwardPointPort
import application.ports.out.GamePort
import application.ports.out.PlayerPort
import application.support.ScoreLookUpError
import domain.dtos.GameRequest
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState

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
                throw ScoreLookUpError.PlayerNotFound(gameRequest.serverEmail)
            }
            val player2 = playerRepository.findPlayerByEmail(gameRequest.receiverEmail)
            if(player2 == null){
                throw ScoreLookUpError.PlayerNotFound(gameRequest.receiverEmail)
            }
            val game =gameRepository.findGameByPlayersIds(player1.id,player2.id)
            if(game == null){
                throw ScoreLookUpError.GameNotFound(player1.email,player2.email)
            }

            if(isGameEnded(game)){
                return null
            }

            if(toServer){
                return addPointToTheServer(game,gameRepository)
            }else{
                return addPointToTheReceiver(game,gameRepository)
            }
        }catch (e: ScoreLookUpError) {
            when (e) {
                is ScoreLookUpError.PlayerNotFound -> {
                    println("Player not found: ${e.message}")
                }
                is ScoreLookUpError.GameNotFound -> {
                    println("Game not found: ${e.message}")
                }
            }
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