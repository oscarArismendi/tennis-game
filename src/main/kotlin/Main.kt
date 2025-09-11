
import application.adapters.TennisAwardPointAdapter
import application.adapters.TennisScoreFormatAdapter
import application.adapters.TennisScoreResetAdapter
import application.ports.`in`.TennisAwardPointPort
import application.ports.`in`.TennisScoreFormatPort
import application.ports.`in`.TennisScoreResetPort
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
    val tennisScoreFormatService = TennisScoreFormatAdapter(mySQLGameRepository, mySQLPlayerRepository)
    val tennisScoreResetService = TennisScoreResetAdapter(mySQLGameRepository, mySQLPlayerRepository)
    val tennisAwardPointService = TennisAwardPointAdapter(mySQLGameRepository, mySQLPlayerRepository)

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
    resetGame(nadalVSDjokovicGameRequest,tennisScoreResetService,tennisScoreFormatService)

    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,false)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,false)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,false)


    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,true)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,true)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,true)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,true)
    addPoint(nadalVSDjokovicGameRequest,tennisScoreFormatService,tennisAwardPointService,true)

    resetGame(nadalVSDjokovicGameRequest,tennisScoreResetService,tennisScoreFormatService)
}

fun addPoint(gameRequest: GameRequest, tennisScoreFormatService: TennisScoreFormatPort, tennisAwardPointService: TennisAwardPointPort,toServer: Boolean){
    val scoringPlayer = if(toServer) "Server" else "Receiver"
    println("Point for the $scoringPlayer:")
    tennisAwardPointService.awardTennisPoint(gameRequest, toServer)
    val scoreResponse = tennisScoreFormatService.getFormattedScore(gameRequest)
    println("${scoreResponse?.formatScore}")
}

fun resetGame(gameRequest: GameRequest, tennisScoreResetService: TennisScoreResetPort,tennisScoreFormatService: TennisScoreFormatPort){
    println("Resetting Game")
    tennisScoreResetService.resetTennisScore(gameRequest)
    val scoreResponse = tennisScoreFormatService.getFormattedScore(gameRequest)
    println("${scoreResponse?.formatScore}")
}