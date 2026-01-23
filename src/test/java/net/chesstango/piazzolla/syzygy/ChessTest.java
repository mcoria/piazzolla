package net.chesstango.piazzolla.syzygy;

import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.Test;

import static net.chesstango.piazzolla.syzygy.Chess.gen_captures;
import static net.chesstango.piazzolla.syzygy.SyzygyConstants.TB_MAX_CAPTURES;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class ChessTest {

    @Test
    void testEnPassantCapture() {
        FEN fen = FEN.of("8/5K2/5p2/5Pp1/8/8/3k4/8 w - g6 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        short[] moves = new short[TB_MAX_CAPTURES];

        int moveCount = gen_captures(syzygyPosition, moves);

        assertEquals(2, moveCount);
        assertEquals(3437, moves[0]);
        assertEquals(2414, moves[1]);
    }
}
