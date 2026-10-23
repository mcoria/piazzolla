package net.chesstango.piazzolla.syzygy;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.gardel.fen.FEN;

import static net.chesstango.piazzolla.syzygy.SyzygyConstants.Color.WHITE;

/**
 * Representation of a chess position formatted for Syzygy tablebase probing.
 * <p>
 * Contains bitboards for piece placement (separated by color and piece type),
 * the halfmove clock for the 50-move rule, castling rights flag, en passant square index,
 * and active side to move.
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
@EqualsAndHashCode
public class SyzygyPosition {

    /**
     * Bitboard of all white pieces on the board.
     */
    long white;

    /**
     * Bitboard of all black pieces on the board.
     */
    long black;

    /**
     * Bitboard of all kings on the board.
     */
    long kings;

    /**
     * Bitboard of all queens on the board.
     */
    long queens;

    /**
     * Bitboard of all rooks on the board.
     */
    long rooks;

    /**
     * Bitboard of all bishops on the board.
     */
    long bishops;

    /**
     * Bitboard of all knights on the board.
     */
    long knights;

    /**
     * Bitboard of all pawns on the board.
     */
    long pawns;

    /**
     * Halfmove clock (number of halfmoves since the last pawn move or capture) for the 50-move rule.
     */
    byte rule50;

    /**
     * Castling availability indicator (non-zero if any castling right exists, 0 otherwise).
     */
    byte castling;

    /**
     * En passant target square index (0 to 63, or 0 if no en passant capture is possible).
     */
    byte ep;

    /**
     * Side to move: {@code true} for White, {@code false} for Black.
     */
    boolean turn;

    /**
     * Constructs a new, empty {@code SyzygyPosition}.
     */
    public SyzygyPosition() {
    }

    /**
     * Creates a {@link SyzygyPosition} from a given {@link FEN} instance.
     *
     * @param fen the chess position in Forsyth-Edwards Notation
     * @return the constructed {@link SyzygyPosition} instance
     */
    public static SyzygyPosition from(FEN fen) {
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
