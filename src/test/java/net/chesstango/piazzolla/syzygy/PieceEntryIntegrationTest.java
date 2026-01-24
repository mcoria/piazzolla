package net.chesstango.piazzolla.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class PieceEntryIntegrationTest {
    public static final String PATH = "D:\\k8s_shared\\syzygy\\3-4-5-6";

    private SyzygyImp syzygy;
    private PieceEntry pieceEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new SyzygyImp(PATH);
        pieceEntry = new PieceEntry(syzygy);
    }

    /**
     * Test for the "KQvK" tableType: tableType without PAWNs
     */
    @Test
    public void test_init_tb_KQvK() {
        assertEquals("F06221548404795B6B33469E247B4560", md5sum("KQvK.rtbw"));
        assertEquals("AC866466E16EB19A4F8C796F8E1ABD2B", md5sum("KQvK.rtbz"));

        syzygy.init_tb("KQvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        /**
         * PieceEntry assertions
         */
        pieceEntry = syzygy.pieceEntry[0];

        assertEquals("KQvK", pieceEntry.tableName);
        assertEquals(0xa3ec1abc71e90863L, pieceEntry.key);
        assertEquals(3, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);

        assertFalse(pieceEntry.dtmLossOnly);

        /**
         * HashEntry assertions
         */
        SyzygyImp.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2622];
        assertEquals(0xa3ec1abc71e90863L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        tbHash = syzygy.tbHash[3438];
        assertEquals(0xd6e4e47d24962951L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pieceEntry.wdl);
        PieceAsymmetricWdl wdl = (PieceAsymmetricWdl) pieceEntry.wdl;

        EncInfo ei_wtm = wdl.ei_wtm;
        assertArrayEquals(new long[]{1, 0, 0, 0, 0, 0, 0}, ei_wtm.factor);
        assertArrayEquals(new byte[]{14, 6, 5, 0, 0, 0, 0}, ei_wtm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_wtm.norm);

        PairsData ei_wtm_precomp = ei_wtm.precomp;
        assertEquals(90, ei_wtm_precomp.indexTable.ptr);
        assertEquals(96, ei_wtm_precomp.sizeTable.ptr);
        assertEquals(128, ei_wtm_precomp.data.ptr);
        assertNull(ei_wtm_precomp.offset);  //POR QUE ES NULL ?????
        assertNull(ei_wtm_precomp.symPat);  //POR QUE ES NULL ?????

        assertEquals(0, ei_wtm_precomp.blockSize);
        assertEquals(0, ei_wtm_precomp.idxBits);
        assertEquals(0, ei_wtm_precomp.minLen);

        assertArrayEquals(new byte[]{4, 0}, ei_wtm.precomp.constValue);
        assertNull(ei_wtm_precomp.base);
        assertNull(ei_wtm_precomp.symLen);

        EncInfo ei_btm = wdl.ei_btm;
        assertArrayEquals(new long[]{1, 0, 0, 0, 0, 0, 0}, ei_btm.factor);
        assertArrayEquals(new byte[]{14, 5, 6, 0, 0, 0, 0}, ei_btm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_btm.norm);

        PairsData ei_btm_precomp = ei_btm.precomp;
        assertEquals(90, ei_btm_precomp.indexTable.ptr);
        assertEquals(96, ei_btm_precomp.sizeTable.ptr);
        assertEquals(128, ei_btm_precomp.data.ptr);
        assertEquals(20, ei_btm_precomp.offset.ptr);
        assertEquals(38, ei_btm_precomp.symPat.ptr);

        assertEquals(6, ei_btm_precomp.blockSize);
        assertEquals(15, ei_btm_precomp.idxBits);
        assertEquals(1, ei_btm_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_btm_precomp.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x4000000000000000L,
                0x1000000000000000L,
                0x400000000000000L,
                0x0L}, ei_btm_precomp.base);
        assertArrayEquals(new byte[]{3, 3, 4, 1, 31, 7, 127, 11, 7, 63, 6, 1, 47, 0, 0, -1, 15},
                ei_btm_precomp.symLen);

        /**
         * DTZ table assertions
         */
        assertNotNull(pieceEntry.dtz);
        PieceDtz dtz = (PieceDtz) pieceEntry.dtz;

        assertEquals(686, dtz.dtzMap.ptr);
        assertArrayEquals(new short[]{0, 0, 0, 0}, dtz.dtzMapIdx);
        assertEquals(0, dtz.dtzFlags);

        EncInfo ei_dtz = dtz.ei_dtz;
        assertArrayEquals(new long[]{1, 0, 0, 0, 0, 0, 0}, ei_dtz.factor);
        assertArrayEquals(new byte[]{6, 14, 5, 0, 0, 0, 0}, ei_dtz.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei_dtz.norm);

        PairsData ei_dtz_precomp = ei_dtz.precomp;
        assertEquals(686, ei_dtz_precomp.indexTable.ptr);
        assertEquals(692, ei_dtz_precomp.sizeTable.ptr);
        assertEquals(768, ei_dtz_precomp.data.ptr);
        assertEquals(8, ei_dtz_precomp.offset.ptr);
        assertEquals(34, ei_dtz_precomp.symPat.ptr);

        assertEquals(9, ei_dtz_precomp.blockSize);
        assertEquals(15, ei_dtz_precomp.idxBits);
        assertEquals(6, ei_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xB400000000000000L,
                0x7000000000000000L,
                0x2E00000000000000L,
                0x0200000000000000L,
                0x040000000000000L,
                0x0L}, ei_dtz_precomp.base);
        assertEquals(217, ei_dtz_precomp.symLen.length);
    }

    /**
     * Test for the "KQvKR" tableType: tableType without PAWNs
     */
    @Test
    public void test_init_tb_KQvKR() {
        assertEquals("044B30304AEC99AAD41CED55994AA2D6", md5sum("KQvKR.rtbw"));
        assertEquals("1A76E02BA0C13C440C33CB95124AD06A", md5sum("KQvKR.rtbz"));

        syzygy.init_tb("KQvKR");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        /**
         * PieceEntry assertions
         */
        pieceEntry = syzygy.pieceEntry[0];

        assertEquals("KQvKR", pieceEntry.tableName);
        assertEquals(0xA1648170ABA24CF8L, pieceEntry.key);
        assertEquals(4, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);

        assertFalse(pieceEntry.dtmLossOnly);

        /**
         * HashEntry assertions
         */
        SyzygyImp.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2582];
        assertEquals(0xA1648170ABA24CF8L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        tbHash = syzygy.tbHash[1780];
        assertEquals(0x6f42d01ce9295d4aL, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pieceEntry.wdl);
        PieceAsymmetricWdl wdl = (PieceAsymmetricWdl) pieceEntry.wdl;

        EncInfo ei_wtm = wdl.ei_wtm;
        assertArrayEquals(new long[]{61, 0, 0, 1, 0, 0, 0}, ei_wtm.factor);
        assertArrayEquals(new byte[]{6, 12, 5, 14, 0, 0, 0}, ei_wtm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 1, 0, 0, 0}, ei_wtm.norm);

        PairsData ei_wtm_precomp = ei_wtm.precomp;
        assertEquals(2234, ei_wtm_precomp.indexTable.ptr);
        assertEquals(2306, ei_wtm_precomp.sizeTable.ptr);
        assertEquals(2880, ei_wtm_precomp.data.ptr);
        assertEquals(18, ei_wtm_precomp.offset.ptr);
        assertEquals(46, ei_wtm_precomp.symPat.ptr);

        assertEquals(6, ei_wtm_precomp.blockSize);
        assertEquals(19, ei_wtm_precomp.idxBits);
        assertEquals(1, ei_wtm_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_wtm.precomp.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L, 0x8000000000000000L, 0x8000000000000000L, 0x8000000000000000L,
                0x8000000000000000L, 0x7C00000000000000L, 0x5A00000000000000L, 0x3100000000000000L,
                0x1000000000000000L, 0x80000000000000L, 0x20000000000000L, 0
        }, ei_wtm_precomp.base);
        assertEquals(193, ei_wtm_precomp.symLen.length);


        EncInfo ei_btm = wdl.ei_btm;
        assertArrayEquals(new long[]{61, 0, 0, 1, 0, 0, 0}, ei_btm.factor);
        assertArrayEquals(new byte[]{5, 14, 6, 12, 0, 0, 0}, ei_btm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 1, 0, 0, 0}, ei_btm.norm);

        PairsData ei_btm_precomp = ei_btm.precomp;
        assertEquals(2258, ei_btm_precomp.indexTable.ptr);
        assertEquals(2462, ei_btm_precomp.sizeTable.ptr);
        assertEquals(7872, ei_btm_precomp.data.ptr);
        assertEquals(632, ei_btm_precomp.offset.ptr);
        assertEquals(664, ei_btm_precomp.symPat.ptr);

        assertEquals(6, ei_btm_precomp.blockSize);
        assertEquals(18, ei_btm_precomp.idxBits);
        assertEquals(2, ei_btm_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_btm_precomp.constValue);
        assertArrayEquals(new long[]{0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xC000000000000000L, 0xC000000000000000L, 0xA200000000000000L, 0x7700000000000000L,
                0x4080000000000000L, 0x1240000000000000L, 0x160000000000000L, 0x70000000000000L,
                0x18000000000000L, 0}, ei_btm_precomp.base);
        assertEquals(523, ei_btm_precomp.symLen.length);

        /**
         * DTZ table assertions
         */
        assertNotNull(pieceEntry.dtz);
        PieceDtz dtz = (PieceDtz) pieceEntry.dtz;

        assertEquals(12330, dtz.dtzMap.ptr);
        assertArrayEquals(new short[]{(short) 1, (short) 33, (short) 36, (short) 37}, dtz.dtzMapIdx);
        assertEquals(2, dtz.dtzFlags);

        EncInfo ei_dtz = dtz.ei_dtz;
        assertArrayEquals(new long[]{61, 0, 0, 1, 0, 0, 0}, ei_dtz.factor);
        assertArrayEquals(new byte[]{14, 5, 12, 6, 0, 0, 0}, ei_dtz.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 1, 0, 0, 0}, ei_dtz.norm);

        PairsData ei_dtz_precomp = ei_dtz.precomp;
        assertEquals(12368, ei_dtz_precomp.indexTable.ptr);
        assertEquals(12458, ei_dtz_precomp.sizeTable.ptr);
        assertEquals(13056, ei_dtz_precomp.data.ptr);
        assertEquals(6, ei_dtz_precomp.offset.ptr);
        assertEquals(44, ei_dtz_precomp.symPat.ptr);

        assertEquals(10, ei_dtz_precomp.blockSize);
        assertEquals(17, ei_dtz_precomp.idxBits);
        assertEquals(7, ei_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_dtz_precomp.constValue);
        assertArrayEquals(new long[]{0xFE00000000000000L, 0xD600000000000000L, 0xA780000000000000L, 0x7F80000000000000L,
                0x5660000000000000L, 0x37A0000000000000L, 0x1D60000000000000L, 0x3FC000000000000L, 0x2000000000000L,
                0x2000000000000L, 0}, ei_dtz_precomp.base);
        assertEquals(4095, ei_dtz_precomp.symLen.length);
    }

    @Test
    public void test_init_table_KQvKQ() {
        assertEquals("71F3830D337C0FC73517DE3B8A0B7D70", md5sum("KQvKQ.rtbw"));
        assertEquals("0B33FADA9ADBF8FB8D64CFA0E9525942", md5sum("KQvKQ.rtbz"));

        pieceEntry.init_tb("KQvKQ");

        assertEquals("KQvKQ", pieceEntry.tableName);
        assertEquals(0x7AD0FF39967F31B4L, pieceEntry.key);
        assertEquals(4, pieceEntry.num);
        assertTrue(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);
        assertFalse(pieceEntry.dtmLossOnly);

        /**
         * WDL table assertions
         */
        assertNotNull(pieceEntry.wdl);
        PieceSymmetricWdl wdl = (PieceSymmetricWdl) pieceEntry.wdl;

        EncInfo ei_wtm = wdl.ei_wtm;
        assertArrayEquals(new long[]{61, 0, 0, 1, 0, 0, 0}, ei_wtm.factor);
        assertArrayEquals(new byte[]{13, 6, 14, 5, 0, 0, 0}, ei_wtm.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 1, 0, 0, 0}, ei_wtm.norm);

        PairsData ei_wtm_precomp = ei_wtm.precomp;
        assertEquals(1570, ei_wtm_precomp.indexTable.ptr);
        assertEquals(1618, ei_wtm_precomp.sizeTable.ptr);
        assertEquals(2112, ei_wtm_precomp.data.ptr);
        assertEquals(16, ei_wtm_precomp.offset.ptr);
        assertEquals(48, ei_wtm_precomp.symPat.ptr);

        assertEquals(6, ei_wtm_precomp.blockSize);
        assertEquals(18, ei_wtm_precomp.idxBits);
        assertEquals(2, ei_wtm_precomp.minLen);

        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xBC00000000000000L, 0x9600000000000000L, 0x6600000000000000L, 0x3B00000000000000L,
                0x1740000000000000L, 0x120000000000000L, 0x50000000000000L, 0x20000000000000L,
                0x0
        }, ei_wtm_precomp.base);
        assertArrayEquals(new byte[]{0, 0}, ei_wtm_precomp.constValue);
        assertEquals(507, ei_wtm_precomp.symLen.length);


        /**
         * DTZ table assertions
         */
        assertNotNull(pieceEntry.dtz);
        PieceDtz dtz = (PieceDtz) pieceEntry.dtz;

        assertEquals(544, dtz.dtzMap.ptr);
        assertArrayEquals(new short[]{(short) 1, (short) 12, (short) 22, (short) 23}, dtz.dtzMapIdx);
        assertEquals(2, dtz.dtzFlags);

        EncInfo ei_dtz = dtz.ei_dtz;
        assertArrayEquals(new long[]{61, 0, 0, 1, 0, 0, 0}, ei_dtz.factor);
        assertArrayEquals(new byte[]{14, 6, 13, 5, 0, 0, 0}, ei_dtz.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 1, 0, 0, 0}, ei_dtz.norm);

        PairsData ei_dtz_precomp = ei_dtz.precomp;
        assertEquals(568, ei_dtz_precomp.indexTable.ptr);
        assertEquals(592, ei_dtz_precomp.sizeTable.ptr);
        assertEquals(832, ei_dtz_precomp.data.ptr);
        assertEquals(18, ei_dtz_precomp.offset.ptr);
        assertEquals(46, ei_dtz_precomp.symPat.ptr);

        assertEquals(6, ei_dtz_precomp.blockSize);
        assertEquals(19, ei_dtz_precomp.idxBits);
        assertEquals(1, ei_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L, 0x8000000000000000L, 0x8000000000000000L, 0x8000000000000000L,
                0x8000000000000000L, 0x7000000000000000L, 0x5000000000000000L, 0x2300000000000000L,
                0xD00000000000000L, 0x80000000000000L, 0x40000000000000L, 0x0
        }, ei_dtz_precomp.base);
        assertEquals(166, ei_dtz_precomp.symLen.length);
    }


    public String md5sum(String filename) {
        try {
            Path tbPath = Path.of(PATH).resolve(filename);
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (FileInputStream fis = new FileInputStream(tbPath.toFile())) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
