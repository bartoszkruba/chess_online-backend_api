package com.company.chess_online_bakend_api.data.model;

import com.company.chess_online_bakend_api.data.model.enums.GameStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = "room")
@ToString(exclude = "room", callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Game extends BaseEntity {

    @OneToOne
    private Room room;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Board board;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Move> moves;

    @ManyToOne
    private User whitePlayer;

    @ManyToOne
    private User blackPlayer;

    private Integer turn;

    private String fenNotation;

    @Builder
    public Game(Long id, LocalDateTime created, LocalDateTime updated, Room room, GameStatus status, User whitePlayer,
                User blackPlayer, Integer turn, Board board, String fenNotation, List<Move> moves) {
        super(id, created, updated);
        this.room = room;
        this.status = status;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.turn = turn;
        this.board = board;
        this.fenNotation = fenNotation;
        this.moves = moves;
    }

    // TODO: 2019-07-20 write tests
    public void addMove(Move move) {

        if (this.moves == null) {
            moves = new ArrayList<>();
        }

        moves.add(move);
    }

    // TODO: 2019-07-20 write tests
    public void increaseTurnCount() {
        if (turn == null) {
            turn = 1;
        }
        turn++;
    }
}
