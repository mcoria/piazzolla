package net.chesstango.piazzolla.syzygy;


import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Mauricio Coria
 */
public class SyzygyImpProbeWDLIntegrationTest {

    //public static final Path PATH = Path.of("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
    public static final Path PATH = Path.of("E:\\syzygy");

    private SyzygyImp syzygy;

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

    @Test
    public void test_tb_probe_wdl_whiteTurn_win() {
        FEN fen = FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int res = syzygy.tb_probe_wdl(syzygyPosition);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, res);
    }

    @Test
    public void test_tb_probe_wdl_whiteTurn_loss() {
        FEN fen = FEN.of("8/8/8/8/8/3k4/2r5/1K6 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int res = syzygy.tb_probe_wdl(syzygyPosition);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, res);
    }

    @Test
    public void test_tb_probe_wdl_blackTurn_win() {
        FEN fen = FEN.of("8/8/8/8/8/3k4/2r5/1K6 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int res = syzygy.tb_probe_wdl(syzygyPosition);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, res);
    }

    @Test
    public void test_tb_probe_wdl_blackTurn_loss() {
        FEN fen = FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int res = syzygy.tb_probe_wdl(syzygyPosition);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, res);
    }

}

