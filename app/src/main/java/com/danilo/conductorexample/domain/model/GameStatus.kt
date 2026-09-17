package com.danilo.conductorexample.domain.model

/**
 * Represents the current gameplay and backlog status of a [Game].
 */
enum class GameStatus {
    /** The game is in the user's backlog, intended to be played in the future. */
    WANT_TO_PLAY,

    /** The game is actively being played by the user. */
    PLAYING,

    /** The user has finished/beaten the game. */
    COMPLETED,

    /** The user ceased playing the game before completing it. */
    ABANDONED
}
