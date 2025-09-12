package config

import java.sql.Connection
import java.sql.DriverManager
// config
const val URL = "jdbc:mysql://localhost:3306/tennis?useSSL=false"
const val USER = "Your User Name!"
const val PASSWORD = "Your Password"

class DatabaseConfig {
    // database
    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
