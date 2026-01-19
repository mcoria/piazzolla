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
public class PawnEntryIntegrationTest {
    public static final Path PATH = Path.of("E:\\syzygy");

    private SyzygyImp syzygy;
    private PawnEntry pawnEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new SyzygyImp(PATH);
        pawnEntry = new PawnEntry(syzygy);
    }

    /**
     * Test for the "KPvKP" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KPvKP() {
        assertEquals("E9390BE76079250CABA06F353F758536", md5sum("KPvKP.rtbw"));
        assertEquals("17CEC6D51197C92B97B3AD9BC5559EE0", md5sum("KPvKP.rtbz"));

        syzygy.init_tb("KPvKP");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        pawnEntry = syzygy.pawnEntry[0];
        assertEquals(0x8E59ED7027C162EAL, pawnEntry.key);
        assertEquals(4, pawnEntry.num);
        assertTrue(pawnEntry.symmetric);
        assertEquals(1, pawnEntry.pawns[0]);
        assertEquals(1, pawnEntry.pawns[1]);


        /**
         * HashEntry assertions
         */
        SyzygyImp.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2277];
        assertEquals(0x8E59ED7027C162EAL, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pawnEntry.wdl);
        PawnSymmetricWdl wdl = (PawnSymmetricWdl) pawnEntry.wdl;

        EncInfo ei0 = wdl.ei_wtm[0];
        assertArrayEquals(new long[]{177754, 3782, 1, 62, 0, 0, 0}, ei0.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei0.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei0.norm);

        PairsData ei0_precomp = ei0.precomp;
        assertEquals(18828, ei0_precomp.indexTable.ptr);
        assertEquals(19620, ei0_precomp.sizeTable.ptr);
        assertEquals(26496, ei0_precomp.data.ptr);
        assertEquals(28, ei0_precomp.offset.ptr);
        assertEquals(64, ei0_precomp.symPat.ptr);

        assertEquals(6, ei0_precomp.blockSize);
        assertEquals(15, ei0_precomp.idxBits);
        assertEquals(6, ei0_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0.precomp.constValue);
        assertArrayEquals(new long[]{
                0xFC00000000000000L,
                0xEC00000000000000L,
                0xB800000000000000L,
                0x8700000000000000L,
                0x5A80000000000000L,
                0x32A0000000000000L,
                0x380000000000000L,
                0xD0000000000000L,
                0x28000000000000L,
                0x4000000000000L, 0
        }, ei0_precomp.base);
        assertEquals(1581, ei0_precomp.symLen.length);


        EncInfo ei1 = wdl.ei_wtm[1];
        assertArrayEquals(new long[]{177754, 62, 1, 2914, 0, 0, 0}, ei1.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei1.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei1.norm);

        PairsData ei1_precomp = ei1.precomp;
        assertEquals(19026, ei1_precomp.indexTable.ptr);
        assertEquals(21262, ei1_precomp.sizeTable.ptr);
        assertEquals(79040, ei1_precomp.data.ptr);
        assertEquals(4806, ei1_precomp.offset.ptr);
        assertEquals(4842, ei1_precomp.symPat.ptr);

        assertEquals(6, ei1_precomp.blockSize);
        assertEquals(15, ei1_precomp.idxBits);
        assertEquals(6, ei1_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp.constValue);
        assertArrayEquals(new long[]{
                0xFC00000000000000L,
                0xF000000000000000L,
                0xB200000000000000L,
                0x8880000000000000L,
                0x5A40000000000000L,
                0x31A0000000000000L,
                0x370000000000000L,
                0xF8000000000000L,
                0x40000000000000L,
                0x10000000000000L,
                0}, ei1_precomp.base);
        assertEquals(1587, ei1_precomp.symLen.length);


        EncInfo ei2 = wdl.ei_wtm[2];
        assertArrayEquals(new long[]{2914, 62, 1, 17484, 0, 0, 0}, ei2.factor);
        assertArrayEquals(new byte[]{1, 9, 14, 6, 0, 0, 0}, ei2.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei2.norm);

        PairsData ei2_precomp = ei2.precomp;
        assertEquals(19224, ei2_precomp.indexTable.ptr);
        assertEquals(23008, ei2_precomp.sizeTable.ptr);
        assertEquals(134912, ei2_precomp.data.ptr);
        assertEquals(9600, ei2_precomp.offset.ptr);
        assertEquals(9636, ei2_precomp.symPat.ptr);

        assertEquals(6, ei2_precomp.blockSize);
        assertEquals(15, ei2_precomp.idxBits);
        assertEquals(7, ei2_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L,
                0xB400000000000000L,
                0x7F80000000000000L,
                0x5500000000000000L,
                0x2D00000000000000L,
                0x860000000000000L,
                0x98000000000000L,
                0x24000000000000L,
                0x8000000000000L,
                0}, ei2_precomp.base);
        assertEquals(1558, ei2_precomp.symLen.length);


        EncInfo ei3 = wdl.ei_wtm[3];
        assertArrayEquals(new long[]{177754, 3782, 1, 62, 0, 0, 0}, ei3.factor);
        assertArrayEquals(new byte[]{1, 9, 14, 6, 0, 0, 0}, ei3.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei3.norm);

        PairsData ei3_precomp = ei3.precomp;
        assertEquals(19422, ei3_precomp.indexTable.ptr);
        assertEquals(24802, ei3_precomp.sizeTable.ptr);
        assertEquals(192320, ei3_precomp.data.ptr);
        assertEquals(14306, ei3_precomp.offset.ptr);
        assertEquals(14342, ei3_precomp.symPat.ptr);

        assertEquals(6, ei3_precomp.blockSize);
        assertEquals(15, ei3_precomp.idxBits);
        assertEquals(7, ei3_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp.constValue);
        assertArrayEquals(new long[]{
                0xE400000000000000L,
                0xAD00000000000000L,
                0x7F80000000000000L,
                0x5940000000000000L,
                0x2EC0000000000000L,
                0x300000000000000L,
                0xA0000000000000L,
                0x24000000000000L,
                0x4000000000000L,
                0}, ei3_precomp.base);
        assertEquals(1495, ei3_precomp.symLen.length);


        /**
         * DTZ table assertions
         */
        assertNotNull(pawnEntry.dtz);
        PawnDtz dtz = (PawnDtz) pawnEntry.dtz;

        assertEquals(5102, dtz.dtzMap.ptr);
        assertArrayEquals(new short[][]{{1, 8, 16, 17}, {18, 30, 41, 42}, {43, 54, 64, 65}, {66, 76, 85, 86}}, dtz.dtzMapIdx);
        assertArrayEquals(new byte[]{2, 2, 2, 2}, dtz.dtzFlags);

        EncInfo ei0_dtz = dtz.ei_dtz[0];
        assertArrayEquals(new long[]{62, 22692, 1, 372, 0, 0, 0}, ei0_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei0_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei0_dtz.norm);

        PairsData ei0_dtz_precomp = ei0_dtz.precomp;
        assertEquals(5188, ei0_dtz_precomp.indexTable.ptr);
        assertEquals(5308, ei0_dtz_precomp.sizeTable.ptr);
        assertEquals(6080, ei0_dtz_precomp.data.ptr);
        assertEquals(36, ei0_dtz_precomp.offset.ptr);
        assertEquals(68, ei0_dtz_precomp.symPat.ptr);

        assertEquals(7, ei0_dtz_precomp.blockSize);
        assertEquals(18, ei0_dtz_precomp.idxBits);
        assertEquals(2, ei0_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xB400000000000000L, 0x7C00000000000000L, 0x4900000000000000L, 0x2800000000000000L,
                0x280000000000000L, 0x60000000000000L, 0x10000000000000L, 0x8000000000000L,
                0}, ei0_dtz_precomp.base);
        assertEquals(328, ei0_dtz_precomp.symLen.length);


        EncInfo ei1_dtz = dtz.ei_dtz[1];
        assertArrayEquals(new long[]{177754, 3782, 1, 62, 0, 0, 0}, ei1_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei1_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei1_dtz.norm);

        PairsData ei1_dtz_precomp = ei1_dtz.precomp;
        assertEquals(5218, ei1_dtz_precomp.indexTable.ptr);
        assertEquals(5456, ei1_dtz_precomp.sizeTable.ptr);
        assertEquals(15296, ei1_dtz_precomp.data.ptr);
        assertEquals(1058, ei1_dtz_precomp.offset.ptr);
        assertEquals(1090, ei1_dtz_precomp.symPat.ptr);

        assertEquals(7, ei1_dtz_precomp.blockSize);
        assertEquals(18, ei1_dtz_precomp.idxBits);
        assertEquals(2, ei1_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xC000000000000000L, 0x9000000000000000L, 0x6000000000000000L, 0x3900000000000000L,
                0x15C0000000000000L, 0x180000000000000L, 0x60000000000000L, 0x18000000000000L,
                0}, ei1_dtz_precomp.base);
        assertEquals(500, ei1_dtz_precomp.symLen.length);


        EncInfo ei2_dtz = dtz.ei_dtz[2];
        assertArrayEquals(new long[]{177754, 62, 1, 2914, 0, 0, 0}, ei2_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei2_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei2_dtz.norm);

        PairsData ei2_dtz_precomp = ei2_dtz.precomp;
        assertEquals(5248, ei2_dtz_precomp.indexTable.ptr);
        assertEquals(5678, ei2_dtz_precomp.sizeTable.ptr);
        assertEquals(29248, ei2_dtz_precomp.data.ptr);
        assertEquals(2596, ei2_dtz_precomp.offset.ptr);
        assertEquals(2628, ei2_dtz_precomp.symPat.ptr);

        assertEquals(7, ei2_dtz_precomp.blockSize);
        assertEquals(18, ei2_dtz_precomp.idxBits);
        assertEquals(2, ei2_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xBC00000000000000L, 0x8C00000000000000L, 0x5300000000000000L, 0x3380000000000000L,
                0x1000000000000000L, 0xE0000000000000L, 0x50000000000000L, 0x10000000000000L,
                0}, ei2_dtz_precomp.base);
        assertEquals(432, ei2_dtz_precomp.symLen.length);


        EncInfo ei3_dtz = dtz.ei_dtz[3];
        assertArrayEquals(new long[]{177754, 62, 1, 2914, 0, 0, 0}, ei3_dtz.factor);
        assertArrayEquals(new byte[]{1, 9, 6, 14, 0, 0, 0}, ei3_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 0, 0, 0}, ei3_dtz.norm);

        PairsData ei3_dtz_precomp = ei3_dtz.precomp;
        assertEquals(5278, ei3_dtz_precomp.indexTable.ptr);
        assertEquals(5892, ei3_dtz_precomp.sizeTable.ptr);
        assertEquals(42688, ei3_dtz_precomp.data.ptr);
        assertEquals(3930, ei3_dtz_precomp.offset.ptr);
        assertEquals(3962, ei3_dtz_precomp.symPat.ptr);

        assertEquals(7, ei3_dtz_precomp.blockSize);
        assertEquals(18, ei3_dtz_precomp.idxBits);
        assertEquals(2, ei3_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L, 0xC000000000000000L,
                0xBC00000000000000L, 0x7C00000000000000L, 0x4B00000000000000L, 0x2D80000000000000L,
                0xA40000000000000L, 0xC0000000000000L, 0x20000000000000L, 0x8000000000000L,
                0}, ei3_dtz_precomp.base);
        assertEquals(380, ei3_dtz_precomp.symLen.length);

    }

    /**
     * Test for the "KPvK" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KPvK() {
        assertEquals("46F6EF491BD26696D7B20281E7C5B721", md5sum("KPvK.rtbw"));
        assertEquals("54460894C15F087CFD16670BF1513755", md5sum("KPvK.rtbz"));

        syzygy.init_tb("KPvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        pawnEntry = syzygy.pawnEntry[0];
        assertEquals(0XEC0ADE190C0F6003L, pawnEntry.key);
        assertEquals(3, pawnEntry.num);
        assertFalse(pawnEntry.symmetric);
        assertEquals(1, pawnEntry.pawns[0]);
        assertEquals(0, pawnEntry.pawns[1]);

        SyzygyImp.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[3776];
        assertEquals(0XEC0ADE190C0F6003L, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        tbHash = syzygy.tbHash[2596];
        assertEquals(0XA24F0F571BB202E7L, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pawnEntry.wdl);
        PawnAsymmetricWdl wdl = (PawnAsymmetricWdl) pawnEntry.wdl;


        /**
         * WHITE WDL table assertions
         */
        EncInfo ei0_wtm = wdl.ei_wtm[0];
        assertArrayEquals(new long[]{63, 1, 378, 0, 0, 0, 0}, ei0_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_wtm.norm);

        PairsData ei0_precomp_wtm = ei0_wtm.precomp;
        assertEquals(1662, ei0_precomp_wtm.indexTable.ptr);
        assertEquals(1710, ei0_precomp_wtm.sizeTable.ptr);
        assertEquals(1920, ei0_precomp_wtm.data.ptr);
        assertEquals(24, ei0_precomp_wtm.offset.ptr);
        assertEquals(42, ei0_precomp_wtm.symPat.ptr);

        assertEquals(6, ei0_precomp_wtm.blockSize);
        assertEquals(15, ei0_precomp_wtm.idxBits);
        assertEquals(4, ei0_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_wtm.precomp.constValue);
        assertArrayEquals(new long[]{
                0xE000000000000000L, 0x7800000000000000L,
                0x1000000000000000L, 0
        }, ei0_precomp_wtm.base);
        assertEquals(49, ei0_precomp_wtm.symLen.length);


        EncInfo ei1_wtm = wdl.ei_wtm[1];
        assertArrayEquals(new long[]{63, 1, 378, 0, 0, 0, 0}, ei1_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_wtm.norm);

        PairsData ei1_precomp_wtm = ei1_wtm.precomp;
        assertEquals(1674, ei1_precomp_wtm.indexTable.ptr);
        assertEquals(1740, ei1_precomp_wtm.sizeTable.ptr);
        assertEquals(2880, ei1_precomp_wtm.data.ptr);
        assertEquals(372, ei1_precomp_wtm.offset.ptr);
        assertEquals(392, ei1_precomp_wtm.symPat.ptr);

        assertEquals(6, ei1_precomp_wtm.blockSize);
        assertEquals(15, ei1_precomp_wtm.idxBits);
        assertEquals(4, ei1_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0xA000000000000000L,
                0x2800000000000000L, 0x0200000000000000L,
                0}, ei1_precomp_wtm.base);
        assertEquals(62, ei1_precomp_wtm.symLen.length);


        EncInfo ei2_wtm = wdl.ei_wtm[2];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei2_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_wtm.norm);

        PairsData ei2_precomp_wtm = ei2_wtm.precomp;
        assertEquals(1686, ei2_precomp_wtm.indexTable.ptr);
        assertEquals(1792, ei2_precomp_wtm.sizeTable.ptr);
        assertEquals(4544, ei2_precomp_wtm.data.ptr);
        assertEquals(796, ei2_precomp_wtm.offset.ptr);
        assertEquals(818, ei2_precomp_wtm.symPat.ptr);

        assertEquals(6, ei2_precomp_wtm.blockSize);
        assertEquals(15, ei2_precomp_wtm.idxBits);
        assertEquals(5, ei2_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x2400000000000000L,
                0x400000000000000L,
                0x100000000000000L,
                0}, ei2_precomp_wtm.base);
        assertEquals(60, ei2_precomp_wtm.symLen.length);

        EncInfo ei3_wtm = wdl.ei_wtm[3];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei3_wtm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_wtm.norm);

        PairsData ei3_precomp_wtm = ei3_wtm.precomp;
        assertEquals(1698, ei3_precomp_wtm.indexTable.ptr);
        assertEquals(1844, ei3_precomp_wtm.sizeTable.ptr);
        assertEquals(6208, ei3_precomp_wtm.data.ptr);
        assertEquals(1228, ei3_precomp_wtm.offset.ptr);
        assertEquals(1248, ei3_precomp_wtm.symPat.ptr);

        assertEquals(6, ei3_precomp_wtm.blockSize);
        assertEquals(15, ei3_precomp_wtm.idxBits);
        assertEquals(4, ei3_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L,
                0x7000000000000000L,
                0x2800000000000000L,
                0x600000000000000L,
                0}, ei3_precomp_wtm.base);
        assertEquals(58, ei3_precomp_wtm.symLen.length);


        /**
         * BLACK WDL table assertions
         */
        EncInfo ei0_btm = wdl.ei_btm[0];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei0_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_btm.norm);

        PairsData ei0_precomp_wbtm = ei0_btm.precomp;
        assertEquals(1668, ei0_precomp_wbtm.indexTable.ptr);
        assertEquals(1724, ei0_precomp_wbtm.sizeTable.ptr);
        assertEquals(2368, ei0_precomp_wbtm.data.ptr);
        assertEquals(192, ei0_precomp_wbtm.offset.ptr);
        assertEquals(214, ei0_precomp_wbtm.symPat.ptr);

        assertEquals(6, ei0_precomp_wbtm.blockSize);
        assertEquals(15, ei0_precomp_wbtm.idxBits);
        assertEquals(4, ei0_precomp_wbtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_precomp_wbtm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0x7800000000000000L,
                0x800000000000000L, 0x200000000000000L,
                0x100000000000000L,
                0}, ei0_precomp_wbtm.base);
        assertEquals(52, ei0_precomp_wbtm.symLen.length);

        EncInfo ei1_btm = wdl.ei_btm[1];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei1_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_btm.norm);

        PairsData ei1_precomp_btm = ei1_btm.precomp;
        assertEquals(1680, ei1_precomp_btm.indexTable.ptr);
        assertEquals(1764, ei1_precomp_btm.sizeTable.ptr);
        assertEquals(3648, ei1_precomp_btm.data.ptr);
        assertEquals(578, ei1_precomp_btm.offset.ptr);
        assertEquals(600, ei1_precomp_btm.symPat.ptr);

        assertEquals(6, ei1_precomp_btm.blockSize);
        assertEquals(15, ei1_precomp_btm.idxBits);
        assertEquals(5, ei1_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0x8800000000000000L, 0x3800000000000000L,
                0x200000000000000L, 0x100000000000000L,
                0}, ei1_precomp_btm.base);
        assertEquals(65, ei1_precomp_btm.symLen.length);


        EncInfo ei2_btm = wdl.ei_btm[2];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei2_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_btm.norm);

        PairsData ei2_precomp_btm = ei2_btm.precomp;
        assertEquals(1692, ei2_precomp_btm.indexTable.ptr);
        assertEquals(1816, ei2_precomp_btm.sizeTable.ptr);
        assertEquals(5312, ei2_precomp_btm.data.ptr);
        assertEquals(998, ei2_precomp_btm.offset.ptr);
        assertEquals(1018, ei2_precomp_btm.symPat.ptr);

        assertEquals(6, ei2_precomp_btm.blockSize);
        assertEquals(15, ei2_precomp_btm.idxBits);
        assertEquals(5, ei2_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0xB000000000000000L,
                0x3400000000000000L,
                0x400000000000000L,
                0}, ei2_precomp_btm.base);
        assertEquals(69, ei2_precomp_btm.symLen.length);

        EncInfo ei3_btm = wdl.ei_btm[3];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei3_btm.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_btm.norm);

        PairsData ei3_precomp_btm = ei3_btm.precomp;
        assertEquals(1704, ei3_precomp_btm.indexTable.ptr);
        assertEquals(1866, ei3_precomp_btm.sizeTable.ptr);
        assertEquals(6912, ei3_precomp_btm.data.ptr);
        assertEquals(1424, ei3_precomp_btm.offset.ptr);
        assertEquals(1446, ei3_precomp_btm.symPat.ptr);

        assertEquals(6, ei3_precomp_btm.blockSize);
        assertEquals(15, ei3_precomp_btm.idxBits);
        assertEquals(4, ei3_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L, 0x9800000000000000L,
                0x4800000000000000L, 0x600000000000000L,
                0x100000000000000L,
                0}, ei3_precomp_btm.base);
        assertEquals(72, ei3_precomp_btm.symLen.length);

        /**
         * DTZ table assertions
         */
        assertNotNull(pawnEntry.dtz);
        PawnDtz dtz = (PawnDtz) pawnEntry.dtz;

        assertEquals(794, dtz.dtzMap.ptr);
        assertArrayEquals(new short[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}, dtz.dtzMapIdx);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, dtz.dtzFlags);

        EncInfo ei0_dtz = dtz.ei_dtz[0];
        assertArrayEquals(new long[]{1, 6, 378, 0, 0, 0, 0}, ei0_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei0_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei0_dtz.norm);

        PairsData ei0_dtz_precomp = ei0_dtz.precomp;
        assertEquals(794, ei0_dtz_precomp.indexTable.ptr);
        assertEquals(818, ei0_dtz_precomp.sizeTable.ptr);
        assertEquals(832, ei0_dtz_precomp.data.ptr);
        assertEquals(28, ei0_dtz_precomp.offset.ptr);
        assertEquals(46, ei0_dtz_precomp.symPat.ptr);

        assertEquals(8, ei0_dtz_precomp.blockSize);
        assertEquals(15, ei0_dtz_precomp.idxBits);
        assertEquals(2, ei0_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L,
                0xC000000000000000L,
                0x9000000000000000L,
                0x1000000000000000L,
                0x400000000000000L,
                0}, ei0_dtz_precomp.base);
        assertEquals(25, ei0_dtz_precomp.symLen.length);


        EncInfo ei1_dtz = dtz.ei_dtz[1];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei1_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei1_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei1_dtz.norm);

        PairsData ei1_dtz_precomp = ei1_dtz.precomp;
        assertEquals(800, ei1_dtz_precomp.indexTable.ptr);
        assertEquals(820, ei1_dtz_precomp.sizeTable.ptr);
        assertEquals(1088, ei1_dtz_precomp.data.ptr);
        assertEquals(124, ei1_dtz_precomp.offset.ptr);
        assertEquals(146, ei1_dtz_precomp.symPat.ptr);

        assertEquals(10, ei1_dtz_precomp.blockSize);
        assertEquals(15, ei1_dtz_precomp.idxBits);
        assertEquals(4, ei1_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xE000000000000000L, 0x7800000000000000L,
                0x2C00000000000000L, 0x400000000000000L,
                0x100000000000000L, 0}, ei1_dtz_precomp.base);
        assertEquals(59, ei1_dtz_precomp.symLen.length);


        EncInfo ei2_dtz = dtz.ei_dtz[2];
        assertArrayEquals(new long[]{3906, 1, 63, 0, 0, 0, 0}, ei2_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei2_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei2_dtz.norm);

        PairsData ei2_dtz_precomp = ei2_dtz.precomp;
        assertEquals(806, ei2_dtz_precomp.indexTable.ptr);
        assertEquals(822, ei2_dtz_precomp.sizeTable.ptr);
        assertEquals(2112, ei2_dtz_precomp.data.ptr);
        assertEquals(324, ei2_dtz_precomp.offset.ptr);
        assertEquals(346, ei2_dtz_precomp.symPat.ptr);

        assertEquals(10, ei2_dtz_precomp.blockSize);
        assertEquals(15, ei2_dtz_precomp.idxBits);
        assertEquals(5, ei2_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xA800000000000000L, 0x2800000000000000L,
                0x400000000000000L, 0x200000000000000L,
                0}, ei2_dtz_precomp.base);
        assertEquals(67, ei2_dtz_precomp.symLen.length);


        EncInfo ei3_dtz = dtz.ei_dtz[3];
        assertArrayEquals(new long[]{63, 1, 378, 0, 0, 0, 0}, ei3_dtz.factor);
        assertArrayEquals(new byte[]{1, 6, 14, 0, 0, 0, 0}, ei3_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 0, 0, 0, 0}, ei3_dtz.norm);

        PairsData ei3_dtz_precomp = ei3_dtz.precomp;
        assertEquals(812, ei3_dtz_precomp.indexTable.ptr);
        assertEquals(824, ei3_dtz_precomp.sizeTable.ptr);
        assertEquals(3136, ei3_dtz_precomp.data.ptr);
        assertEquals(550, ei3_dtz_precomp.offset.ptr);
        assertEquals(572, ei3_dtz_precomp.symPat.ptr);

        assertEquals(10, ei3_dtz_precomp.blockSize);
        assertEquals(15, ei3_dtz_precomp.idxBits);
        assertEquals(4, ei3_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xF000000000000000L,
                0xA800000000000000L,
                0x4400000000000000L,
                0x800000000000000L,
                0x100000000000000L,
                0}, ei3_dtz_precomp.base);
        assertEquals(74, ei3_dtz_precomp.symLen.length);
    }

    /**
     * Test for the "KPvK" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KRNPvKN() {
        assertEquals("8B292AA5CE7EA607BEBDB4DEBF6F9D77", md5sum("KRNPvKN.rtbw"));
        assertEquals("43249A8ABFCD32C5BA0A98AB7C7A8277", md5sum("KRNPvKN.rtbz"));

        syzygy.init_tb("KRNPvKN");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        pawnEntry = syzygy.pawnEntry[0];
        assertEquals(0x124FD08BCBF8415CL, pawnEntry.key);
        assertEquals(6, pawnEntry.num);
        assertFalse(pawnEntry.symmetric);
        assertEquals(1, pawnEntry.pawns[0]);
        assertEquals(0, pawnEntry.pawns[1]);


        SyzygyImp.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[292];
        assertEquals(0x124FD08BCBF8415CL, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        tbHash = syzygy.tbHash[730];
        assertEquals(0x2DAE7CDE50C0F4DCL, tbHash.key);
        assertSame(tbHash.ptr, pawnEntry);

        /**
         * WDL table assertions
         */
        assertNotNull(pawnEntry.wdl);
        PawnAsymmetricWdl wdl = (PawnAsymmetricWdl) pawnEntry.wdl;

        /**
         * WHITE WDL table assertions
         */
        EncInfo ei0_wtm = wdl.ei_wtm[0];
        assertArrayEquals(new long[]{3906, 1, 63, 23436, 1429596, 85775760, 0}, ei0_wtm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 10, 4, 6, 0}, ei0_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei0_wtm.norm);

        PairsData ei0_precomp_wtm = ei0_wtm.precomp;

        assertEquals(98730, ei0_precomp_wtm.indexTable.ptr);
        assertEquals(677922, ei0_precomp_wtm.sizeTable.ptr);
        assertEquals(4757568, ei0_precomp_wtm.data.ptr);
        assertEquals(42, ei0_precomp_wtm.offset.ptr);
        assertEquals(86, ei0_precomp_wtm.symPat.ptr);

        assertEquals(5, ei0_precomp_wtm.blockSize);
        assertEquals(20, ei0_precomp_wtm.idxBits);
        assertEquals(1, ei0_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_wtm.precomp.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7000000000000000L,
                0x5500000000000000L,
                0x3C60000000000000L,
                0x2BD0000000000000L,
                0x1BE0000000000000L,
                0xEB8000000000000L,
                0x1B6000000000000L,
                0x18000000000000L,
                0x4800000000000L,
                0x800000000000L,
                0x200000000000L,
                0L
        }, ei0_precomp_wtm.base);
        assertEquals(4095, ei0_precomp_wtm.symLen.length);


        EncInfo ei1_wtm = wdl.ei_wtm[1];
        assertArrayEquals(new long[]{238266, 1, 63, 3906, 1429596, 85775760, 0}, ei1_wtm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 10, 6, 4, 0}, ei1_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei1_wtm.norm);

        PairsData ei1_precomp_wtm = ei1_wtm.precomp;
        assertEquals(243528, ei1_precomp_wtm.indexTable.ptr);
        assertEquals(1632496, ei1_precomp_wtm.sizeTable.ptr);
        assertEquals(20030656, ei1_precomp_wtm.data.ptr);
        assertEquals(24714, ei1_precomp_wtm.offset.ptr);
        assertEquals(24758, ei1_precomp_wtm.symPat.ptr);

        assertEquals(5, ei1_precomp_wtm.blockSize);
        assertEquals(20, ei1_precomp_wtm.idxBits);
        assertEquals(1, ei1_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7F00000000000000L,
                0x6B80000000000000L,
                0x5C80000000000000L,
                0x46C0000000000000L,
                0x2BE0000000000000L,
                0x1CE0000000000000L,
                0xE3C000000000000L,
                0xDE000000000000L,
                0x13000000000000L,
                0x5800000000000L,
                0x1800000000000L,
                0x400000000000L,
                0}, ei1_precomp_wtm.base);
        assertEquals(4095, ei1_precomp_wtm.symLen.length);


        EncInfo ei2_wtm = wdl.ei_wtm[2];
        assertArrayEquals(new long[]{843461640, 1, 63, 3906, 238266, 14295960, 0}, ei2_wtm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 4, 10, 6, 0}, ei2_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei2_wtm.norm);

        PairsData ei2_precomp_wtm = ei2_wtm.precomp;
        assertEquals(388326, ei2_precomp_wtm.indexTable.ptr);
        assertEquals(2667182, ei2_precomp_wtm.sizeTable.ptr);
        assertEquals(36585536, ei2_precomp_wtm.data.ptr);
        assertEquals(49386, ei2_precomp_wtm.offset.ptr);
        assertEquals(49432, ei2_precomp_wtm.symPat.ptr);

        assertEquals(5, ei2_precomp_wtm.blockSize);
        assertEquals(20, ei2_precomp_wtm.idxBits);
        assertEquals(1, ei2_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7F00000000000000L,
                0x6D80000000000000L,
                0x5B00000000000000L,
                0x44A0000000000000L,
                0x2EC0000000000000L,
                0x1D60000000000000L,
                0xE18000000000000L,
                0xA6000000000000L,
                0x13000000000000L,
                0x4000000000000L,
                0xC00000000000L,
                0x400000000000L,
                0x100000000000L,
                0}, ei2_precomp_wtm.base);
        assertEquals(4095, ei2_precomp_wtm.symLen.length);

        EncInfo ei3_wtm = wdl.ei_wtm[3];
        assertArrayEquals(new long[]{3906, 1, 63, 23436, 1429596, 85775760, 0}, ei3_wtm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 10, 4, 6, 0}, ei3_wtm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei3_wtm.norm);

        PairsData ei3_precomp_wtm = ei3_wtm.precomp;
        assertEquals(533124, ei3_precomp_wtm.indexTable.ptr);
        assertEquals(3721486, ei3_precomp_wtm.sizeTable.ptr);
        assertEquals(53454336, ei3_precomp_wtm.data.ptr);
        assertEquals(74062, ei3_precomp_wtm.offset.ptr);
        assertEquals(74106, ei3_precomp_wtm.symPat.ptr);

        assertEquals(5, ei3_precomp_wtm.blockSize);
        assertEquals(20, ei3_precomp_wtm.idxBits);
        assertEquals(1, ei3_precomp_wtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_wtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7F00000000000000L,
                0x6F00000000000000L,
                0x5A40000000000000L,
                0x4600000000000000L,
                0x3090000000000000L,
                0x1DD0000000000000L,
                0xD4C000000000000L,
                0xAE000000000000L,
                0x12000000000000L,
                0x2000000000000L,
                0x800000000000L,
                0x200000000000L,
                0}, ei3_precomp_wtm.base);
        assertEquals(4095, ei3_precomp_wtm.symLen.length);


        /**
         * BLACK WDL table assertions
         */
        EncInfo ei0_btm = wdl.ei_btm[0];
        assertArrayEquals(new long[]{238266, 1, 63, 3906, 1429596, 85775760, 0}, ei0_btm.factor);
        assertArrayEquals(new byte[]{1, 2, 4, 6, 10, 14, 0}, ei0_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei0_btm.norm);

        PairsData ei0_precomp_wbtm = ei0_btm.precomp;
        assertEquals(127692, ei0_precomp_wbtm.indexTable.ptr);
        assertEquals(949046, ei0_precomp_wbtm.sizeTable.ptr);
        assertEquals(9095488, ei0_precomp_wbtm.data.ptr);
        assertEquals(12380, ei0_precomp_wbtm.offset.ptr);
        assertEquals(12420, ei0_precomp_wbtm.symPat.ptr);

        assertEquals(5, ei0_precomp_wbtm.blockSize);
        assertEquals(18, ei0_precomp_wbtm.idxBits);
        assertEquals(1, ei0_precomp_wbtm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_precomp_wbtm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x6D80000000000000L,
                0x5640000000000000L,
                0x4000000000000000L,
                0x28B0000000000000L,
                0x19F0000000000000L,
                0xD20000000000000L,
                0x366000000000000L,
                0x5000000000000L,
                0x800000000000L,
                0}, ei0_precomp_wbtm.base);
        assertEquals(4095, ei0_precomp_wbtm.symLen.length);

        EncInfo ei1_btm = wdl.ei_btm[1];
        assertArrayEquals(new long[]{238266, 1, 63, 3906, 1429596, 85775760, 0}, ei1_btm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 10, 4, 6, 0}, ei1_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei1_btm.norm);

        PairsData ei1_precomp_btm = ei1_btm.precomp;
        assertEquals(272490, ei1_precomp_btm.indexTable.ptr);
        assertEquals(1922558, ei1_precomp_btm.sizeTable.ptr);
        assertEquals(24671552, ei1_precomp_btm.data.ptr);
        assertEquals(37052, ei1_precomp_btm.offset.ptr);
        assertEquals(37092, ei1_precomp_btm.symPat.ptr);

        assertEquals(5, ei1_precomp_btm.blockSize);
        assertEquals(18, ei1_precomp_btm.idxBits);
        assertEquals(1, ei1_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7180000000000000L,
                0x5900000000000000L,
                0x3EE0000000000000L,
                0x2CC0000000000000L,
                0x1C40000000000000L,
                0xDD4000000000000L,
                0x1DE000000000000L,
                0x7000000000000L,
                0x800000000000L,
                0}, ei1_precomp_btm.base);
        assertEquals(4095, ei1_precomp_btm.symLen.length);


        EncInfo ei2_btm = wdl.ei_btm[2];
        assertArrayEquals(new long[]{3906, 1, 63, 23436, 1429596, 85775760, 0}, ei2_btm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 4, 6, 10, 0}, ei2_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei2_btm.norm);

        PairsData ei2_precomp_btm = ei2_btm.precomp;
        assertEquals(417288, ei2_precomp_btm.indexTable.ptr);
        assertEquals(2960094, ei2_precomp_btm.sizeTable.ptr);
        assertEquals(41272064, ei2_precomp_btm.data.ptr);
        assertEquals(61726, ei2_precomp_btm.offset.ptr);
        assertEquals(61768, ei2_precomp_btm.symPat.ptr);

        assertEquals(5, ei2_precomp_btm.blockSize);
        assertEquals(18, ei2_precomp_btm.idxBits);
        assertEquals(1, ei2_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x6F00000000000000L,
                0x5880000000000000L,
                0x3F40000000000000L,
                0x2C40000000000000L,
                0x1BC0000000000000L,
                0xE20000000000000L,
                0x1E8000000000000L,
                0x9000000000000L,
                0x1000000000000L,
                0x400000000000L,
                0}, ei2_precomp_btm.base);
        assertEquals(4095, ei2_precomp_btm.symLen.length);

        EncInfo ei3_btm = wdl.ei_btm[3];
        assertArrayEquals(new long[]{843461640, 1, 63, 3906, 238266, 14295960, 0}, ei3_btm.factor);
        assertArrayEquals(new byte[]{1, 2, 14, 6, 10, 4, 0}, ei3_btm.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei3_btm.norm);

        PairsData ei3_precomp_btm = ei3_btm.precomp;
        assertEquals(562086, ei3_precomp_btm.indexTable.ptr);
        assertEquals(4010622, ei3_precomp_btm.sizeTable.ptr);
        assertEquals(58080448, ei3_precomp_btm.data.ptr);
        assertEquals(86400, ei3_precomp_btm.offset.ptr);
        assertEquals(86444, ei3_precomp_btm.symPat.ptr);

        assertEquals(5, ei3_precomp_btm.blockSize);
        assertEquals(18, ei3_precomp_btm.idxBits);
        assertEquals(1, ei3_precomp_btm.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_precomp_btm.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x6D00000000000000L,
                0x57C0000000000000L,
                0x3EC0000000000000L,
                0x2BE0000000000000L,
                0x1C50000000000000L,
                0xDEC000000000000L,
                0x1E8000000000000L,
                0x12000000000000L,
                0x2000000000000L,
                0x400000000000L,
                0x200000000000L,
                0}, ei3_precomp_btm.base);
        assertEquals(4095, ei3_precomp_btm.symLen.length);

        /**
         * DTZ table assertions
         */
        assertNotNull(pawnEntry.dtz);
        PawnDtz dtz = (PawnDtz) pawnEntry.dtz;

        assertEquals(49374, dtz.dtzMap.ptr);
        assertArrayEquals(new short[][]{{1,12,14,15}, {16,25,29,30}, {31,41,43,44}, {45,54,56,58}}, dtz.dtzMapIdx);
        assertArrayEquals(new byte[]{2,2,2,2}, dtz.dtzFlags);

        EncInfo ei0_dtz = dtz.ei_dtz[0];
        assertArrayEquals(new long[]{14295960, 1, 63, 3906, 238266, 85775760, 0}, ei0_dtz.factor);
        assertArrayEquals(new byte[]{1, 2, 4, 14, 6, 10, 0}, ei0_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei0_dtz.norm);

        PairsData ei0_dtz_precomp = ei0_dtz.precomp;
        assertEquals(49432, ei0_dtz_precomp.indexTable.ptr);
        assertEquals(281104, ei0_dtz_precomp.sizeTable.ptr);
        assertEquals(2631808, ei0_dtz_precomp.data.ptr);
        assertEquals(42, ei0_dtz_precomp.offset.ptr);
        assertEquals(86, ei0_dtz_precomp.symPat.ptr);

        assertEquals(7, ei0_dtz_precomp.blockSize);
        assertEquals(19, ei0_dtz_precomp.idxBits);
        assertEquals(1, ei0_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei0_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x8000000000000000L,
                0x7C00000000000000L,
                0x7400000000000000L,
                0x6480000000000000L,
                0x4FC0000000000000L,
                0x3BC0000000000000L,
                0x2A10000000000000L,
                0x1A20000000000000L,
                0xDF4000000000000L,
                0x368000000000000L,
                0xB000000000000L,
                0x1000000000000L,
                0x800000000000L,
                0x200000000000L,
                0L}, ei0_dtz_precomp.base);
        assertEquals(4095, ei0_dtz_precomp.symLen.length);


        EncInfo ei1_dtz = dtz.ei_dtz[1];
        assertArrayEquals(new long[]{843461640, 1, 63, 3906, 238266, 14295960, 0}, ei1_dtz.factor);
        assertArrayEquals(new byte[]{1, 2, 4, 14, 6, 10, 0}, ei1_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei1_dtz.norm);

        PairsData ei1_dtz_precomp = ei1_dtz.precomp;
        assertEquals(107350, ei1_dtz_precomp.indexTable.ptr);
        assertEquals(825508, ei1_dtz_precomp.sizeTable.ptr);
        assertEquals(37473664, ei1_dtz_precomp.data.ptr);
        assertEquals(12378, ei1_dtz_precomp.offset.ptr);
        assertEquals(12418, ei1_dtz_precomp.symPat.ptr);

        assertEquals(7, ei1_dtz_precomp.blockSize);
        assertEquals(19, ei1_dtz_precomp.idxBits);
        assertEquals(2, ei1_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei1_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xB800000000000000L,
                0xA700000000000000L,
                0x8A00000000000000L,
                0x6500000000000000L,
                0x4980000000000000L,
                0x3030000000000000L,
                0x1C38000000000000L,
                0xC14000000000000L,
                0x28000000000000L,
                0x3000000000000L,
                0x800000000000L,
                0}, ei1_dtz_precomp.base);
        assertEquals(4095, ei1_dtz_precomp.symLen.length);


        EncInfo ei2_dtz = dtz.ei_dtz[2];
        assertArrayEquals(new long[]{3906, 1, 63, 23436, 1429596, 85775760, 0}, ei2_dtz.factor);
        assertArrayEquals(new byte[]{1, 2, 4, 10, 14, 6, 0}, ei2_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei2_dtz.norm);

        PairsData ei2_dtz_precomp = ei2_dtz.precomp;
        assertEquals(165268, ei2_dtz_precomp.indexTable.ptr);
        assertEquals(1406992, ei2_dtz_precomp.sizeTable.ptr);
        assertEquals(74688640, ei2_dtz_precomp.data.ptr);
        assertEquals(24710, ei2_dtz_precomp.offset.ptr);
        assertEquals(24754, ei2_dtz_precomp.symPat.ptr);

        assertEquals(7, ei2_dtz_precomp.blockSize);
        assertEquals(19, ei2_dtz_precomp.idxBits);
        assertEquals(2, ei2_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei2_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xBC00000000000000L,
                0xAB00000000000000L,
                0x9000000000000000L,
                0x6540000000000000L,
                0x4960000000000000L,
                0x30A0000000000000L,
                0x1CB8000000000000L,
                0xB8C000000000000L,
                0x18000000000000L,
                0x2000000000000L,
                0x800000000000L,
                0x400000000000L,
                0x200000000000L,
                0}, ei2_dtz_precomp.base);
        assertEquals(4095, ei2_dtz_precomp.symLen.length);


        EncInfo ei3_dtz = dtz.ei_dtz[3];
        assertArrayEquals(new long[]{843461640, 1, 63, 3906, 238266, 14295960, 0}, ei3_dtz.factor);
        assertArrayEquals(new byte[]{1, 2, 4, 6, 10, 14, 0}, ei3_dtz.pieces);
        assertArrayEquals(new byte[]{1, 1, 1, 1, 1, 1, 0}, ei3_dtz.norm);

        PairsData ei3_dtz_precomp = ei3_dtz.precomp;
        assertEquals(223186, ei3_dtz_precomp.indexTable.ptr);
        assertEquals(2022448, ei3_dtz_precomp.sizeTable.ptr);
        assertEquals(114077824, ei3_dtz_precomp.data.ptr);
        assertEquals(37046, ei3_dtz_precomp.offset.ptr);
        assertEquals(37088, ei3_dtz_precomp.symPat.ptr);

        assertEquals(7, ei3_dtz_precomp.blockSize);
        assertEquals(19, ei3_dtz_precomp.idxBits);
        assertEquals(2, ei3_dtz_precomp.minLen);

        assertArrayEquals(new byte[]{0, 0}, ei3_dtz_precomp.constValue);
        assertArrayEquals(new long[]{
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xC000000000000000L,
                0xBE00000000000000L,
                0xAA00000000000000L,
                0x9100000000000000L,
                0x6480000000000000L,
                0x4980000000000000L,
                0x3010000000000000L,
                0x1CB8000000000000L,
                0xBBC000000000000L,
                0x14000000000000L,
                0x2000000000000L,
                0x800000000000L,
                0x400000000000L,
                0}, ei3_dtz_precomp.base);
        assertEquals(4095, ei3_dtz_precomp.symLen.length);
    }


    public String md5sum(String filename) {
        try {
            Path tbPath = PATH.resolve(filename);
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
