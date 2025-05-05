package net.chesstango.syzygy;


import net.chesstango.gardel.fen.FEN;

import static net.chesstango.syzygy.SyzygyConstants.Color.WHITE;

/**
 * @author Mauricio Coria
 */
public class BitPosition {
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

    static BitPosition from(FEN fen){
        BitPositionBuilder bitPositionBuilder = new BitPositionBuilder();
        fen.export(bitPositionBuilder);
        return bitPositionBuilder.getPositionRepresentation();
    }
}
