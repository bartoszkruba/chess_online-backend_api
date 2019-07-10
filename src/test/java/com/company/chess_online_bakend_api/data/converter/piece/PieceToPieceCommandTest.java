package com.company.chess_online_bakend_api.data.converter.piece;

import com.company.chess_online_bakend_api.data.command.PieceCommand;
import com.company.chess_online_bakend_api.data.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PieceToPieceCommandTest {

    @InjectMocks
    PieceToPieceCommand pieceToPieceCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertNull() {

        PieceCommand pieceCommand = pieceToPieceCommand.convert(null);

        assertNull(pieceCommand);
    }

    @Test
    void testConvertEmptyObject() {

        PieceCommand pieceCommand = pieceToPieceCommand.convert(Piece.builder().build());

        assertNull(pieceCommand.getId());
        assertNull(pieceCommand.getPieceColor());
        assertNull(pieceCommand.getPieceType());
        assertNull(pieceCommand.getPosition());
    }

    @Test
    void testConvert() {
        Piece piece = Piece.builder()
                .id(1L)
                .pieceColor(PieceColor.WHITE)
                .pieceType(PieceType.KING)
                .horizontalPosition(HorizontalPosition.A)
                .verticalPosition(VerticalPosition.ONE).build();

        PieceCommand pieceCommand = pieceToPieceCommand.convert(piece);

        assertEquals(Long.valueOf(1), pieceCommand.getId());
        assertEquals(PieceColor.WHITE, pieceCommand.getPieceColor());
        assertEquals(PieceType.KING, pieceCommand.getPieceType());
        assertEquals("A1", pieceCommand.getPosition());
    }

}