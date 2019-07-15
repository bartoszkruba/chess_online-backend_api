package com.company.chess_online_bakend_api.service.jpa;

import com.company.chess_online_bakend_api.data.command.GameCommand;
import com.company.chess_online_bakend_api.data.command.RoomCommand;
import com.company.chess_online_bakend_api.data.command.UserCommand;
import com.company.chess_online_bakend_api.data.converter.game.GameCommandToGame;
import com.company.chess_online_bakend_api.data.converter.game.GameToGameCommand;
import com.company.chess_online_bakend_api.data.model.Game;
import com.company.chess_online_bakend_api.data.model.Room;
import com.company.chess_online_bakend_api.data.model.User;
import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import com.company.chess_online_bakend_api.data.repository.GameRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


// TODO: 2019-07-15 Write tests for GameNotFoundException
class GameServiceJpaImplTest {

    private User USER_1 = User.builder().id(1L).build();
    private User USER_2 = User.builder().id(2L).build();

    private Room ROOM = Room.builder().id(1L).build();

    private Game GAME_1 = Game.builder()
            .id(1L)
            .status(GameStatus.STARTED)
            .turn(2)
            .room(ROOM)
            .blackPlayer(USER_1)
            .whitePlayer(USER_2).build();

    private Game GAME_2 = Game.builder()
            .id(2L)
            .status(GameStatus.STARTED)
            .turn(2)
            .room(ROOM)
            .blackPlayer(USER_1)
            .whitePlayer(USER_2).build();

    private UserCommand USERCOMMAND1 = UserCommand.builder().id(1L).build();
    private UserCommand USERCOMMAND2 = UserCommand.builder().id(2L).build();

    private RoomCommand ROOMCOMMAND = RoomCommand.builder().id(1L).build();

    private GameCommand GAMECOMMAND1 = GameCommand.builder()
            .id(1L)
            .status(GameStatus.STARTED)
            .turn(2)
            .roomId(ROOMCOMMAND.getId())
            .blackPlayer(USERCOMMAND1)
            .whitePlayer(USERCOMMAND2).build();

    private GameCommand GAMECOMMAND2 = GameCommand.builder()
            .id(1L)
            .status(GameStatus.STARTED)
            .turn(2)
            .roomId(ROOMCOMMAND.getId())
            .blackPlayer(USERCOMMAND1)
            .whitePlayer(USERCOMMAND2).build();

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameToGameCommand gameToGameCommand;

    @Mock
    private GameCommandToGame gameCommandToGame;

    @InjectMocks
    private GameServiceJpaImpl gameService;

    @BeforeEach
    void setUp() {
        when(gameToGameCommand.convert(GAME_1)).thenReturn(GAMECOMMAND1);
        when(gameToGameCommand.convert(GAME_2)).thenReturn(GAMECOMMAND2);

        when(gameCommandToGame.convert(GAMECOMMAND1)).thenReturn(GAME_1);
        when(gameCommandToGame.convert(GAMECOMMAND2)).thenReturn(GAME_2);
    }

    @Test
    void getByRoomId() {
        when(gameRepository.findGameByRoom(ROOM)).thenReturn(Optional.of(GAME_1));

        GameCommand gameCommand = gameService.getByRoomId(ROOM.getId());

        assertEquals(GAMECOMMAND1, gameCommand);
        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findGameByRoom(ROOM);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void findById() {
        when(gameRepository.findById(GAME_1.getId())).thenReturn(Optional.of(GAME_1));

        GameCommand gameCommand = gameService.findById(GAME_1.getId());

        assertEquals(GAMECOMMAND1, gameCommand);
        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findById(GAME_1.getId());
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void save() {
        when(gameRepository.save(GAME_1)).thenReturn(GAME_1);

        GameCommand gameCommand = gameService.save(GAMECOMMAND1);

        assertEquals(GAMECOMMAND1, gameCommand);
        verify(gameCommandToGame, times(1)).convert(GAMECOMMAND1);
        verifyNoMoreInteractions(gameCommandToGame);
        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verifyNoMoreInteractions(gameToGameCommand);

        verify(gameRepository, times(1)).save(GAME_1);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void findAll() {
        when(gameRepository.findAll()).thenReturn(Set.of(GAME_1, GAME_2));

        Set<GameCommand> gameCommands = gameService.findAll();

        assertTrue(gameCommands.contains(GAMECOMMAND1));
        assertTrue(gameCommands.contains(GAMECOMMAND2));
        assertEquals(2, gameCommands.size());

        verify(gameToGameCommand, times(1)).convert(GAME_1);
        verify(gameToGameCommand, times(1)).convert(GAME_2);
        verifyNoMoreInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).findAll();
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void delete() {
        gameService.delete(GAMECOMMAND1);

        verify(gameCommandToGame, times(1)).convert(GAMECOMMAND1);
        verifyNoMoreInteractions(gameCommandToGame);
        verifyZeroInteractions(gameToGameCommand);

        verify(gameRepository, times(1)).delete(GAME_1);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void deleteById() {
        gameService.deleteById(GAME_1.getId());

        verifyZeroInteractions(gameToGameCommand);
        verifyZeroInteractions(gameCommandToGame);

        verify(gameRepository, times(1)).deleteById(GAME_1.getId());
        verifyNoMoreInteractions(gameRepository);
    }
}