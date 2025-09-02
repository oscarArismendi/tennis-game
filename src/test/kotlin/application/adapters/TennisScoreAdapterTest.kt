package application.adapters

import org.example.application.adapters.TennisScoreFormatAdapter
import io.kotest.matchers.shouldBe
import org.example.application.ports.out.GamePort
import org.example.application.ports.out.PlayerPort
import org.example.domain.dtos.GameRequest
import org.example.domain.dtos.GameScoreResponse
import org.example.domain.models.Advantage
import org.example.domain.models.Game
import org.example.domain.models.GameState
import org.example.domain.models.Player
import org.junit.jupiter.api.Test

class TennisScoreFormatAdapterTest {

    val deuceGameStubRepository = object : GamePort {
        override fun saveGame(game: Game): Game? {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return null
        }

        override fun updateGameById(updateColumns: String, gameId: Long): Boolean {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return false
        }

        override fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game? {
            return Game(
                id = 1,
                serverId = 1,
                receiverId = 2,
                serverScore = 40,
                receiverScore = 40,
                advantage = Advantage.NONE,
                state = GameState.IN_PROGRESS
            )
        }
    }

    val notStartedGameStubRepository = object : GamePort{
        override fun saveGame(game: Game): Game? {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return null
        }

        override fun updateGameById(updateColumns: String, gameId: Long): Boolean {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return false
        }

        override fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game? {
            return Game(
                id = 1,
                serverId = 1,
                receiverId = 2,
                serverScore = 0,
                receiverScore = 0,
                advantage = Advantage.NONE ,
                state = GameState.NOT_STARTED
            )
        }
    }

    val adInGameStubRepository = object : GamePort{
        override fun saveGame(game: Game): Game? {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return null
        }

        override fun updateGameById(updateColumns: String, gameId: Long): Boolean {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return false
        }

        override fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game? {
            return Game(
                id = 1,
                serverId = 1,
                receiverId = 2,
                serverScore = 40,
                receiverScore = 40,
                advantage = Advantage.AD_IN,
                state = GameState.IN_PROGRESS
            )
        }
    }

    val adOutGameStubRepository = object : GamePort {
        override fun saveGame(game: Game): Game? {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return null
        }

        override fun updateGameById(updateColumns: String, gameId: Long): Boolean {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return false
        }

        override fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game?{
            return Game(
                id = 1,
                serverId = 1,
                receiverId = 2,
                serverScore = 40,
                receiverScore = 40,
                advantage = Advantage.AD_OUT,
                state = GameState.IN_PROGRESS
            )
        }
    }

    val playerStubRepository = object : PlayerPort{

        override fun savePlayer(player: Player): Player? {
            // NOT IMPLEMENTED FOR TESTING PURPOSES
            return null
        }
        override fun findPlayerByEmail(email: String): Player? {
            return when (email) {
                "dummyServer@dummy.com" -> Player(id = 1, firstname = "Server", lastname="Player", email = "dummyServer@dummy.com")
                "dummyPlayer@dummy.com" -> Player(id = 2, firstname = "Receiver", lastname= "Player", email = "dummyPlayer@dummy.com")
                else -> null
            }
        }
    }

    @Test
    fun `when the score is 40-40 returns deuce`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyPlayer@dummy.com")
        //When
        val result: GameScoreResponse? = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,deuceGameStubRepository, playerStubRepository)
        //Then
        result?.formatScore shouldBe "Deuce"
    }

    @Test
    fun `when the score is 0-0 returns Love-all`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyPlayer@dummy.com")
        //When
        val result: GameScoreResponse? = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,notStartedGameStubRepository, playerStubRepository)
        //Then
        result?.formatScore shouldBe "Love-all"
    }

    @Test
    fun `when the score is 40-40 and advantage is with the server returns 'Advantage server name'`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyPlayer@dummy.com")
        //When
        val result: GameScoreResponse? = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,adInGameStubRepository,playerStubRepository)
        //Then
        result?.formatScore shouldBe "Advantage Server Player"
    }

    @Test
    fun `when the score is 40-40 and advantage is with the receiver returns 'Advantage receiver name'`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyPlayer@dummy.com")
        //When
        val result: GameScoreResponse? = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,adOutGameStubRepository,playerStubRepository)
        //Then
        result?.formatScore shouldBe "Advantage Receiver Player"
    }
}