package net.chesstango.piazzolla.syzygy;


import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class SyzygyPositionTest {

    @Test
    public void testToPosition01() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        assertEquals(0x0000808000000000L, syzygyPosition.white);
        assertEquals(0x8000000000000000L, syzygyPosition.black);
        assertEquals(0x8000800000000000L, syzygyPosition.kings);
        assertEquals(0x0000008000000000L, syzygyPosition.queens);
        assertEquals(0x0000000000000000L, syzygyPosition.rooks);
        assertEquals(0x0000000000000000L, syzygyPosition.bishops);
        assertEquals(0x0000000000000000L, syzygyPosition.knights);
        assertEquals(0x0000000000000000L, syzygyPosition.pawns);
        assertEquals(0x00L, syzygyPosition.rule50);
        assertEquals(0x00L, syzygyPosition.ep);
        assertTrue(syzygyPosition.turn);
    }

    @Test
    public void testToPosition02() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 5 10");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        assertEquals(0x0000808000000000L, syzygyPosition.white);
        assertEquals(0x8000000000000000L, syzygyPosition.black);
        assertEquals(0x8000800000000000L, syzygyPosition.kings);
        assertEquals(0x0000008000000000L, syzygyPosition.queens);
        assertEquals(0x0000000000000000L, syzygyPosition.rooks);
        assertEquals(0x0000000000000000L, syzygyPosition.bishops);
        assertEquals(0x0000000000000000L, syzygyPosition.knights);
        assertEquals(0x0000000000000000L, syzygyPosition.pawns);
        assertEquals(0x05L, syzygyPosition.rule50);
        assertEquals(0x00L, syzygyPosition.ep);
        assertTrue(syzygyPosition.turn);
    }

}
