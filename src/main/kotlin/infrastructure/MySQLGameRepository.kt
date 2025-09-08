package infrastructure

import application.ports.out.GamePort
import domain.models.Advantage
import domain.models.Game
import domain.models.GameState
import config.DatabaseConfig
import java.sql.PreparedStatement
import kotlin.use

class MySQLGameRepository: GamePort {
    override fun findGameByPlayersIds(player1Id: Long, player2Id: Long): Game? {
        val sql = "SELECT id,server_id, receiver_id, server_score, receiver_score, advantage, state FROM game WHERE (server_id = ? AND receiver_id = ?) OR (server_id = ? AND receiver_id = ?)"
        try {

            val dbConfig = DatabaseConfig()
            dbConfig.getConnection().use { connection ->
                connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setLong(1, player1Id)
                    statement.setLong(2, player2Id)
                    statement.setLong(3, player2Id)
                    statement.setLong(4, player1Id)
                    statement.executeQuery()

                    val resultSet = statement.executeQuery()

                    if (resultSet.next()) {
                        // Retrieve values from result set
                        val id = resultSet.getLong("id")
                        val serverId = resultSet.getLong("server_id")
                        val receiverId = resultSet.getLong("receiver_id")
                        val serverScore = resultSet.getInt("server_score")
                        val receiverScore = resultSet.getInt("receiver_score")
                        val advantageStr = resultSet.getString("advantage")
                        val stateStr = resultSet.getString("state")

                        return Game(id,
                            serverId,
                            receiverId,
                            serverScore,
                            receiverScore,
                            Advantage.valueOf(advantageStr),
                            GameState.valueOf(stateStr)
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("Error at creating a game: " + e.message)
        }
        return null
    }

    override fun saveGame(game: Game): Game? {
        val sql = "INSERT INTO game (server_id, receiver_id, server_score, receiver_score, advantage, state) VALUES (?,?,?,?,?,?)"
        try {

            val dbConfig = DatabaseConfig()
            dbConfig.getConnection().use { connection ->
                connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setLong(1, game.serverId)
                    statement.setLong(2, game.receiverId)
                    statement.setInt(3, game.serverScore)
                    statement.setInt(4, game.receiverScore)
                    statement.setString(5, game.advantage.name)
                    statement.setString(6, game.state.name)

                    statement.executeUpdate()

                    statement.generatedKeys.use { keys ->
                        if (keys.next()) {
                            game.id = keys.getLong(1)
                        }
                        println("Game created successfully!")
                        return game
                    }
                }
            }
        } catch (e: Exception) {
            println("Error at creating a game: " + e.message)
        }
        return null
    }

    override fun updateGameById(updateColumns: String, gameId: Long): Boolean {
        val sql = "UPDATE game SET $updateColumns WHERE id = ?"
        try {

            val dbConfig = DatabaseConfig()
            dbConfig.getConnection().use { connection ->
                connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setLong(1, gameId)

                    val rowsUpdated = statement.executeUpdate()

                    if (rowsUpdated > 0){
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            println("Error at updating a game: " + e.message)
        }
        return false
    }
}