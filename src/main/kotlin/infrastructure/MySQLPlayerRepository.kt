package infrastructure

import application.ports.out.PlayerPort
import config.DatabaseConfig
import domain.models.Player
import java.sql.PreparedStatement
import java.sql.SQLException

class MySQLPlayerRepository : PlayerPort {
    override fun findPlayerByEmail(email: String): Player? {
        val sql = "CALL GetRowByColumnValue(?,?,?,?)"
        try {
            val dbConfig = DatabaseConfig()
            dbConfig.getConnection().use { connection ->
                connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setString(1, "id,firstname,lastname,email") // SelectColumns
                    statement.setString(2, "player") // TableName
                    statement.setString(3, "email") // ColumnName
                    statement.setString(4, "'$email'") // SearchValue

                    val resultSet = statement.executeQuery()

                    if (resultSet.next()) {
                        // Retrieve values from result set
                        val id = resultSet.getLong("id")
                        val firstName = resultSet.getString("firstname")
                        val lastName = resultSet.getString("lastname")
                        val email = resultSet.getString("email")

                        return Player(id, firstName, lastName, email)
                    }
                }
            }
        } catch (e: Exception) {
            println("Error at find a player with the email $email: " + e.message)
        }
        return null
    }

    override fun savePlayer(player: Player): Player? {
        val sql = "INSERT INTO player (firstName,lastName,email) VALUES (?, ?, ?)"
        try {
            val dbConfig = DatabaseConfig()
            dbConfig.getConnection().use { connection ->
                connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setString(1, player.firstname)
                    statement.setString(2, player.lastname)
                    statement.setString(3, player.email)
                    statement.executeUpdate()

                    statement.generatedKeys.use { keys ->
                        if (keys.next()) {
                            player.id = keys.getLong(1)
                        }
                        println("Player created successfully!")
                        return player
                    }
                }
            }
        } catch (e: SQLException) {
            println("Error at creating a player: " + e.message)
        }
        return null
    }
}
