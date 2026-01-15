package net.chesstango.piazzolla.syzygy;

import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class Syszygy_3_4_5_IntegrationTest {
    static final Path PATH = Path.of("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

    SyzygyImp syzygy;

    @BeforeEach
    public void setUp() {
        syzygy = new SyzygyImp(PATH);
        syzygy.tb_init();
    }

    @AfterEach
    public void tearDown() {
        //syzygy.close();
    }

    @Test
    public void test_tb_init() {
        assertEquals(650, syzygy.pieceEntry.length);
        assertEquals(861, syzygy.pawnEntry.length);
        assertEquals(4096, syzygy.tbHash.length);

        assertEquals(5, syzygy.TB_LARGEST);
        assertEquals(5, syzygy.TB_MaxCardinality);
        assertEquals(0, syzygy.TB_MaxCardinalityDTM);
        assertEquals(84, syzygy.tbNumPiece);
        assertEquals(61, syzygy.tbNumPawn);
        assertEquals(145, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(145, syzygy.numDtz);
    }

    @Test
    public void testMaxPieces() {
        assertEquals(5, syzygy.tb_largest());
    }


    /**
     * Probe fails, position with 6 pieces
     */
    @Test
    public void test_tb_probe_root_6_fails() {
        FEN fen = FEN.of("6N1/5KR1/2n5/8/8/8/2n5/1k6 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertEquals(Syzygy.TB_RESULT_FAILED, res);
    }

}
