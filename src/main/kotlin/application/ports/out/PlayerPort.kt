package application.ports.out

import domain.models.Player

interface PlayerPort {
    fun findPlayerByEmail(email: String): Player?
    fun savePlayer(player: Player): Player?
}