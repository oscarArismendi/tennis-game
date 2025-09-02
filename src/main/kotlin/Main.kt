package org.example

import org.example.application.adapters.TennisScoreFormatAdapter
import org.example.application.adapters.TennisScoreResetAdapter
import org.example.application.handlers.TennisScoreFormatHandler
import org.example.application.handlers.TennisScoreResetHandler
import org.example.domain.dtos.GameRequest
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

    //handler definition
    val tennisScoreFormatHandler = TennisScoreFormatHandler(tennisScoreFormatService, mySQLGameRepository, mySQLPlayerRepository)
    val tennisScoreResetHandler = TennisScoreResetHandler(tennisScoreResetService, mySQLGameRepository, mySQLPlayerRepository )

    val nadal = mySQLPlayerRepository.findPlayerByEmail("rafa.nadal@example.com")
    val djokovic = mySQLPlayerRepository.findPlayerByEmail("novak.djokovic@example.com")
    println("${nadal?.lastname} playing against ${djokovic?.lastname}")


    val newPlayer = Player(0, "Roger", "Federer", "roger.federer@example.com")
    var federer = mySQLPlayerRepository.findPlayerByEmail(newPlayer.email)
    if (federer == null) {
        federer = mySQLPlayerRepository.savePlayer(newPlayer)
    }

    println("${federer?.lastname} is waiting")

    val nadalVSDjokovicGame = mySQLGameRepository.findGameByPlayersIds(1, 2)
    println("Find game with id: ${nadalVSDjokovicGame?.id} with server id: ${nadalVSDjokovicGame?.serverId} and receiver id: ${nadalVSDjokovicGame?.receiverId} \n${nadalVSDjokovicGame?.serverScore}-${nadalVSDjokovicGame?.receiverScore} the advantage is ${nadalVSDjokovicGame?.advantage?.message} and state: ${nadalVSDjokovicGame?.state?.message} ")

    val nadalVSDjokovicGameRequest = GameRequest("rafa.nadal@example.com", "novak.djokovic@example.com")

    println("Score:")
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)


    tennisScoreResetHandler.resetGame(nadalVSDjokovicGameRequest)
    println("Score After reset:")
    tennisScoreFormatHandler.getFormattedScore(nadalVSDjokovicGameRequest)
}