package application

import org.example.application.TennisScoreFormatAdapter
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TennisScoreFormatAdapterTest {

    val deuceGameStubRepository: GamePort {
        override fun saveGame(){
            // NOT IMPLEMENTED FOR TESTING PURPOSES
        }
        override fun findByPlayers(serverEmail: String, receiverEmail: String): Game? {
            return Game(
                id = 1,
                server_id = 1,
                receiver_id = 2,
                server_score = 40,
                receiver_score = 40,
                advantage = Advantage.NONE ,
                state = GameState.IN_PROGRESS
            )
        }
    }

    val notStartedGameStubRepository: GamePort {
        override fun saveGame(){
            // NOT IMPLEMENTED FOR TESTING PURPOSES
        }
        override fun findByPlayers(serverEmail: String, receiverEmail: String): Game? {
            return Game(
                id = 1,
                server_id = 1,
                receiver_id = 2,
                server_score = 0,
                receiver_score = 0,
                advantage = Advantage.NONE ,
                state = GameState.NOT_STARTED
            )
        }
    }

    val adInGameStubRepository: GamePort {
        override fun saveGame(){
            // NOT IMPLEMENTED FOR TESTING PURPOSES
        }
        override fun findByPlayers(serverEmail: String, receiverEmail: String): Game? {
            return Game(
                id = 1,
                server_id = 1,
                receiver_id = 2,
                server_score = 40,
                receiver_score = 40,
                advantage = Advantage.AD_IN,
                state = GameState.IN_PROGRESS
            )
        }
    }

    val adOutGameStubRepository: GamePort {
        override fun saveGame(){
            // NOT IMPLEMENTED FOR TESTING PURPOSES
        }
        override fun findByPlayers(serverEmail: String, receiverEmail: String): Game? {
            return Game(
                id = 1,
                server_id = 1,
                receiver_id = 2,
                server_score = 40,
                receiver_score = 40,
                advantage = Advantage.AD_OUT,
                state = GameState.PLAYING
            )
        }
    }

    val playerStubRepository: PlayerPort{
        override fun savePlayer(){
            // NOT IMPLEMENTED FOR TESTING PURPOSES
        }
        override fun findByPlayerEmail(email: String): Player? {
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
        val dummyGameRequest = GameRequest(serverEmail="dummy40@dummy.com", receiverEmail="dummy40@dummy.com")
        //When
        val result: GameScoreResponse = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,deuceGameStubRepository)
        //Then
        result.formatScore shouldBe "Deuce"
    }

    @Test
    fun `when the score is 0-0 returns Love-all`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummy0@dummy.com", receiverEmail="dummy0@dummy.com")
        //When
        val result: GameScoreResponse = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,notStartedGameStubRepository)
        //Then
        result.formatScore shouldBe "Love-all"
    }

    @Test
    fun `when the score is 40-40 and advantage is with the server returns "Advantage <server name>"`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyReceiver@dummy.com")
        //When
        val result: GameScoreResponse = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,adInGameStubRepository,playerStubRepository)
        //Then
        result.formatScore shouldBe "Advantage Server Player"
    }

    @Test
    fun `when the score is 40-40 and advantage is with the receiver returns "Advantage <receiver name>"`() {
        //Given
        val tennisScoreFormatAdapter = TennisScoreFormatAdapter()
        val dummyGameRequest = GameRequest(serverEmail="dummyServer@dummy.com", receiverEmail="dummyReceiver@dummy.com")
        //When
        val result: GameScoreResponse = tennisScoreFormatAdapter.getFormattedScore(dummyGameRequest,adOutGameStubRepository,playerStubRepository)
        //Then
        result.formatScore shouldBe "Advantage Receiver Player"
    }
}