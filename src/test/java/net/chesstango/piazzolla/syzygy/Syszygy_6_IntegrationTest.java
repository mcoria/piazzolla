package net.chesstango.piazzolla.syzygy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class Syszygy_6_IntegrationTest {
    static final String PATH = "D:\\k8s_shared\\syzygy\\3-4-5-6";

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

        assertEquals(6, syzygy.TB_LARGEST);
        assertEquals(6, syzygy.TB_MaxCardinality);
        assertEquals(0, syzygy.TB_MaxCardinalityDTM);
        assertEquals(254, syzygy.tbNumPiece);
        assertEquals(256, syzygy.tbNumPawn);
        assertEquals(510, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(510, syzygy.numDtz);
    }

    @Test
    public void testMaxPieces() {
        assertEquals(6, syzygy.tb_largest());
    }
}
