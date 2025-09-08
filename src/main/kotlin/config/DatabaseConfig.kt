package config

import java.sql.Connection
import java.sql.DriverManager

class DatabaseConfig {
    // config
    val URL = "jdbc:mysql://localhost:3306/tennis?useSSL=false"
    val USER = "Your username"
    val PASSWORD = "Your password"

    // database
    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }

}