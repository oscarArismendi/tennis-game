package org.example.domain.models

class Game(
    var id: Long,
    var serverId: Long,
    var receiverId: Long,
    var serverScore: Int = 0,
    var receiverScore: Int = 0,
    var advantage: Advantage,
    var state: GameState
)