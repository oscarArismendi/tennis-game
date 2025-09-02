package application.adapters

import io.kotest.matchers.shouldBe
import org.example.application.adapters.TennisScoreFormatAdapter
import org.example.application.adapters.TennisScoreResetAdapter
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse
import org.example.domain.models.Advantage
import org.example.domain.models.Game
import org.example.domain.models.GameState
import org.example.domain.models.Player
import org.example.infrastructure.MySQLGameRepository
import org.example.infrastructure.MySQLPlayerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TennisScoreResetAdapterTest {

    val mysqlGameRepository: GamePort = MySQLGameRepository()
    val mysqlPlayerRepository: PlayerPort = MySQLPlayerRepository()
    // Integration test
    @Test
    fun `when the score is 40-40 and advantage is with the server,reset, go to initial state`() {
        //Given
        val tennisScoreResetAdapter = TennisScoreResetAdapter()
        val serverPlayer = Player(
            id = 1000,
            firstname = "Server",
            lastname="Player",
            email = "dummyServer@dummy.com"
        )
        val receiverPlayer = Player(
            id =10001,
            firstname = "Receiver",
            lastname= "Player",
            email = "dummyPlayer@dummy.com"
        )
        val currentGame = Game(
            id = 1,
            serverId = serverPlayer.id,
            receiverId = receiverPlayer.id,
            serverScore = 40,
            receiverScore = 40,
            advantage = Advantage.AD_IN,
            state = GameState.ENDED
        )
        // To force error if it does not exists
        var player1Id: Long = 0
        var player2Id: Long = 0
        if(mysqlPlayerRepository.findPlayerByEmail(serverPlayer.email) == null){
            player1Id = mysqlPlayerRepository.savePlayer(serverPlayer)!!.id
        }else{
            player1Id = mysqlPlayerRepository.findPlayerByEmail(serverPlayer.email)!!.id
        }
        if(mysqlPlayerRepository.findPlayerByEmail(receiverPlayer.email) == null){
            player2Id = mysqlPlayerRepository.savePlayer(receiverPlayer)!!.id
        }else{
            player2Id = mysqlPlayerRepository.findPlayerByEmail(receiverPlayer.email)!!.id
        }
        if(mysqlGameRepository.findGameByPlayersIds(player1Id,player2Id)==null){
            currentGame.serverId = player1Id
            currentGame.receiverId = player2Id
            mysqlGameRepository.saveGame(currentGame)
        }
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyPlayer@dummy.com")
        //When
        val result: Game? = tennisScoreResetAdapter.resetTennisScore(dummyGameRequest,mysqlGameRepository, mysqlPlayerRepository)
        //Then
        result?.serverScore shouldBe 0
        result?.receiverScore shouldBe 0
        result?.advantage shouldBe Advantage.NONE
        result?.state shouldBe GameState.NOT_STARTED
    }

}