package domain.models

enum class GameState(val message: String) {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    ENDED("Ended")
}