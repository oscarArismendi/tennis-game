package application.support

sealed class ScoreLookUpError(message: String): IllegalArgumentException(message) {
    class PlayerNotFound( email: String): ScoreLookUpError("Player with email $email not found")
    class GameNotFound( serverEmail: String, receiverEmail: String): ScoreLookUpError("Game not found for players $serverEmail and $receiverEmail")
}