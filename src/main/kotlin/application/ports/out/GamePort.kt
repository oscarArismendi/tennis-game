package org.example.application.ports.out

import org.example.domain.models.Game

interface GamePort {
    fun findGameByPlayersIds(player1Id: Long, player2Id:Long): Game?
    fun saveGame(game: Game): Game?
}