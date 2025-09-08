
import application.adapters.TennisAwardPointAdapter
import application.adapters.TennisScoreFormatAdapter
import application.adapters.TennisScoreResetAdapter
import application.handlers.TennisAwardPointHandler
import application.handlers.TennisScoreFormatHandler
import application.handlers.TennisScoreResetHandler
import domain.dtos.GameRequest
import domain.models.Game
import domain.models.Player
import infrastructure.MySQLGameRepository
import infrastructure.MySQLPlayerRepository

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

    val nadalVSDjokovicGame: Game? = mySQLGameRepository.findGameByPlayersIds(1, 2)
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