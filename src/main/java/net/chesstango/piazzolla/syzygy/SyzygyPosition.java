package net.chesstango.piazzolla.syzygy;


import lombok.Setter;
import net.chesstango.gardel.fen.FEN;

import static net.chesstango.piazzolla.syzygy.SyzygyConstants.Color.WHITE;

/**
 * @author Mauricio Coria
 */
@Setter
public class SyzygyPosition {
    long white;
    long black;
    long kings;
    long queens;
    long rooks;
    long bishops;
    long knights;
    long pawns;
    int rule50;
    int castling;
    int ep;
    boolean turn;

    static SyzygyPosition from(FEN fen) {
        SyzygyPositionBuilder syzygyPositionBuilder = new SyzygyPositionBuilder();
        fen.export(syzygyPositionBuilder);
        return syzygyPositionBuilder.getPositionRepresentation();
    }

    long pieces_by_type(SyzygyConstants.Color c, SyzygyConstants.PieceType p) {
        long mask = (c == WHITE) ? white : black;
        return switch (p) {
            case PAWN -> pawns & mask;
            case KNIGHT -> knights & mask;
            case BISHOP -> bishops & mask;
            case ROOK -> rooks & mask;
            case QUEEN -> queens & mask;
            case KING -> kings & mask;
        };
    }
}
