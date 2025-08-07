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
public class SyzygyImpTest {

    public static final Path PATH = Path.of("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

    private SyzygyImp syzygy;

    @BeforeEach
    public void setUp() throws Exception {
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
    public void test_tb_probe_root_KQvK_white() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(15, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(5, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(0, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvK_black() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, Syzygy.TB_GET_WDL(res));
        assertEquals(2, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(0, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(1, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKR_white() {
        FEN fen = FEN.of("7k/r7/7K/7Q/8/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(12, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(3, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(5, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKR_black() {
        FEN fen = FEN.of("7k/r7/7K/7Q/8/8/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_DRAW, Syzygy.TB_GET_WDL(res));
        assertEquals(0, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(1, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(14, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKQ_white() {
        FEN fen = FEN.of("7k/q7/7K/7Q/8/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(3, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(10, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(7, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQvKQ_black() {
        FEN fen = FEN.of("7k/q7/7K/7Q/8/8/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(1, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(6, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(15, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQNvKQ_white() {
        FEN fen = FEN.of("7k/q7/7K/7Q/4N3/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(3, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(11, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(14, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KQNvKQ_black() {
        FEN fen = FEN.of("7k/q7/7K/7Q/4N3/8/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(1, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(4, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(17, count(results, Syzygy.TB_LOSS));
    }


    @Test
    public void test_tb_probe_root_KPvK_white() {
        FEN fen = FEN.of("8/P7/4K3/7k/8/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(10, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(2, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(0, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KPvK_black() {
        FEN fen = FEN.of("8/P7/4K3/7k/8/8/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, Syzygy.TB_GET_WDL(res));
        assertEquals(2, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(0, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(5, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KPvKP_white() {
        FEN fen = FEN.of("8/P7/4K3/7k/6p1/8/8/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(1, Syzygy.TB_GET_DTZ(res));

        assertEquals(10, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(1, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(1, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_KPPvKP_black() {
        FEN fen = FEN.of("8/8/1k2P2K/6P1/8/3p4/8/8 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_BLESSED_LOSS, Syzygy.TB_GET_WDL(res));
        assertEquals(104, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(0, count(results, Syzygy.TB_DRAW));
        assertEquals(2, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(7, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_longest_3() {
        FEN fen = FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, Syzygy.TB_GET_WDL(res));
        assertEquals(32, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(0, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(4, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_longest_4() {
        FEN fen = FEN.of("8/8/8/6B1/8/8/4k3/1K5N b - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_LOSS, Syzygy.TB_GET_WDL(res));
        assertEquals(65, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(0, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(0, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(5, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_tb_probe_root_longest_5() {
        FEN fen = FEN.of("K7/N7/k7/8/3p4/8/N7/8 w - - 0 1");

        SyzygyPosition syzygyPosition = SyzygyPosition.from(fen);

        int[] results = new int[Syzygy.TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(syzygyPosition, results);

        assertNotEquals(Syzygy.TB_RESULT_FAILED, res);

        assertEquals(Syzygy.TB_CURSED_WIN, Syzygy.TB_GET_WDL(res));
        assertEquals(164, Syzygy.TB_GET_DTZ(res));

        assertEquals(0, count(results, Syzygy.TB_WIN));
        assertEquals(1, count(results, Syzygy.TB_CURSED_WIN));
        assertEquals(6, count(results, Syzygy.TB_DRAW));
        assertEquals(0, count(results, Syzygy.TB_BLESSED_LOSS));
        assertEquals(0, count(results, Syzygy.TB_LOSS));
    }

    @Test
    public void test_maxpieces() {
        assertEquals(5, syzygy.tb_largest());
    }

    static int count(int[] results, int wdl) {
        int count = 0;
        for (int i = 0; i < results.length && results[i] != Syzygy.TB_RESULT_FAILED; i++) {
            if (Syzygy.TB_GET_WDL(results[i]) == wdl) {
                count++;
            }
        }
        return count;
    }

}

