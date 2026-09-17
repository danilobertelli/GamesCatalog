package com.danilo.conductorexample.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class GameTest {

    @Test
    fun `create valid game successfully`() {
        val game = Game(
            id = "game-123",
            title = "The Legend of Zelda",
            overview = "An epic action-adventure game.",
            coverImageUrl = "https://example.com/cover.jpg",
            platforms = listOf("Nintendo Switch"),
            status = GameStatus.PLAYING,
            rating = 5,
            completionDateEpochMs = null
        )

        assertEquals("game-123", game.id)
        assertEquals("The Legend of Zelda", game.title)
        assertEquals(GameStatus.PLAYING, game.status)
        assertEquals(5, game.rating)
        assertNull(game.completionDateEpochMs)
    }

    @Test
    fun `blank title throws IllegalArgumentException`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Game(
                id = "1",
                title = "   ",
                overview = "Overview",
                coverImageUrl = null,
                platforms = emptyList(),
                status = GameStatus.WANT_TO_PLAY,
                rating = null,
                completionDateEpochMs = null
            )
        }
        assertEquals("Game title cannot be blank", exception.message)
    }

    @Test
    fun `rating below 1 throws IllegalArgumentException`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Game(
                id = "1",
                title = "Hades",
                overview = "Rogue-like dungeon crawler",
                coverImageUrl = null,
                platforms = listOf("PC"),
                status = GameStatus.COMPLETED,
                rating = 0,
                completionDateEpochMs = null
            )
        }
        assertEquals("Rating must be between 1 and 5", exception.message)
    }

    @Test
    fun `rating above 5 throws IllegalArgumentException`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Game(
                id = "1",
                title = "Hades",
                overview = "Rogue-like dungeon crawler",
                coverImageUrl = null,
                platforms = listOf("PC"),
                status = GameStatus.COMPLETED,
                rating = 6,
                completionDateEpochMs = null
            )
        }
        assertEquals("Rating must be between 1 and 5", exception.message)
    }
}
