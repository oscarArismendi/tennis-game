package org.example

import org.example.application.adapters.TennisAwardPointAdapter
import org.example.application.adapters.TennisScoreFormatAdapter
import org.example.application.adapters.TennisScoreResetAdapter
import org.example.application.handlers.TennisAwardPointHandler
import org.example.application.handlers.TennisScoreFormatHandler
import org.example.application.handlers.TennisScoreResetHandler
import org.example.domain.dtos.GameRequest
import org.example.domain.models.Game
import org.example.domain.models.Player
import org.example.infrastructure.MySQLGameRepository
import org.example.infrastructure.MySQLPlayerRepository

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    //repositories definition
    val mySQLPlayerRepository = MySQLPlayerRepository()
    val mySQLGameRepository = MySQLGameRepository()

    //adapters definitions
    val tennisScoreFormatService = TennisScoreFormatAdapter()
    val tennisScoreResetService = TennisScoreResetAdapter()
    val tennisAwardPointService = TennisAwardPointAdapter()

    //handler definition
    val tennisScoreFormatHandler = TennisScoreFormatHandler(tennisScoreFormatService, mySQLGameRepository, mySQLPlayerRepository)
    val tennisScoreResetHandler = TennisScoreResetHandler(tennisScoreResetService, mySQLGameRepository, mySQLPlayerRepository )
    val tennisAwardPointHandler = TennisAwardPointHandler(tennisAwardPointService, mySQLGameRepository, mySQLPlayerRepository)

    val nadal = mySQLPlayerRepository.findPlayerByEmail("rafa.nadal@example.com")
    val djokovic = mySQLPlayerRepository.findPlayerByEmail("novak.djokovic@example.com")
    println("${nadal?.lastname} playing against ${djokovic?.lastname}")


    val newPlayer = Player(0, "Roger", "Federer", "roger.federer@example.com")
    var federer = mySQLPlayerRepository.findPlayerByEmail(newPlayer.email)
    if (federer == null) {
        federer = mySQLPlayerRepository.savePlayer(newPlayer)
    }

    println("${federer?.lastname} is waiting")

    var nadalVSDjokovicGame: Game? = mySQLGameRepository.findGameByPlayersIds(1, 2)
    println("Find game with id: ${nadalVSDjokovicGame?.id} with server id: ${nadalVSDjokovicGame?.serverId} and receiver id: ${nadalVSDjokovicGame?.receiverId} \n${nadalVSDjokovicGame?.serverScore}-${nadalVSDjokovicGame?.receiverScore} the advantage is ${nadalVSDjokovicGame?.advantage?.message} and state: ${nadalVSDjokovicGame?.state?.message} ")

    val nadalVSDjokovicGameRequest = GameRequest("rafa.nadal@example.com", "novak.djokovic@example.com")
    tennisScoreResetHandler.resetGame(nadalVSDjokovicGameRequest)
    println("Score:")
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After One point to Receiver:")
    tennisAwardPointHandler.toReceiver(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Receiver:")
    tennisAwardPointHandler.toReceiver(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Receiver:")
    tennisAwardPointHandler.toReceiver(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Server:")
    tennisAwardPointHandler.toServer(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Server:")
    tennisAwardPointHandler.toServer(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Server:")
    tennisAwardPointHandler.toServer(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Server:")
    tennisAwardPointHandler.toServer(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After another point to Server:")
    tennisAwardPointHandler.toServer(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)

    println("Score After reset:")
    tennisScoreResetHandler.resetGame(nadalVSDjokovicGameRequest)
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)
}