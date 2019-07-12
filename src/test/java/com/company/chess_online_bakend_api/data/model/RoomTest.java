package com.company.chess_online_bakend_api.data.model;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoomTest {

    private final Long ID = 1L;
    private final String ROOM_NAME = "Room 1";

    Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder().id(ID).name(ROOM_NAME).build();
    }

    @Test
    void ConstructorTest() {

        assertEquals(ROOM_NAME, room.getName());
        assertEquals(GameStatus.WAITNG_TO_START, room.getGame().getStatus());
        assertEquals(Integer.valueOf(0), room.getGame().getTurn());
        assertEquals(room, room);
        assertNull(room.getGame().getWhitePlayer());
        assertNull(room.getGame().getBlackPlayer());
    }

    @Test
    void newGameTest() {

        room.startNewGame();

        Game game = room.getGame();

        assertEquals(GameStatus.WAITNG_TO_START, game.getStatus());
        assertEquals(Integer.valueOf(0), game.getTurn());
        assertEquals(room, game.getRoom());
        assertNull(game.getWhitePlayer());
        assertNull(game.getBlackPlayer());
    }

    @Test
    void newGameWithPlayersTest() {
        User player1 = User.builder().id(2L).build();
        User player2 = User.builder().id(3L).build();

        Game game = room.startNewGame(player1, player2);

        assertEquals(GameStatus.STARTED, game.getStatus());
        assertEquals(Integer.valueOf(1), game.getTurn());
        assertEquals(room, game.getRoom());
        assertEquals(player1, game.getWhitePlayer());
        assertEquals(player2, game.getBlackPlayer());
    }

    @Test
    void newGameWhitePlayerNull() {
        Room room = Room.builder().build();

        Assertions.assertThrows(NullPointerException.class, () -> {
            room.startNewGame(null, User.builder().build());
        });
    }

    @Test
    void newGameBlackPlayerNull() {
        Room room = Room.builder().build();

        Assertions.assertThrows(NullPointerException.class, () -> {
            room.startNewGame(User.builder().build(), null);
        });
    }
}