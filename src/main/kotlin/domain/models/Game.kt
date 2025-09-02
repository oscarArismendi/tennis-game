package org.example.domain.models

class Game(
    var id: Long,
    val serverId: Long,
    val receiverId: Long,
    var serverScore: Int = 0,
    var receiverScore: Int = 0,
    val advantage: Advantage,
    val state: GameState
)