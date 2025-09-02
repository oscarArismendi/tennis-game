package org.example.application.ports.out

import org.example.domain.models.Player

interface PlayerPort {
    fun findPlayerByEmail(email: String): Player?
    fun savePlayer(player: Player): Player?
}